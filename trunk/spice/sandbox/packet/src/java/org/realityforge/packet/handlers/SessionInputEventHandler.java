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
 * @version $Revision: 1.2 $ $Date: 2004-01-13 06:59:47 $
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
            if( null == transport.getUserData() )
            {
                handleUnconnectedInput( transport );
            }
            else
            {
                handleConnectedInput( transport );
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
    void handleUnconnectedInput( final ChannelTransport transport )
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
                return;
            }

            final long sessionID = TypeIOUtil.readLong( input );
            final short authID = TypeIOUtil.readShort( input );
            if( -1 == sessionID )
            {
                final Session session = _sessionManager.newSession();
                session.setTransport( transport );
                sendConnectMessage( session );
            }
            else
            {
                final Session session =
                    _sessionManager.findSession( sessionID );
                if( null == session )
                {
                    disconnectTransport( transport,
                                         Protocol.ERROR_BAD_SESSION );
                }
                else if( session.getAuthID() != authID )
                {
                    disconnectTransport( transport,
                                         Protocol.ERROR_BAD_AUTH );
                }
            }
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
    void handleConnectedInput( final ChannelTransport transport )
        throws IOException
    {
        final InputStream input = transport.getInputStream();
        final int available = input.available();
        if( available < Protocol.SIZEOF_BYTE )
        {
            return;
        }

        input.mark( Protocol.SIZEOF_MSG_HEADER );

        final int msg = input.read();
        if( Protocol.C2S_ESTABLISHED == msg )
        {
            handleEstablish( transport );
        }
        else if( Protocol.MSG_DISCONNECT == msg )
        {
            handleDisconnect( transport );
        }
        else if( Protocol.MSG_DATA == msg )
        {
            handleDataMessage( transport );
        }
        else if( Protocol.MSG_ACK == msg )
        {
            handleAck( transport );
        }
        else if( Protocol.MSG_NACK == msg )
        {
            handleNack( transport );
        }
        else
        {
            disconnectTransport( transport, Protocol.ERROR_BAD_MESSAGE );
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
    void handleDisconnect( final ChannelTransport transport )
        throws IOException
    {
        final InputStream input = transport.getInputStream();
        final int available = input.available();
        if( available < Protocol.SIZEOF_BYTE )
        {
            input.reset();
        }
        else
        {
            final byte reason = (byte)input.read();
            disconnectTransport( transport, reason );
        }
    }

    /**
     * Handle a data message.
     * 
     * @param transport the transport
     * @throws IOException if io error occurs
     */
    void handleDataMessage( final ChannelTransport transport )
        throws IOException
    {
        ensureValidSession( transport );
        final InputStream input = transport.getInputStream();
        final short length = TypeIOUtil.readShort( input );
        if( input.available() < length )
        {
            input.reset();
        }
        else if( length < Protocol.SIZEOF_SHORT )
        {
            //Can not get sequence number out
            disconnectTransport( transport, Protocol.ERROR_NO_SEQUENCE );
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



