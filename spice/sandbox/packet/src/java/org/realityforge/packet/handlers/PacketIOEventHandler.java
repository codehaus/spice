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
import org.codehaus.spice.timeevent.events.TimeEvent;
import org.codehaus.spice.timeevent.source.SchedulingKey;
import org.codehaus.spice.timeevent.source.TimeEventSource;
import org.codehaus.spice.timeevent.triggers.PeriodicTimeTrigger;
import org.realityforge.packet.Packet;
import org.realityforge.packet.events.AckEvent;
import org.realityforge.packet.events.AckRequestEvent;
import org.realityforge.packet.events.DataPacketReadyEvent;
import org.realityforge.packet.events.NackEvent;
import org.realityforge.packet.events.NackRequestEvent;
import org.realityforge.packet.events.PacketReadEvent;
import org.realityforge.packet.events.PacketWriteRequestEvent;
import org.realityforge.packet.events.SessionActiveEvent;
import org.realityforge.packet.events.SessionConnectEvent;
import org.realityforge.packet.events.SessionDisconnectEvent;
import org.realityforge.packet.events.SessionDisconnectRequestEvent;
import org.realityforge.packet.events.SessionEstablishRequestEvent;
import org.realityforge.packet.events.SessionEvent;
import org.realityforge.packet.events.SessionInactiveEvent;
import org.realityforge.packet.events.TransportDisconnectRequestEvent;
import org.realityforge.packet.session.PacketQueue;
import org.realityforge.packet.session.Session;
import org.realityforge.packet.session.SessionManager;

/**
 * @author Peter Donald
 * @version $Revision: 1.11 $ $Date: 2004-02-02 01:33:57 $
 */
public class PacketIOEventHandler
    extends AbstractDirectedHandler
{
    /**
     * The number of milliseconds before the session will timeout.
     */
    private static final int TIMEOUT_IN_MILLIS = 30 * 1000;

    /**
     * The time event source to use to add timeouts.
     */
    private final TimeEventSource _timeEventSource;

    /**
     * The associated BufferManager used to create ByteBuffers for incoming
     * packets.
     */
    private final BufferManager _bufferManager;

    /**
     * The SessionManager via which sessions are located and created.
     */
    private final SessionManager _sessionManager;

    /**
     * The destination of all events destined for next layer.
     */
    private final EventSink _target;

    /**
     * Create handler with specified destination sink.
     * 
     * @param sink the destination
     */
    public PacketIOEventHandler( final TimeEventSource timeEventSource,
                                 final EventSink sink,
                                 final EventSink target,
                                 final BufferManager bufferManager,
                                 final SessionManager sessionManager )
    {
        super( sink );
        if( null == timeEventSource )
        {
            throw new NullPointerException( "timeEventSource" );
        }
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
        _timeEventSource = timeEventSource;
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
        else if( event instanceof AckEvent )
        {
            handleAck( (AckEvent)event );
        }
        else if( event instanceof NackEvent )
        {
            handleNack( (NackEvent)event );
        }
        else if( event instanceof PacketReadEvent )
        {
            final PacketReadEvent e = (PacketReadEvent)event;
            handlePacketReadEvent( e.getSession(), e.getPacket() );
        }
        else if( event instanceof TimeEvent )
        {
            final TimeEvent e = (TimeEvent)event;
            handleTimeout( e );
        }
        else if( event instanceof SessionEstablishRequestEvent )
        {
            final SessionEstablishRequestEvent e = (SessionEstablishRequestEvent)event;
            handleEstablish( e );
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
        else if( event instanceof SessionDisconnectRequestEvent )
        {
            final SessionDisconnectRequestEvent e =
                (SessionDisconnectRequestEvent)event;
            handleSessionDisconnect( e.getSession() );
        }
        else if( event instanceof TransportDisconnectRequestEvent )
        {
            final TransportDisconnectRequestEvent e =
                (TransportDisconnectRequestEvent)event;
            handleDisconnect( e.getTransport(), e.getReason() );
        }
        else
        {
            handleOutput( event );
        }
    }

    private void handleAck( final AckEvent ce )
    {
        final short sequence = ce.getSequence();
        final Session session = ce.getSession();
        final PacketQueue queue = session.getMessageQueue();
        queue.ack( sequence );
        if( session.isPendingDisconnect() )
        {
            final SessionDisconnectRequestEvent response =
                new SessionDisconnectRequestEvent( session );
            getSink().addEvent( response );
        }
    }

    private void handleNack( final NackEvent ce )
    {
        final short sequence = ce.getSequence();
        final Session session = ce.getSession();
        final Packet packet =
            session.getMessageQueue().getPacket( sequence );
        if( null == packet )
        {
            final TransportDisconnectRequestEvent error =
                new TransportDisconnectRequestEvent( session.getTransport(),
                                                     Protocol.ERROR_BAD_NACK );
            getSink().addEvent( error );
        }
        else
        {
            final PacketWriteRequestEvent response =
                new PacketWriteRequestEvent( session, packet );
            getSink().addEvent( response );
        }
    }

    /**
     * Handle timeout of conenction.
     *
     * @param e the event
     */
    private void handleTimeout( final TimeEvent e )
    {
        final SchedulingKey key = e.getKey();
        key.cancel();
        final Session session = (Session)key.getUserData();
        final int status = session.getStatus();
        if( Session.STATUS_LOST == status )
        {
            session.setPendingDisconnect();
            final SessionDisconnectRequestEvent response =
                new SessionDisconnectRequestEvent( session );
            getSink().addEvent( response );
        }
    }

    /**
     * Handle message to disconnect Session gracefully.
     * 
     * @param session the session
     */
    private void handleSessionDisconnect( final Session session )
    {
        final ChannelTransport transport = session.getTransport();
        if( null != transport )
        {
            handleDisconnect( transport, Protocol.ERROR_NONE );
        }
        disconnectSession( session );
    }

    private void disconnectSession( final Session session )
    {
        session.setStatus( Session.STATUS_DISCONNECTED );
        _sessionManager.deleteSession( session );
        _target.addEvent( new SessionDisconnectEvent( session ) );
    }

    /**
     * Handle Establish event on clientside.
     * 
     * @param e the event
     */
    private void handleEstablish( final SessionEstablishRequestEvent e )
    {
        final Session session = e.getSession();
        final ChannelTransport transport = session.getTransport();
        if( canOutput( transport ) )
        {
            return;
        }
        try
        {
            sendEstablished( transport );
            sendSessionStatus( session );
            _target.addEvent( new SessionActiveEvent( session ) );
        }
        catch( final IOException ioe )
        {
            ioe.printStackTrace( System.out );
            signalDisconnectTransport( transport,
                                       Protocol.ERROR_IO_ERROR,
                                       getSink() );
        }
    }

    private void sendSessionStatus( final Session session )
    {
        final AckRequestEvent ack =
            new AckRequestEvent( session,
                                 session.getLastPacketProcessed() );
        getSink().addEvent( ack );
        //TODO: Send out some nacks
    }

    private boolean canOutput( final ChannelTransport transport )
    {
        return null == transport || transport.getOutputStream().isClosed();
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
        if( null != session &&
            Session.STATUS_DISCONNECTED != session.getStatus() )
        {
            closeTransport( transport );
            final PeriodicTimeTrigger trigger =
                new PeriodicTimeTrigger( TIMEOUT_IN_MILLIS, -1 );
            final SchedulingKey key =
                _timeEventSource.addTrigger( trigger, session );
            session.setTimeoutKey( key );
            _target.addEvent( new SessionInactiveEvent( session ) );
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
            final int status = session.getStatus();
            if( Session.STATUS_DISCONNECTED == status )
            {
                signalDisconnectTransport( transport,
                                           Protocol.ERROR_SESSION_DISCONNECTED,
                                           getSink() );
                return;
            }
            session.setTransport( transport );
            if( session.isClient() )
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
    }

    /**
     * Handle disconenect event.
     */
    private void handleDisconnect( final ChannelTransport transport,
                                   final byte reason )
    {
        try
        {
            sendDisconnect( transport, reason );
        }
        catch( final IOException ioe )
        {
        }
    }

    private void closeTransport( final ChannelTransport transport )
    {
        transport.getOutputStream().close();
        final Session session = (Session)transport.getUserData();
        if( null != session )
        {
            session.setTransport( null );
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
        if( canOutput( transport ) )
        {
            return;
        }
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
            ioe.printStackTrace( System.out );
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

        input.mark( Protocol.MAX_MESSAGE_HEADER );

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
            System.out.println( "PACK msg = " + msg );
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
                _target.addEvent( new SessionConnectEvent( session ) );
                sendConnect( transport, session );
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
                    sendConnect( transport, session );
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
     * Send a "connect" message on transport.
     *
     * @param transport the transport
     * @param session the associated session
     * @throws IOException if error reading from stream
     */
    private void sendConnect( final ChannelTransport transport,
                              final Session session )
        throws IOException
    {
        session.setTransport( transport );
        sendConnect( transport,
                     session.getSessionID(),
                     session.getAuthID() );
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
            if( sessionID != session.getSessionID() )
            {
                // To make sure clients get connect event when
                // sessionID is known. 
                _target.addEvent( new SessionConnectEvent( session ) );
            }

            session.setStatus( Session.STATUS_ESTABLISHED );
            session.setSessionID( sessionID );
            session.setAuthID( authID );
            getSink().addEvent( new SessionEstablishRequestEvent( session ) );
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
        _target.addEvent( new SessionActiveEvent( session ) );
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
            final Session session = (Session)transport.getUserData();
            if( null != session && Protocol.ERROR_NONE == reason )
            {
                session.setPendingDisconnect();
                disconnectSession( session );
            }
            closeTransport( transport );
            return false;
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
        final Session session = (Session)transport.getUserData();
        session.getMessageQueue().addPacket( packet );
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
            final ByteBuffer buffer = _bufferManager.aquireBuffer( length );
            for( int i = 0; i < length; i++ )
            {
                final int data = input.read();
                buffer.put( (byte)data );
            }

            buffer.flip();
            final Session session = (Session)transport.getUserData();
            final Packet packet = new Packet( sequence, 0, buffer );
            getSink().addEvent( new PacketReadEvent( session, packet ) );
            return true;
        }
    }

    /**
     * Handle event indicating a packet has been read.
     *
     * @param session the session
     * @param packet the packet
     */
    private void handlePacketReadEvent( final Session session,
                                        final Packet packet )
    {
        if( session.isPendingDisconnect() )
        {
            return;
        }

        final PacketQueue queue = session.getMessageQueue();

        Packet candidate = queue.peek();
        short processed = session.getLastPacketProcessed();

        while( null != candidate &&
               Protocol.isNextInSequence( candidate.getSequence(),
                                          processed ) )
        {
            final DataPacketReadyEvent response =
                new DataPacketReadyEvent( session, packet );
            _target.addEvent( response );
            processed++;
            session.setLastPacketProcessed( processed );
            candidate = queue.pop();
        }

        sendSessionStatus( session );
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
        transport.getInputStream().close();
    }
}



