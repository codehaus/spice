package org.realityforge.packet.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.buffers.BufferManager;
import org.codehaus.spice.netevent.events.InputDataPresentEvent;
import org.codehaus.spice.netevent.handlers.AbstractDirectedHandler;
import org.codehaus.spice.netevent.transport.ChannelTransport;
import org.realityforge.packet.Packet;
import org.realityforge.packet.events.AckEvent;
import org.realityforge.packet.events.NackEvent;
import org.realityforge.packet.events.PacketReadEvent;
import org.realityforge.packet.events.SessionEstablishedEvent;
import org.realityforge.packet.events.TransportDisconnectRequestEvent;
import org.realityforge.packet.session.Session;
import org.realityforge.packet.session.SessionManager;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2004-01-13 23:35:44 $
 */
public class SessionInputEventHandler
    extends AbstractDirectedHandler
{
    /** The associated BufferManager used to create ByteBuffers for incoming packets. */
    private final BufferManager _bufferManager;

    /** The SessionManager via which sessions are located and created. */
    private final SessionManager _sessionManager;

    /**
     * Create handler with specified destination sink.
     * 
     * @param sink the destination
     */
    public SessionInputEventHandler( final EventSink sink,
                                     final BufferManager bufferManager,
                                     final SessionManager sessionManager )
    {
        super( sink );
        if( null == bufferManager )
        {
            throw new NullPointerException( "bufferManager" );
        }
        if( null == sessionManager )
        {
            throw new NullPointerException( "sessionManager" );
        }
        _bufferManager = bufferManager;
        _sessionManager = sessionManager;
    }

    /**
     * @see EventHandler#handleEvent(Object)
     */
    public void handleEvent( final Object event )
    {
        final InputDataPresentEvent ie = (InputDataPresentEvent)event;
        final ChannelTransport transport = ie.getTransport();
        try
        {
            boolean dataPresent = true;
            while( dataPresent )
            {
                if( null == transport.getUserData() )
                {
                    dataPresent = handleUnconnectedInput( transport );
                }
                else
                {
                    dataPresent = handleConnectedInput( transport );
                }
            }
        }
        catch( final IOException ioe )
        {
            disconnectTransport( transport, Protocol.ERROR_IO_ERROR );
        }
    }

    /**
     * Handle input from an unconnected transport.
     * 
     * @param transport the transport
     * @throws IOException if an error occurs reading from transport
     */
    boolean handleUnconnectedInput( final ChannelTransport transport )
        throws IOException
    {
        final InputStream input = transport.getInputStream();
        final int available = input.available();
        if( available >= Protocol.SIZEOF_GREETING )
        {
            if( !checkMagicNumber( input ) )
            {
                disconnectTransport( transport,
                                     Protocol.ERROR_BAD_MAGIC );
                return false;
            }

            final long sessionID = TypeIOUtil.readLong( input );
            final short authID = TypeIOUtil.readShort( input );
            if( -1 == sessionID )
            {
                final Session session = _sessionManager.newSession();
                session.setTransport( transport );
                sendConnectMessage( session );
                return true;
            }
            else
            {
                final Session session =
                    _sessionManager.findSession( sessionID );
                if( null == session )
                {
                    disconnectTransport( transport,
                                         Protocol.ERROR_BAD_SESSION );
                    return false;
                }
                else if( session.getAuthID() != authID )
                {
                    disconnectTransport( transport,
                                         Protocol.ERROR_BAD_AUTH );
                    return false;
                }
                else
                {
                    return true;
                }
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Send message indicating serverside has connected session.
     * 
     * @param session the session
     * @throws IOException if unable to write message
     */
    void sendConnectMessage( final Session session )
        throws IOException
    {
        final OutputStream output =
            session.getTransport().getOutputStream();
        output.write( Protocol.S2C_CONNECTED );
        TypeIOUtil.writeLong( output, session.getSessionID() );
        TypeIOUtil.writeShort( output, session.getAuthID() );
        output.flush();
    }

    /**
     * Check if streams starts with correct magic number.
     * 
     * @param input the input stream.
     * @return true if stream starts with magic number
     * @throws IOException if error reading from stream
     */
    boolean checkMagicNumber( final InputStream input )
        throws IOException
    {
        final byte[] magic = Protocol.MAGIC;
        for( int i = 0; i < magic.length; i++ )
        {
            final byte b = magic[ i ];
            final int in = input.read();
            if( b != in )
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Handle input from a connected transport. A Transport is considered
     * connected if it gets through initial magic number handshake and session
     * establishment.
     * 
     * @param transport the transport
     * @throws IOException if error reading input
     */
    boolean handleConnectedInput( final ChannelTransport transport )
        throws IOException
    {
        final InputStream input = transport.getInputStream();
        final int available = input.available();
        if( available < Protocol.SIZEOF_BYTE )
        {
            return false;
        }

        input.mark( Protocol.SIZEOF_MSG_HEADER );

        final int msg = input.read();
        if( Protocol.C2S_ESTABLISHED == msg )
        {
            handleEstablish( transport );
            return true;
        }
        else if( Protocol.MSG_DISCONNECT == msg )
        {
            return handleDisconnect( transport );
        }
        else if( Protocol.MSG_DATA == msg )
        {
            return handleDataMessage( transport );
        }
        else if( Protocol.MSG_ACK == msg )
        {
            handleAck( transport );
            return true;
        }
        else if( Protocol.MSG_NACK == msg )
        {
            handleNack( transport );
            return true;
        }
        else
        {
            disconnectTransport( transport, Protocol.ERROR_BAD_MESSAGE );
            return false;
        }
    }

    /**
     * Handle an establish message.
     * 
     * @param transport the transport
     */
    void handleEstablish( final ChannelTransport transport )
    {
        final Session session = (Session)transport.getUserData();
        session.setStatus( Session.STATUS_ESTABLISHED );
        getSink().addEvent( new SessionEstablishedEvent( session ) );
    }

    /**
     * Handle an ack message.
     * 
     * @param transport the transport
     * @throws IOException if io error occurs
     */
    void handleAck( final ChannelTransport transport )
        throws IOException
    {
        ensureValidSession( transport );
        final Session session = (Session)transport.getUserData();
        final InputStream input = transport.getInputStream();
        final short sequence = TypeIOUtil.readShort( input );
        getSink().addEvent( new AckEvent( session, sequence ) );
    }

    /**
     * Handle a nack message.
     * 
     * @param transport the transport
     * @throws IOException if io error occurs
     */
    void handleNack( final ChannelTransport transport )
        throws IOException
    {
        ensureValidSession( transport );
        final Session session = (Session)transport.getUserData();
        final InputStream input = transport.getInputStream();
        final short sequence = TypeIOUtil.readShort( input );
        getSink().addEvent( new NackEvent( session, sequence ) );
    }

    /**
     * Handle a disconnect message.
     * 
     * @param transport the transport
     * @throws IOException if io error occurs
     */
    boolean handleDisconnect( final ChannelTransport transport )
        throws IOException
    {
        final InputStream input = transport.getInputStream();
        final int available = input.available();
        if( available < Protocol.SIZEOF_BYTE )
        {
            input.reset();
            return false;
        }
        else
        {
            final byte reason = (byte)input.read();
            disconnectTransport( transport, reason );
            return true;
        }
    }

    /**
     * Handle a data message.
     * 
     * @param transport the transport
     * @throws IOException if io error occurs
     */
    boolean handleDataMessage( final ChannelTransport transport )
        throws IOException
    {
        ensureValidSession( transport );
        final InputStream input = transport.getInputStream();
        final short length = TypeIOUtil.readShort( input );
        if( input.available() < length )
        {
            input.reset();
            return false;
        }
        else if( length < Protocol.SIZEOF_SHORT )
        {
            //Can not get sequence number out
            disconnectTransport( transport, Protocol.ERROR_NO_SEQUENCE );
            return false;
        }
        else
        {
            final short sequence = TypeIOUtil.readShort( input );
            final int count = length - Protocol.SIZEOF_SHORT;
            final ByteBuffer buffer = _bufferManager.aquireBuffer( count );
            for( int i = 0; i < count; i++ )
            {
                final int data = input.read();
                buffer.put( (byte)data );
            }
            final Session session = (Session)transport.getUserData();
            final Packet packet = new Packet( sequence, 0, buffer );
            getSink().addEvent( new PacketReadEvent( session, packet ) );
            return true;
        }
    }

    /**
     * Ensure that transport has a session that is established else throw an
     * IOException.
     * 
     * @param transport the transport
     * @throws IOException if session is not present and established
     */
    void ensureValidSession( final ChannelTransport transport )
        throws IOException
    {
        final Session session = (Session)transport.getUserData();
        if( null == session )
        {
            throw new IOException( "No session specififed." );
        }
        if( Session.STATUS_ESTABLISHED != session.getStatus() )
        {
            throw new IOException( "Session not established." );
        }
    }

    /**
     * Send event indicating that transport should be disconnected.
     * 
     * @param transport the transport
     * @param reason the reason for disconnect
     */
    void disconnectTransport( final ChannelTransport transport,
                              final byte reason )
    {
        final TransportDisconnectRequestEvent error =
            new TransportDisconnectRequestEvent( transport,
                                                 reason );
        getSink().addEvent( error );
    }
}



