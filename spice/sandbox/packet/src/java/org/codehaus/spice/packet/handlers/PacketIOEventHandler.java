package org.codehaus.spice.packet.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.List;
import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.buffers.BufferManager;
import org.codehaus.spice.netevent.events.AbstractTransportEvent;
import org.codehaus.spice.netevent.events.ChannelClosedEvent;
import org.codehaus.spice.netevent.events.ConnectErrorEvent;
import org.codehaus.spice.netevent.events.ConnectEvent;
import org.codehaus.spice.netevent.events.InputDataPresentEvent;
import org.codehaus.spice.netevent.handlers.AbstractDirectedHandler;
import org.codehaus.spice.netevent.source.SelectableChannelEventSource;
import org.codehaus.spice.netevent.transport.ChannelTransport;
import org.codehaus.spice.netevent.transport.MultiBufferInputStream;
import org.codehaus.spice.netevent.transport.TransportOutputStream;
import org.codehaus.spice.packet.Packet;
import org.codehaus.spice.packet.events.AbstractSessionEvent;
import org.codehaus.spice.packet.events.ConnectRequestEvent;
import org.codehaus.spice.packet.events.DataPacketReadyEvent;
import org.codehaus.spice.packet.events.PacketWriteRequestEvent;
import org.codehaus.spice.packet.events.PingRequestEvent;
import org.codehaus.spice.packet.events.SessionActiveEvent;
import org.codehaus.spice.packet.events.SessionConnectEvent;
import org.codehaus.spice.packet.events.SessionDisconnectEvent;
import org.codehaus.spice.packet.events.SessionDisconnectRequestEvent;
import org.codehaus.spice.packet.events.SessionErrorEvent;
import org.codehaus.spice.packet.events.SessionInactiveEvent;
import org.codehaus.spice.packet.session.PacketQueue;
import org.codehaus.spice.packet.session.Session;
import org.codehaus.spice.packet.session.SessionManager;
import org.codehaus.spice.timeevent.events.TimeEvent;
import org.codehaus.spice.timeevent.source.SchedulingKey;
import org.codehaus.spice.timeevent.source.TimeEventSource;
import org.codehaus.spice.timeevent.triggers.PeriodicTimeTrigger;

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-03-26 02:23:02 $
 */
public class PacketIOEventHandler
    extends AbstractDirectedHandler
{
    /**
     * Flag that set to true when should debug messages.
     */
    private static final boolean DEBUG = System.getProperty( "packet.debug",
                                                             "false" ).equals(
                                                                 "true" );

    /**
     * The maximum amount of time that session can be idle without an attempt to
     * send write.
     */
    private static final int MAX_IDLE_TIME = 20 * 1000; //50 * 1000;

    /**
     * The number of milliseconds before the session will timeout.
     */
    private static final int TIMEOUT_IN_MILLIS = 2 * 60 * 1000;

    /**
     * The event source to register channels into.
     */
    private final SelectableChannelEventSource _channelEventSource;

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
                                 final SelectableChannelEventSource ces,
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
        _channelEventSource = ces;
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
        if( event instanceof TimeEvent )
        {
            final TimeEvent e = (TimeEvent) event;
            handleTimeout( e );
        }
        else if( event instanceof ConnectErrorEvent )
        {
            final ConnectErrorEvent ce = (ConnectErrorEvent) event;
            handleConnectFailed( ce );
        }
        else if( event instanceof ChannelClosedEvent )
        {
            final ChannelClosedEvent cc = (ChannelClosedEvent) event;
            handleClose( cc );
        }
        else
        {
            final ChannelTransport transport;
            if( event instanceof AbstractTransportEvent )
            {
                final AbstractTransportEvent e = (AbstractTransportEvent) event;
                transport = e.getTransport();
            }
            else
            {
                final AbstractSessionEvent e = (AbstractSessionEvent) event;
                transport = e.getSession().getTransport();
            }
            try
            {
                if( event instanceof InputDataPresentEvent )
                {
                    final InputDataPresentEvent ie = (InputDataPresentEvent) event;
                    handleInput( ie );
                }
                else if( event instanceof ConnectRequestEvent )
                {
                    final ConnectRequestEvent cre = (ConnectRequestEvent) event;
                    startConnect( cre.getSession() );
                }
                else if( event instanceof PingRequestEvent )
                {
                    final PingRequestEvent pre = (PingRequestEvent) event;
                    sendPing( pre.getSession() );
                }
                else if( event instanceof ConnectEvent )
                {
                    final ConnectEvent ce = (ConnectEvent) event;
                    handleConnect( ce );
                }
                else if( event instanceof SessionDisconnectRequestEvent )
                {
                    final SessionDisconnectRequestEvent e = (SessionDisconnectRequestEvent) event;
                    handleSessionDisconnectRequest( e.getSession() );
                }
                else if( event instanceof PacketWriteRequestEvent )
                {
                    final PacketWriteRequestEvent e = (PacketWriteRequestEvent) event;
                    handleOutput( e );
                }
            }
            catch( final IOException ioe )
            {
                ioe.printStackTrace( System.out );
                closeTransport( transport );
            }
        }
    }

    private void startConnect( final Session session ) throws IOException
    {
        //Make sure session is disconnected
        session.setTransport( null );
        final SocketChannel channel = SocketChannel.open();
        _channelEventSource.registerChannel( channel,
                                             SelectionKey.OP_CONNECT,
                                             session );
        channel.socket().setSoLinger( true, 2 );
        channel.connect( session.getAddress() );
        if( !session.isConnecting() )
        {
            session.setConnecting( true );
        }
    }

    private void handleNack( final Session session, final short sequence )
    {
        if( isDebugEnabled() )
        {
            debug( session, "RECEIVED NACK " + sequence );
        }
        final Packet packet = session.getTransmitQueue().getPacket( sequence );
        if( null == packet )
        {
            signalDisconnectTransport( session.getTransport(),
                                       SessionErrorEvent.ERROR_BAD_NACK );
        }
        else
        {
            final PacketWriteRequestEvent response = new PacketWriteRequestEvent(
                session, packet );
            getSink().addEvent( response );
        }
    }

    private boolean isDebugEnabled()
    {
        return DEBUG;
    }

    /**
     * Handle timeout of conenction.
     *
     * @param e the event
     */
    private void handleTimeout( final TimeEvent e )
    {
        final SchedulingKey key = e.getKey();
        final TimerEntry timer = (TimerEntry) key.getUserData();
        final Session session = timer.getSession();
        final int status = session.getStatus();
        if( Session.STATUS_NOT_CONNECTED == status && !session.isClient() )
        {
            session.setError();
            final SessionErrorEvent error = new SessionErrorEvent( session,
                                                                   SessionErrorEvent.ERROR_SESSION_TIMEOUT );
            _target.addEvent( error );
            disconnectSession( session );
        }
        else if( Session.STATUS_CONNECTED == status )
        {
            session.cancelTimeout();
            closeTransport( session.getTransport() );
        }
        else if( Session.STATUS_ESTABLISHED == status )
        {
            final ChannelTransport transport = session.getTransport();
            transport.reregister();
            final long lastNetTime = Math.max( transport.getLastRxTime(),
                                               transport.getLastTxTime() );
            final long now = System.currentTimeMillis();
            if( lastNetTime + MAX_IDLE_TIME < now &&
                session.getLastPingTime() < lastNetTime )
            {
                session.ping();
            }
        }
    }

    /**
     * Handle message to disconnect Session gracefully.
     *
     * @param session the session
     */
    private void handleSessionDisconnectRequest( final Session session )
        throws IOException
    {
        if( isDebugEnabled() )
        {
            debug( session, "DISCONNECT REQUESTED BY USER" );
        }
        session.setDisconnectRequested();
        final ChannelTransport transport = session.getTransport();
        if( canOutput( transport ) )
        {
            sendSessionStatus( session );
            sendPing( session );
            sendDisconnectRequest( transport );
        }
    }

    private void sendDisconnectRequest( final ChannelTransport transport )
        throws IOException
    {
        final TransportOutputStream output = transport.getOutputStream();
        output.write( MessageCodes.DISCONNECT_REQUEST );
        output.flush();
    }

    private void disconnectSession( final Session session )
    {
        final int status = session.getStatus();
        if( Session.STATUS_DISCONNECTED == status )
        {
            return;
        }
        if( Session.STATUS_ESTABLISHED == status )
        {
            _target.addEvent( new SessionInactiveEvent( session ) );
        }
        session.setStatus( Session.STATUS_DISCONNECTED );
        _sessionManager.deleteSession( session );
        _target.addEvent( new SessionDisconnectEvent( session ) );
    }

    private void makeActive( final Session session ) throws IOException
    {
        session.setStatus( Session.STATUS_ESTABLISHED );
        sendSessionStatus( session );
        sendPing( session );
        if( session.isDisconnectRequested() )
        {
            sendDisconnectRequest( session.getTransport() );
        }
        final PeriodicTimeTrigger trigger =
           new PeriodicTimeTrigger( MAX_IDLE_TIME, MAX_IDLE_TIME );
        final TimerEntry timer = new TimerEntry( "PINGER", session );
        final SchedulingKey key =
           _timeEventSource.addTrigger( trigger, timer );
        session.setTimeoutKey( key );
        session.setConnecting( false );
        _target.addEvent( new SessionActiveEvent( session ) );
    }

    private void sendSessionStatus( final Session session ) throws IOException
    {
        if( !canOutput( session.getTransport() ) )
        {
            return;
        }

        sendAck( session );

        sendNacks( session );
    }

    private void sendPing( final Session session ) throws IOException
    {
        final ChannelTransport transport = session.getTransport();
        if( canOutput( transport ) && session.hasTransmittedData() )
        {
            final short sequence = session.getLastPacketTransmitted();
            session.setLastPingTime( System.currentTimeMillis() );
            ensureValidSession( transport );
            if( canOutput( transport ) )
            {
                if( isDebugEnabled() )
                {
                    debug( session, "SEND PING " + sequence );
                }
                final TransportOutputStream output = transport.getOutputStream();
                output.write( MessageCodes.PING );
                TypeIOUtil.writeShort( output, sequence );
                output.flush();
            }
        }
    }

    private void sendAck( final Session session ) throws IOException
    {
        final ChannelTransport transport = session.getTransport();
        if( session.hasReceivedData() && session.needsToSendAck() )
        {
            final short processed = session.getLastPacketProcessed();
            session.acked();
            if( isDebugEnabled() )
            {
                debug( session, "SEND ACK " + processed );
            }
            sendAck( transport, processed );
            checkDisconnect( session, transport );
        }
    }

    private void sendNacks( final Session session ) throws IOException
    {
        final ChannelTransport transport = session.getTransport();
        final short processed = session.getLastPacketProcessed();
        final PacketQueue queue = session.getReceiveQueue();
        final short expected = session.getLastPacketExpected();
        final short initial = (short) ( processed + 1 );
        for( short i = initial; i <= expected; i++ )
        {
            final Packet packet = queue.getPacket( i );
            if( null == packet )
            {
                if( isDebugEnabled() )
                {
                    debug( session, "SEND NACK " + i );
                }
                sendNack( transport, i );
            }
        }
    }

    private boolean canOutput( final ChannelTransport transport )
    {
        return !( null == transport || transport.getOutputStream().isClosed() );
    }

    /**
     * Handle connect failure event.
     *
     * @param ce the connect failure event
     */
    private void handleConnectFailed( final ConnectErrorEvent ce )
    {
        final ChannelTransport transport = ce.getTransport();
       final Session session;
       try
       {
          session = (Session) transport.getUserData();
       if( null != session )
        {
            session.setTransport( null );
            setupInactivityTimout( session );
        }
          }
       catch( final ClassCastException e )
       {
          final Object object = transport.getUserData();
          if( null != object )
          {
             System.out.println( "object.getClass() = " + object.getClass() );
          }
          e.printStackTrace();

       }
    }

    private void setupInactivityTimout( final Session session )
    {
        session.cancelTimeout();
        final PeriodicTimeTrigger trigger = new PeriodicTimeTrigger(
            TIMEOUT_IN_MILLIS, -1 );
        final TimerEntry timer = new TimerEntry( "INACTIVE TIMEOUT", session );
        final SchedulingKey key = _timeEventSource.addTrigger( trigger,
                                                               timer );
        session.setTimeoutKey( key );
    }

    /**
     * Handle close of transport event.
     *
     * @param cc the close event
     */
    private void handleClose( final ChannelClosedEvent cc )
    {
        final ChannelTransport transport = cc.getTransport();
        final Session session = (Session) transport.getUserData();

        if( null != session && session.isPendingDisconnect() )
        {
            disconnectSession( session );
        }

        closeTransport( transport );

        if( isDebugEnabled() )
        {
            debug( transport, "Transport Closed. Session=" + session );
        }

        if( null != session &&
            Session.STATUS_DISCONNECTED != session.getStatus() )
        {
            setupInactivityTimout( session );
            _target.addEvent( new SessionInactiveEvent( session ) );
        }
    }

    /**
     * Handle initial greeting event event.
     *
     * @param e the event
     */
    private void handleConnect( final ConnectEvent e ) throws IOException
    {
        final ChannelTransport transport = e.getTransport();
        final Session session = (Session) transport.getUserData();
        if( null != session && session.isClient() )
        {
            final int status = session.getStatus();
            if( Session.STATUS_DISCONNECTED == status || session.isError() )
            {
                signalDisconnectTransport( transport,
                                           SessionErrorEvent.ERROR_SESSION_DISCONNECTED );
                return;
            }
            session.setTransport( transport );
            sendGreeting( transport,
                          session.getSessionID(),
                          session.getAuthID() );
            setupInactivityTimout( session );
        }
    }

    private void closeTransport( final ChannelTransport transport )
    {
        if( null == transport )
        {
            return;
        }
        transport.getOutputStream().close();
        final Session session = (Session) transport.getUserData();
        if( null != session )
        {
            session.setTransport( null );
        }
    }

    /**
     * Handle event relating to debug.
     *
     * @param e the debug event
     */
    private void handleOutput( final PacketWriteRequestEvent e )
        throws IOException
    {
        final Session session = e.getSession();
        final ChannelTransport transport = session.getTransport();
        final Packet packet = e.getPacket();
        if( !canOutput( transport ) )
        {
            if( isDebugEnabled() )
            {
                debug( session, "QUEUED " + packet.getSequence() );
            }
            return;
        }
        else
        {
            final PacketQueue transmitQueue = session.getTransmitQueue();
            if( null != transmitQueue.getPacket( packet.getSequence() ) )
            {
                sendData( transport, packet );
            }
            else
            {
                if( isDebugEnabled() )
                {
                    debug( session, "DROPPING Packet Write Request " +
                                    "as already acked: " + packet.getSequence() );
                }
            }
        }
    }

    /**
     * Handle input from transport.
     *
     * @param ie the event indicating data present
     */
    private void handleInput( final InputDataPresentEvent ie )
        throws IOException
    {
        final ChannelTransport transport = ie.getTransport();
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
        if( available < TypeIOUtil.SIZEOF_BYTE )
        {
            return false;
        }

        input.mark( Protocol.MAX_MESSAGE_HEADER );

        final Session session = (Session) transport.getUserData();
        final int msg = input.read();
        if( isDebugEnabled() )
        {
            debug( session, "msg = " + msg + " available=" + available );
        }
        if( !session.isClient() && MessageCodes.ESTABLISHED == msg &&
            Session.STATUS_CONNECTED == session.getStatus() )
        {
            return receiveEstablish( transport );
        }
        else if( session.isClient() && MessageCodes.CONNECT == msg &&
                 Session.STATUS_CONNECTED == session.getStatus() )
        {
            return receiveConnect( transport );
        }
        else if( MessageCodes.ERROR == msg )
        {
            return receiveError( transport );
        }
        else if( MessageCodes.DISCONNECT_REQUEST == msg )
        {
            return receiveDisconnectRequest( transport );
        }
        else if( MessageCodes.DISCONNECT == msg )
        {
            return receiveDisconnect( transport );
        }
        else if( MessageCodes.DATA == msg )
        {
            return receiveData( transport );
        }
        else if( MessageCodes.PING == msg )
        {
            return receivePing( transport );
        }
        else if( MessageCodes.ACK == msg )
        {
            return receiveAck( transport );
        }
        else if( MessageCodes.NACK == msg )
        {
            return receiveNack( transport );
        }
        else
        {
            debug( session, "UNKNOWN MESSAGE " + msg );
            signalDisconnectTransport( transport,
                                       SessionErrorEvent.ERROR_BAD_MESSAGE );
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
                               final short authID ) throws IOException
    {
        if( isDebugEnabled() )
        {
            debug( transport, "SEND GREETING" );
        }
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
            if( isDebugEnabled() )
            {
                debug( transport, "RECEIVE GREETING" );
            }
            if( !checkMagicNumber( input ) )
            {
                signalDisconnectTransport( transport,
                                           SessionErrorEvent.ERROR_BAD_MAGIC );
                return false;
            }

            final long sessionID = TypeIOUtil.readLong( input );
            final short authID = TypeIOUtil.readShort( input );
            if( -1 == sessionID )
            {
                final Session session = _sessionManager.newSession();
                session.setSink( getSink() );
                sendConnect( transport, session );
                _target.addEvent( new SessionConnectEvent( session ) );
                return true;
            }
            else
            {
                final Session session = _sessionManager.findSession( sessionID );
                if( null == session )
                {
                    signalDisconnectTransport( transport,
                                               SessionErrorEvent.ERROR_BAD_SESSION );
                    return false;
                }
                else if( session.getAuthID() != authID )
                {
                    signalDisconnectTransport( transport,
                                               SessionErrorEvent.ERROR_BAD_AUTH );
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
     * @param session   the associated session
     * @throws IOException if error reading from stream
     */
    private void sendConnect( final ChannelTransport transport,
                              final Session session ) throws IOException
    {
        session.setTransport( transport );
        sendConnect( transport, session.getSessionID(), session.getAuthID() );
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
                              final short authID ) throws IOException
    {
        if( isDebugEnabled() )
        {
            debug( transport, "SEND CONNECT" );
        }
        final OutputStream output = transport.getOutputStream();
        output.write( MessageCodes.CONNECT );
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
        final Session session = (Session) transport.getUserData();
        final InputStream input = transport.getInputStream();
        final int available = input.available();
        if( available < TypeIOUtil.SIZEOF_LONG + TypeIOUtil.SIZEOF_SHORT )
        {
            input.reset();
            return false;
        }
        else
        {
            if( isDebugEnabled() )
            {
                debug( session, "RECEIVE CONNECT" );
            }
            final long sessionID = TypeIOUtil.readLong( input );
            final short authID = TypeIOUtil.readShort( input );
            if( sessionID != session.getSessionID() )
            {
                // To make sure clients get connect event when
                // sessionID is known.
                session.setSink( getSink() );
                _target.addEvent( new SessionConnectEvent( session ) );
            }

            session.setSessionID( sessionID );
            session.setAuthID( authID );
            sendEstablished( transport );
            makeActive( session );
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
        ensureSessionPresent( transport );
        if( isDebugEnabled() )
        {
            debug( transport, "SEND ESTABLISH" );
        }
        final TransportOutputStream output = transport.getOutputStream();
        output.write( MessageCodes.ESTABLISHED );
        output.flush();
    }

    /**
     * Receive an establish message.
     *
     * @param transport the transport
     */
    private boolean receiveEstablish( final ChannelTransport transport )
        throws IOException
    {
        final Session session = (Session) transport.getUserData();
        if( isDebugEnabled() )
        {
            debug( session, "RECEIVE ESTABLISH" );
        }
        makeActive( session );
        return true;
    }

    /**
     * Send an ack message.
     *
     * @param transport the transport
     * @throws IOException if io error occurs
     */
    private void sendAck( final ChannelTransport transport,
                          final short sequence ) throws IOException
    {
        ensureValidSession( transport );
        if( canOutput( transport ) )
        {
            final TransportOutputStream output = transport.getOutputStream();
            output.write( MessageCodes.ACK );
            TypeIOUtil.writeShort( output, sequence );
            output.flush();
        }
    }

    /**
     * Receive an ack message.
     *
     * @param transport the transport
     * @throws IOException if io error occurs
     */
    private boolean receivePing( final ChannelTransport transport )
        throws IOException
    {
        ensureValidSession( transport );
        final Session session = (Session) transport.getUserData();
        final InputStream input = transport.getInputStream();
        final int available = input.available();
        if( available < TypeIOUtil.SIZEOF_SHORT )
        {
            input.reset();
            return false;
        }
        else
        {
            final short sequence = TypeIOUtil.readShort( input );
            if( isDebugEnabled() )
            {
                debug( session, "RECEIVE PING " + sequence );
            }
            session.setLastPacketExpected( sequence );
            sendNacks( session );
            checkDisconnect( session, transport );
            return true;
        }
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
        final Session session = (Session) transport.getUserData();
        final InputStream input = transport.getInputStream();
        final int available = input.available();
        if( available < TypeIOUtil.SIZEOF_SHORT )
        {
            input.reset();
            return false;
        }
        else
        {
            final short sequence = TypeIOUtil.readShort( input );
            if( isDebugEnabled() )
            {
                debug( session, "RECEIVE ACK " + sequence );
            }
            final List acked = session.getTransmitQueue().ack( sequence );

            final int count = acked.size();
            for( int i = 0; i < count; i++ )
            {
                final Packet packet = (Packet) acked.get( i );
                _bufferManager.releaseBuffer( packet.getData() );
            }

            checkDisconnect( session, transport );
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
                           final short sequence ) throws IOException
    {
        ensureValidSession( transport );
        final TransportOutputStream output = transport.getOutputStream();
        output.write( MessageCodes.NACK );
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
        final Session session = (Session) transport.getUserData();
        final InputStream input = transport.getInputStream();
        final int available = input.available();
        if( available < TypeIOUtil.SIZEOF_SHORT )
        {
            input.reset();
            return false;
        }
        else
        {
            final short sequence = TypeIOUtil.readShort( input );
            handleNack( session, sequence );
            return true;
        }
    }

    /**
     * Receive a disconnect message.
     *
     * @param transport the transport
     * @throws IOException if io error occurs
     */
    private void sendDisconnect( final ChannelTransport transport )
        throws IOException
    {
        if( isDebugEnabled() )
        {
            debug( transport, "SEND DISCONNECT" );
        }
        final TransportOutputStream output = transport.getOutputStream();
        output.write( MessageCodes.DISCONNECT );
        output.flush();
    }

    /**
     * Receive a disconnect message.
     *
     * @param transport the transport
     * @return if message received
     * @throws IOException if io error occurs
     */
    private boolean receiveDisconnectRequest( final ChannelTransport transport )
        throws IOException
    {
        ensureValidSession( transport );
        final Session session = (Session) transport.getUserData();
        if( isDebugEnabled() )
        {
            debug( session, "RECEIVED DISCONNECT REQUEST" );
        }
        if( !session.isInShutdown() || session.isClient() )
        {
            session.setPendingDisconnect();
        }
        sendSessionStatus( session );

        checkDisconnect( session, transport );
        return true;
    }

    private void checkDisconnect( final Session session,
                                  final ChannelTransport transport )
        throws IOException
    {
        if( session.isPendingDisconnect() && completionPossible( session ) )
        {
            sendDisconnect( transport );
        }
    }

    private boolean completionPossible( final Session session )
    {
        final int txQueueSize = session.getTransmitQueue().size();
        final int rxQueueSize = session.getReceiveQueue().size();
        return 0 == rxQueueSize && 0 == txQueueSize && session.getLastPacketExpected() <=
                                                       session.getLastPacketProcessed();
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
        ensureValidSession( transport );
        final Session session = (Session) transport.getUserData();
        if( isDebugEnabled() )
        {
            debug( session, "RECEIVED DISCONNECT" );
        }
        closeTransport( session.getTransport() );
        disconnectSession( session );
        return false;
    }

    /**
     * Receive a disconnect message.
     *
     * @param transport the transport
     * @return if message received
     * @throws IOException if io error occurs
     */
    private boolean receiveError( final ChannelTransport transport )
        throws IOException
    {
        final InputStream input = transport.getInputStream();
        final int available = input.available();
        if( available < TypeIOUtil.SIZEOF_BYTE )
        {
            input.reset();
            return false;
        }
        else
        {
            final byte reason = (byte) input.read();

            final Session session = (Session) transport.getUserData();
            if( null != session && isDebugEnabled() )
            {
                debug( session, "Received Error=" + reason );
            }

            signalDisconnectTransport( transport, reason );
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
                           final Packet packet ) throws IOException
    {
        final Session session = (Session) transport.getUserData();
        if( !canOutput( transport ) ||
            Session.STATUS_ESTABLISHED != session.getStatus() )
        {
            return;
        }

        final ByteBuffer data = packet.getData();
        if( isDebugEnabled() )
        {
            debug( session, "SEND DATA " + packet.getSequence() + "(" + data.limit() + ") @ " +
                            transport.getTxByteCount() );
        }

        final TransportOutputStream output = transport.getOutputStream();
        output.write( MessageCodes.DATA );
        TypeIOUtil.writeShort( output, packet.getSequence() );

        final int dataSize = data.limit();
        TypeIOUtil.writeInteger( output, dataSize );
        data.position( 0 );
        while( data.remaining() > 0 )
        {
            output.write( data.get() );
        }
        output.flush();
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
        if( available < TypeIOUtil.SIZEOF_SHORT + TypeIOUtil.SIZEOF_INTEGER )
        {
            input.reset();
            return false;
        }

        final short sequence = TypeIOUtil.readShort( input );
        final int length = TypeIOUtil.readInteger( input );
        final Session session = (Session) transport.getUserData();
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
                buffer.put( (byte) data );
            }

            buffer.flip();

            if( isDebugEnabled() )
            {

                debug( (Session) transport.getUserData(), "RECEIVED DATA " +
                                                          sequence + "(" + buffer.limit() +
                                                          ") @ " + transport.getRxByteCount() + "/" + ( available -
                                                                                                        1 ) );
            }

            final Packet packet = new Packet( sequence, 0, buffer );
            handlePacketReadEvent( session, packet );
            return true;
        }
    }

    /**
     * Handle event indicating a packet has been read.
     *
     * @param session the session
     * @param packet  the packet
     */
    private void handlePacketReadEvent( final Session session,
                                        final Packet packet )
        throws IOException
    {
        final short sequence = packet.getSequence();
        if( sequence <= session.getLastPacketProcessed() )
        {
            //Discarding packet as it has already been processed on this side
            if( isDebugEnabled() )
            {
                debug( session, "DISCARDING DUPLICATE " + sequence );
            }
            return;
        }

        final PacketQueue queue = session.getReceiveQueue();

        queue.addPacket( packet );

        Packet candidate = queue.peek();
        short processed = session.getLastPacketProcessed();

        while( null != candidate && Protocol.isNextInSequence(
            candidate.getSequence(), processed ) )
        {
            queue.pop();
            final DataPacketReadyEvent response = new DataPacketReadyEvent(
                session, candidate );
            _target.addEvent( response );
            processed++;
            session.setLastPacketProcessed( processed );
            candidate = queue.peek();
        }

        session.setLastPacketReceived( sequence );

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
        ensureSessionPresent( transport );
        final Session session = (Session) transport.getUserData();
        if( Session.STATUS_ESTABLISHED != session.getStatus() )
        {
            throw new IOException( "Session not established. " + session );
        }
    }

    private void ensureSessionPresent( final ChannelTransport transport )
        throws IOException
    {
        final Session session = (Session) transport.getUserData();
        if( null == session )
        {
            throw new IOException( "No session specififed." );
        }
    }

    /**
     * Send event indicating that transport should be disconnected.
     *
     * @param transport the transport
     * @param reason    the reason for disconnect
     */
    private void signalDisconnectTransport( final ChannelTransport transport,
                                            final byte reason )
    {
        final Session session = (Session) transport.getUserData();
        if( null != session )
        {
            final SessionErrorEvent error = new SessionErrorEvent( session,
                                                                   reason );
            _target.addEvent( error );
            if( isDebugEnabled() )
            {
                debug( session, "Signalling Error=" + reason );
            }
            disconnectSession( session );
        }
        else
        {
            if( isDebugEnabled() )
            {
                final String message = "Transport Error=" + reason;
                debug( transport, message );
            }
        }

        try
        {
            final TransportOutputStream output = transport.getOutputStream();
            output.write( MessageCodes.ERROR );
            output.write( reason );
        }
        catch( final IOException ioe )
        {
        }
        transport.getInputStream().close();
    }

    private void debug( final Session session, final String text )
    {
        if( isDebugEnabled() )
        {
            final String message = ( session.isClient() ?
                                     "PACK CL" :
                                     "PACK SV" ) +
                                   " (" + session.getSessionID() + "): " + text + " -- " +
                                   session.getUserData();
            System.out.println( message );
        }
    }

    private void debug( final ChannelTransport transport, final String text )
    {
        if( isDebugEnabled() )
        {
            final Session session = (Session) transport.getUserData();
            if( null != session )
            {
                debug( session, text );
            }
            else
            {
                final String message = text + " -- " + transport;
                System.out.println( message );
            }
        }
    }
}
