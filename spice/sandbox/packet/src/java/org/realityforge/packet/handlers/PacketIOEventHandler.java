package org.realityforge.packet.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.buffers.BufferManager;
import org.codehaus.spice.netevent.events.ChannelClosedEvent;
import org.codehaus.spice.netevent.events.ConnectEvent;
import org.codehaus.spice.netevent.events.InputDataPresentEvent;
import org.codehaus.spice.netevent.handlers.AbstractDirectedHandler;
import org.codehaus.spice.netevent.transport.ChannelTransport;
import org.codehaus.spice.netevent.transport.MultiBufferInputStream;
import org.codehaus.spice.netevent.transport.TransportOutputStream;
import org.realityforge.packet.Packet;
import org.realityforge.packet.events.AckEvent;
import org.realityforge.packet.events.AckRequestEvent;
import org.realityforge.packet.events.NackEvent;
import org.realityforge.packet.events.NackRequestEvent;
import org.realityforge.packet.events.PacketReadEvent;
import org.realityforge.packet.events.PacketWriteRequestEvent;
import org.realityforge.packet.events.SessionEstablishedEvent;
import org.realityforge.packet.events.SessionEstablishedRequestEvent;
import org.realityforge.packet.events.SessionEvent;
import org.realityforge.packet.events.TransportDisconnectRequestEvent;
import org.realityforge.packet.session.Session;
import org.realityforge.packet.session.SessionManager;

/**
 * @author Peter Donald
 * @version $Revision: 1.4 $ $Date: 2004-01-16 06:48:00 $
 */
public class PacketIOEventHandler
    extends AbstractDirectedHandler
{
    /** The associated BufferManager used to create ByteBuffers for incoming packets. */
    private final BufferManager _bufferManager;

    /** The SessionManager via which sessions are located and created. */
    private final SessionManager _sessionManager;

    /** The destination of all events destined for next layer. */
    private final EventSink _target;

    /**
     * Create handler with specified destination sink.
     * 
     * @param sink the destination
     */
    public PacketIOEventHandler( final EventSink sink,
                                 final EventSink target,
                                 final BufferManager bufferManager,
                                 final SessionManager sessionManager )
    {
        super( sink );
        if( null == target )
        {
            throw new NullPointerException( "target" );
        }
        if( null == bufferManager )
        {
            throw new NullPointerException( "bufferManager" );
        }
        if( null == sessionManager )
        {
            throw new NullPointerException( "sessionManager" );
        }
        _target = target;
        _bufferManager = bufferManager;
        _sessionManager = sessionManager;
    }

    /**
     * @see EventHandler#handleEvent(Object)
     */
    public void handleEvent( final Object event )
    {
        if( event instanceof InputDataPresentEvent )
        {
            final InputDataPresentEvent ie = (InputDataPresentEvent)event;
            handleInput( ie );
        }
        else if( event instanceof ConnectEvent )
        {
            final ConnectEvent ce = (ConnectEvent)event;
            handleGreeting( ce );
        }
        else if( event instanceof ChannelClosedEvent )
        {
            final ChannelClosedEvent cc = (ChannelClosedEvent)event;
            handleClose( cc );
        }
        else if( event instanceof TransportDisconnectRequestEvent )
        {
            final TransportDisconnectRequestEvent e =
                (TransportDisconnectRequestEvent)event;
            handleDisconnect( e );
        }
        else
        {
            handleOutput( event );
        }
    }

    /**
     * Handle close of transport event.
     * 
     * @param cc the close event
     */
    private void handleClose( final ChannelClosedEvent cc )
    {
        final ChannelTransport transport = cc.getTransport();
        final Session session = (Session)transport.getUserData();
        if( null != session )
        {
            session.setTransport( null );
        }
    }

    /**
     * Handle initial greeting event event.
     * 
     * @param e the event
     */
    private void handleGreeting( final ConnectEvent e )
    {
        final ChannelTransport transport = e.getTransport();
        final Session session = (Session)transport.getUserData();

        if( null != session && session.isClient() )
        {
            try
            {
                sendGreeting( transport,
                              session.getSessionID(),
                              session.getAuthID() );
            }
            catch( final IOException ioe )
            {
                signalDisconnectTransport( transport,
                                           Protocol.ERROR_IO_ERROR,
                                           getSink() );
            }
        }
    }

    /**
     * Handle disconenect event.
     * 
     * @param e the event
     */
    private void handleDisconnect( final TransportDisconnectRequestEvent e )
    {
        final ChannelTransport transport = e.getTransport();
        try
        {
            sendDisconnect( transport, e.getReason() );
            transport.getOutputStream().close();
        }
        catch( final IOException ioe )
        {
        }
    }

    /**
     * Handle event relating to output.
     * 
     * @param event the output event
     */
    private void handleOutput( final Object event )
    {
        final SessionEvent se = (SessionEvent)event;
        final Session session = se.getSession();
        final ChannelTransport transport = session.getTransport();
        try
        {
            if( event instanceof AckRequestEvent )
            {
                final AckRequestEvent e = (AckRequestEvent)event;
                sendAck( transport, e.getSequence() );
            }
            else if( event instanceof NackRequestEvent )
            {
                final NackRequestEvent e = (NackRequestEvent)event;
                sendNack( transport, e.getSequence() );
            }
            else if( event instanceof SessionEstablishedRequestEvent )
            {
                sendEstablished( transport );
            }
            else if( event instanceof PacketWriteRequestEvent )
            {
                final PacketWriteRequestEvent e =
                    (PacketWriteRequestEvent)event;
                sendData( transport, e.getPacket() );
            }
            else
            {
                signalDisconnectTransport( transport,
                                           Protocol.ERROR_BAD_MESSAGE,
                                           getSink() );
            }
        }
        catch( final IOException ioe )
        {
            signalDisconnectTransport( transport,
                                       Protocol.ERROR_IO_ERROR,
                                       getSink() );
        }
    }

    /**
     * Handle input from transport.
     * 
     * @param ie the event indicating data present
     */
    private void handleInput( final InputDataPresentEvent ie )
    {
        final ChannelTransport transport = ie.getTransport();
        try
        {
            boolean dataPresent = true;
            while( dataPresent )
            {
                if( null == transport.getUserData() )
                {
                    dataPresent = receiveGreeting( transport );
                }
                else
                {
                    dataPresent = handleConnectedInput( transport );
                }
            }
        }
        catch( final IOException ioe )
        {
            ioe.printStackTrace( System.out );
            signalDisconnectTransport( transport,
                                       Protocol.ERROR_IO_ERROR,
                                       getSink() );
        }
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

        input.mark( Protocol.SIZEOF_BYTE );

        final Session session = (Session)transport.getUserData();

        final int msg = input.read();
        if( !session.isClient() && Protocol.C2S_ESTABLISHED == msg )
        {
            return receiveEstablish( transport );
        }
        else if( session.isClient() && Protocol.S2C_CONNECT == msg )
        {
            return receiveConnect( transport );
        }
        else if( Protocol.MSG_DISCONNECT == msg )
        {
            return receiveDisconnect( transport );
        }
        else if( Protocol.MSG_DATA == msg )
        {
            return receiveData( transport );
        }
        else if( Protocol.MSG_ACK == msg )
        {
            return receiveAck( transport );
        }
        else if( Protocol.MSG_NACK == msg )
        {
            return receiveNack( transport );
        }
        else
        {
            signalDisconnectTransport( transport,
                                       Protocol.ERROR_BAD_MESSAGE,
                                       getSink() );
            return false;
        }
    }

    /**
     * Send a the greeting at start of stream.
     * 
     * @param transport the transport
     */
    private void sendGreeting( final ChannelTransport transport,
                               final long sessionID,
                               final short authID )
        throws IOException
    {
        final OutputStream output = transport.getOutputStream();
        final byte[] magic = Protocol.MAGIC;
        for( int i = 0; i < magic.length; i++ )
        {
            output.write( magic[ i ] );
        }
        TypeIOUtil.writeLong( output, sessionID );
        TypeIOUtil.writeShort( output, authID );
        output.flush();
    }

    /**
     * Receive greeting that occurs at start of stream.
     * 
     * @param transport the transport
     * @throws IOException if an error occurs reading from transport
     */
    private boolean receiveGreeting( final ChannelTransport transport )
        throws IOException
    {
        final MultiBufferInputStream input = transport.getInputStream();
        final int available = input.available();
        if( available >= Protocol.SIZEOF_GREETING )
        {
            if( !checkMagicNumber( input ) )
            {
                signalDisconnectTransport( transport,
                                           Protocol.ERROR_BAD_MAGIC,
                                           getSink() );
                return false;
            }

            final long sessionID = TypeIOUtil.readLong( input );
            final short authID = TypeIOUtil.readShort( input );
            if( -1 == sessionID )
            {
                final Session session = _sessionManager.newSession();
                session.setTransport( transport );
                sendConnect( transport,
                             session.getSessionID(),
                             session.getAuthID() );
                return true;
            }
            else
            {
                final Session session =
                    _sessionManager.findSession( sessionID );
                if( null == session )
                {
                    signalDisconnectTransport( transport,
                                               Protocol.ERROR_BAD_SESSION,
                                               getSink() );
                    return false;
                }
                else if( session.getAuthID() != authID )
                {
                    signalDisconnectTransport( transport,
                                               Protocol.ERROR_BAD_AUTH,
                                               getSink() );
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
     * Check if streams starts with correct magic number.
     * 
     * @param input the input stream.
     * @return true if stream starts with magic number
     * @throws IOException if error reading from stream
     */
    private boolean checkMagicNumber( final InputStream input )
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
     * Send a Connect message.
     * 
     * @param transport the transport
     */
    private void sendConnect( final ChannelTransport transport,
                              final long sessionID,
                              final short authID )
        throws IOException
    {
        final OutputStream output = transport.getOutputStream();
        output.write( Protocol.S2C_CONNECT );
        TypeIOUtil.writeLong( output, sessionID );
        TypeIOUtil.writeShort( output, authID );
        output.flush();
    }

    /**
     * Handle an establish message.
     * 
     * @param transport the transport
     */
    private boolean receiveConnect( final ChannelTransport transport )
        throws IOException
    {
        final Session session = (Session)transport.getUserData();
        final InputStream input = transport.getInputStream();
        final int available = input.available();
        if( available < Protocol.SIZEOF_LONG + Protocol.SIZEOF_SHORT )
        {
            input.reset();
            return false;
        }
        else
        {
            final long sessionID = TypeIOUtil.readLong( input );
            final short authID = TypeIOUtil.readShort( input );
            session.setStatus( Session.STATUS_ESTABLISHED );
            session.setSessionID( sessionID );
            session.setAuthID( authID );
            _target.addEvent( new SessionEstablishedEvent( session ) );
            return true;
        }
    }

    /**
     * Send an establish message.
     * 
     * @param transport the transport
     */
    private void sendEstablished( final ChannelTransport transport )
        throws IOException
    {
        ensureValidSession( transport );
        final TransportOutputStream output = transport.getOutputStream();
        output.write( Protocol.C2S_ESTABLISHED );
        output.flush();
    }

    /**
     * Receive an establish message.
     * 
     * @param transport the transport
     */
    private boolean receiveEstablish( final ChannelTransport transport )
    {
        final Session session = (Session)transport.getUserData();
        session.setStatus( Session.STATUS_ESTABLISHED );
        _target.addEvent( new SessionEstablishedEvent( session ) );
        return true;
    }

    /**
     * Send an ack message.
     * 
     * @param transport the transport
     * @throws IOException if io error occurs
     */
    private void sendAck( final ChannelTransport transport,
                          final short sequence )
        throws IOException
    {
        ensureValidSession( transport );
        final TransportOutputStream output = transport.getOutputStream();
        output.write( Protocol.MSG_ACK );
        TypeIOUtil.writeShort( output, sequence );
        output.flush();
    }

    /**
     * Receive an ack message.
     * 
     * @param transport the transport
     * @throws IOException if io error occurs
     */
    private boolean receiveAck( final ChannelTransport transport )
        throws IOException
    {

        ensureValidSession( transport );
        final Session session = (Session)transport.getUserData();
        final InputStream input = transport.getInputStream();
        final int available = input.available();
        if( available < Protocol.SIZEOF_SHORT )
        {
            input.reset();
            return false;
        }
        else
        {
            final short sequence = TypeIOUtil.readShort( input );
            getSink().addEvent( new AckEvent( session, sequence ) );
            return true;
        }
    }

    /**
     * Send a nack message.
     * 
     * @param transport the transport
     * @throws IOException if io error occurs
     */
    private void sendNack( final ChannelTransport transport,
                           final short sequence )
        throws IOException
    {
        ensureValidSession( transport );
        final TransportOutputStream output = transport.getOutputStream();
        output.write( Protocol.MSG_NACK );
        TypeIOUtil.writeShort( output, sequence );
        output.flush();
    }

    /**
     * Receive a nack message.
     * 
     * @param transport the transport
     * @throws IOException if io error occurs
     */
    private boolean receiveNack( final ChannelTransport transport )
        throws IOException
    {
        ensureValidSession( transport );
        final Session session = (Session)transport.getUserData();
        final InputStream input = transport.getInputStream();
        final int available = input.available();
        if( available < Protocol.SIZEOF_SHORT )
        {
            input.reset();
            return false;
        }
        else
        {
            final short sequence = TypeIOUtil.readShort( input );
            getSink().addEvent( new NackEvent( session, sequence ) );
            return true;
        }
    }

    /**
     * Receive a disconnect message.
     * 
     * @param transport the transport
     * @throws IOException if io error occurs
     */
    private void sendDisconnect( final ChannelTransport transport,
                                 final byte reason )
        throws IOException
    {
        ensureValidSession( transport );
        final TransportOutputStream output = transport.getOutputStream();
        output.write( Protocol.MSG_DISCONNECT );
        output.write( reason );
        output.flush();
    }

    /**
     * Receive a disconnect message.
     * 
     * @param transport the transport
     * @return if message received
     * @throws IOException if io error occurs
     */
    private boolean receiveDisconnect( final ChannelTransport transport )
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
            signalDisconnectTransport( transport, reason,
                                       getSink() );
            return true;
        }
    }

    /**
     * Send a data message.
     * 
     * @param transport the transport
     * @throws IOException if io error occurs
     */
    private void sendData( final ChannelTransport transport,
                           final Packet packet )
        throws IOException
    {
        ensureValidSession( transport );
        final TransportOutputStream output = transport.getOutputStream();
        output.write( Protocol.MSG_DATA );
        TypeIOUtil.writeShort( output, packet.getSequence() );

        final ByteBuffer data = packet.getData();
        final int dataSize = data.limit();
        TypeIOUtil.writeShort( output, (short)dataSize );
        while( data.remaining() > 0 )
        {
            output.write( data.get() );
        }
        output.flush();
        _bufferManager.releaseBuffer( data );
    }

    /**
     * Receive a data message.
     * 
     * @param transport the transport
     * @return if message received
     * @throws IOException if io error occurs
     */
    private boolean receiveData( final ChannelTransport transport )
        throws IOException
    {
        ensureValidSession( transport );
        final InputStream input = transport.getInputStream();
        final int available = input.available();
        if( available < Protocol.SIZEOF_SHORT + Protocol.SIZEOF_SHORT )
        {
            input.reset();
            return false;
        }

        final short sequence = TypeIOUtil.readShort( input );
        final short length = TypeIOUtil.readShort( input );
        if( input.available() < length )
        {
            input.reset();
            return false;
        }
        else
        {
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
    private void ensureValidSession( final ChannelTransport transport )
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
     * @param sink the sink to send message to
     */
    private void
        signalDisconnectTransport( final ChannelTransport transport,
                                   final byte reason,
                                   final EventSink sink )
    {
        final TransportDisconnectRequestEvent error =
            new TransportDisconnectRequestEvent( transport,
                                                 reason );
        sink.addEvent( error );
    }
}



