package org.realityforge.packet.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
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
import org.codehaus.spice.netevent.transport.ChannelTransport;
import org.codehaus.spice.netevent.transport.MultiBufferInputStream;
import org.codehaus.spice.netevent.transport.TransportOutputStream;
import org.codehaus.spice.timeevent.events.TimeEvent;
import org.codehaus.spice.timeevent.source.SchedulingKey;
import org.codehaus.spice.timeevent.source.TimeEventSource;
import org.codehaus.spice.timeevent.triggers.PeriodicTimeTrigger;
import org.realityforge.packet.Packet;
import org.realityforge.packet.events.AbstractSessionEvent;
import org.realityforge.packet.events.DataPacketReadyEvent;
import org.realityforge.packet.events.PacketWriteRequestEvent;
import org.realityforge.packet.events.PingRequestEvent;
import org.realityforge.packet.events.SessionActiveEvent;
import org.realityforge.packet.events.SessionConnectEvent;
import org.realityforge.packet.events.SessionDisconnectEvent;
import org.realityforge.packet.events.SessionDisconnectRequestEvent;
import org.realityforge.packet.events.SessionInactiveEvent;
import org.realityforge.packet.session.PacketQueue;
import org.realityforge.packet.session.Session;
import org.realityforge.packet.session.SessionManager;

/**
 * @author Peter Donald
 * @version $Revision: 1.25 $ $Date: 2004-02-13 04:41:51 $
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
      if( event instanceof TimeEvent )
      {
         final TimeEvent e = (TimeEvent)event;
         handleTimeout( e );
      }
      else if( event instanceof ConnectErrorEvent )
      {
         final ConnectErrorEvent ce = (ConnectErrorEvent)event;
         handleConnectFailed( ce );
      }
      else if( event instanceof ChannelClosedEvent )
      {
         final ChannelClosedEvent cc = (ChannelClosedEvent)event;
         handleClose( cc );
      }
      else
      {
         final ChannelTransport transport;
         if( event instanceof AbstractTransportEvent )
         {
            final AbstractTransportEvent e = (AbstractTransportEvent)event;
            transport = e.getTransport();
         }
         else
         {
            final AbstractSessionEvent e = (AbstractSessionEvent)event;
            transport = e.getSession().getTransport();
         }
         try
         {
            if( event instanceof InputDataPresentEvent )
            {
               final InputDataPresentEvent ie = (InputDataPresentEvent)event;
               handleInput( ie );
            }
            else if( event instanceof PingRequestEvent )
            {
               final PingRequestEvent pre = (PingRequestEvent)event;
               sendPing( pre.getSession() );
            }
            else if( event instanceof ConnectEvent )
            {
               final ConnectEvent ce = (ConnectEvent)event;
               handleGreeting( ce );
            }
            else if( event instanceof SessionDisconnectRequestEvent )
            {
               final SessionDisconnectRequestEvent e =
                  (SessionDisconnectRequestEvent)event;
               handleSessionDisconnectRequest( e.getSession() );
            }
            else if( event instanceof PacketWriteRequestEvent )
            {
               final PacketWriteRequestEvent e =
                  (PacketWriteRequestEvent)event;
               handleOutput( e );
            }
            else
            {
               signalDisconnectTransport( transport,
                                          Protocol.ERROR_BAD_MESSAGE );
            }
         }
         catch( final IOException ioe )
         {
            ioe.printStackTrace( System.out );
            signalDisconnectTransport( transport,
                                       Protocol.ERROR_IO_ERROR );
         }
      }
   }

   private void handleNack( final Session session,
                            final short sequence )
   {
      //output( session, "RECEIVED NACK " + sequence );
      final Packet packet =
         session.getTransmitQueue().getPacket( sequence );
      if( null == packet )
      {
         signalDisconnectTransport( session.getTransport(),
                                    Protocol.ERROR_BAD_NACK );
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
      final Session session = (Session)key.getUserData();
      session.cancelTimeout();
      final int status = session.getStatus();
      if( Session.STATUS_LOST == status )
      {
         session.requestShutdown();
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
      session.setDisconnectRequested();
      final ChannelTransport transport = session.getTransport();
      if( !canOutput( transport ) )
      {
         return;
      }

      sendSessionStatus( session );
      final TransportOutputStream output = transport.getOutputStream();
      output.write( MessageCodes.DISCONNECT_REQUEST );
      output.flush();
   }

   private void disconnectSession( final Session session )
   {
      if( Session.STATUS_ESTABLISHED == session.getStatus() )
      {
         _target.addEvent( new SessionInactiveEvent( session ) );
      }
      session.setStatus( Session.STATUS_DISCONNECTED );
      _sessionManager.deleteSession( session );
      _target.addEvent( new SessionDisconnectEvent( session ) );
   }

   private void makeActive( final Session session )
      throws IOException
   {
      sendSessionStatus( session );
      sendPing( session );
      session.setConnecting( false );
      _target.addEvent( new SessionActiveEvent( session ) );
   }

   private void sendSessionStatus( final Session session )
      throws IOException
   {
      if( !canOutput( session.getTransport() ) )
      {
         return;
      }

      sendAck( session );

      sendNacks( session );
   }

   private void sendPing( final Session session )
      throws IOException
   {
      if( !session.hasTransmittedData() )
      {
         return;
      }
      final ChannelTransport transport = session.getTransport();
      if( session.hasTransmittedData() )
      {
         final short sequence = session.getLastPacketTransmitted();
         //output( session, "SEND PING " + sequence );
         ensureValidSession( transport );
         if( canOutput( transport ) )
         {
            final TransportOutputStream output = transport.getOutputStream();
            output.write( MessageCodes.PING );
            TypeIOUtil.writeShort( output, sequence );
            output.flush();
         }
      }
   }

   private void sendAck( final Session session )
      throws IOException
   {
      final ChannelTransport transport = session.getTransport();
      if( session.hasReceivedData() &&
          session.needsToSendAck() )
      {
         final short processed = session.getLastPacketProcessed();
         session.acked();
         //output( session, "SEND ACK " + processed );
         sendAck( transport, processed );
         checkDisconnect( session, transport );
      }
   }

   private void sendNacks( final Session session )
      throws IOException
   {
      final ChannelTransport transport = session.getTransport();
      final short processed = session.getLastPacketProcessed();
      final PacketQueue queue = session.getReceiveQueue();
      final short expected = session.getLastPacketExpected();
      final short initial = (short)(processed + 1);
      for( short i = initial; i <= expected; i++ )
      {
         final Packet packet = queue.getPacket( i );
         if( null == packet )
         {
            //output( session, "SEND NACK " + i );
            sendNack( transport, i );
         }
      }
   }

   private boolean canOutput( final ChannelTransport transport )
   {
      return !(null == transport ||
               transport.getOutputStream().isClosed());
   }

   /**
    * Handle connect failure event.
    *
    * @param ce the connect failure event
    */
   private void handleConnectFailed( final ConnectErrorEvent ce )
   {
      final ChannelTransport transport = ce.getTransport();
      final Session session = (Session)transport.getUserData();
      if( null != session )
      {
         session.setConnecting( false );
         session.setTransport( null );
         final PeriodicTimeTrigger trigger =
            new PeriodicTimeTrigger( TIMEOUT_IN_MILLIS, -1 );
         final SchedulingKey key =
            _timeEventSource.addTrigger( trigger, session );
         session.setTimeoutKey( key );
         session.setStatus( Session.STATUS_CONNECT_FAILED );
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

      if( null != session && session.isPendingDisconnect() )
      {
         disconnectSession( session );
      }

      closeTransport( transport );
      if( null != session &&
          Session.STATUS_DISCONNECTED != session.getStatus() )
      {
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
      throws IOException
   {
      final ChannelTransport transport = e.getTransport();
      final Session session = (Session)transport.getUserData();
      if( null == session )
      {
         return;
      }
      session.setConnecting( false );

      if( session.isClient() )
      {
         final int status = session.getStatus();
         if( Session.STATUS_DISCONNECTED == status )
         {
            signalDisconnectTransport( transport,
                                       Protocol.ERROR_SESSION_DISCONNECTED );
            return;
         }
         //TODO: Why doesn't this occur on server connection?
         session.setTransport( transport );
         sendGreeting( transport,
                       session.getSessionID(),
                       session.getAuthID() );
      }
   }

   private void closeTransport( final ChannelTransport transport )
   {
      if( null == transport )
      {
         return;
      }
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
    * @param e the output event
    */
   private void handleOutput( final PacketWriteRequestEvent e )
      throws IOException
   {
      final Session session = e.getSession();
      final ChannelTransport transport = session.getTransport();
      final Packet packet = e.getPacket();
      if( !canOutput( transport ) )
      {
         //output( session, "QUEUED " + packet.getSequence() );
         return;
      }
      else
      {
         final PacketQueue transmitQueue = session.getTransmitQueue();
         if( null != transmitQueue.getPacket( packet.getSequence() ) )
         {
            sendData( transport, packet );
         }
         /*
         else
         {
             output( session, "DROPPING Packet Write Request " +
                              "as already acked: " + packet.getSequence() );
         }
         */
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

      if( input.available() < Protocol.SIZEOF_BYTE )
      {
         return false;
      }

      input.mark( Protocol.MAX_MESSAGE_HEADER );

      final Session session = (Session)transport.getUserData();
      final int msg = input.read();
      if( !session.isClient() &&
          MessageCodes.ESTABLISHED == msg &&
          Session.STATUS_CONNECTED == session.getStatus() )
      {
         return receiveEstablish( transport );
      }
      else if( session.isClient() &&
               MessageCodes.CONNECT == msg &&
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
         output( session, "UNKNOWN MESSAGE " + msg );
         signalDisconnectTransport( transport,
                                    Protocol.ERROR_BAD_MESSAGE );
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
                                       Protocol.ERROR_BAD_MAGIC );
            return false;
         }

         final long sessionID = TypeIOUtil.readLong( input );
         final short authID = TypeIOUtil.readShort( input );
         if( -1 == sessionID )
         {
            final Session session = _sessionManager.newSession();
            session.setSink( getSink() );
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
                                          Protocol.ERROR_BAD_SESSION );
               return false;
            }
            else if( session.getAuthID() != authID )
            {
               signalDisconnectTransport( transport,
                                          Protocol.ERROR_BAD_AUTH );
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
            session.setSink( getSink() );
            _target.addEvent( new SessionConnectEvent( session ) );
         }

         session.setStatus( Session.STATUS_ESTABLISHED );
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
      ensureValidSession( transport );
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
      final Session session = (Session)transport.getUserData();
      session.setStatus( Session.STATUS_ESTABLISHED );
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
                         final short sequence )
      throws IOException
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
         //output( session, "PING " + sequence );
         session.setLastPacketExpected( sequence );
         sendNacks( session );
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
         //output( session, "RECEIVE ACK " + sequence );
         final List acked =
            session.getTransmitQueue().ack( sequence );

         final int count = acked.size();
         for( int i = 0; i < count; i++ )
         {
            final Packet packet = (Packet)acked.get( i );
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
                          final short sequence )
      throws IOException
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
      final Session session = (Session)transport.getUserData();
      if( !session.isDisconnectRequested() || !session.isClient() )
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
      return 0 == rxQueueSize &&
             0 == txQueueSize &&
             session.getLastPacketExpected() <=
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
      final Session session = (Session)transport.getUserData();
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
      if( available < Protocol.SIZEOF_BYTE )
      {
         input.reset();
         return false;
      }
      else
      {
         final byte reason = (byte)input.read();
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
                          final Packet packet )
      throws IOException
   {
      ensureValidSession( transport );
      //final Session session = (Session)transport.getUserData();
      //output( session, "SENT " + packet.getSequence() );

      final TransportOutputStream output = transport.getOutputStream();
      output.write( MessageCodes.DATA );
      TypeIOUtil.writeShort( output, packet.getSequence() );

      final ByteBuffer data = packet.getData();

      final int dataSize = data.limit();
      TypeIOUtil.writeShort( output, (short)dataSize );
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
      if( input.available() < Protocol.SIZEOF_SHORT + Protocol.SIZEOF_SHORT )
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

         //output( (Session)transport.getUserData(), "RECEIVED " + sequence );

         final Session session = (Session)transport.getUserData();
         final Packet packet = new Packet( sequence, 0, buffer );
         handlePacketReadEvent( session, packet );
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
      throws IOException
   {
      final short sequence = packet.getSequence();
      if( sequence <= session.getLastPacketProcessed() )
      {
         //Discarding packet as it has already been processed on this side
         //output( session, "DISCARDING DUPLICATE " + sequence );
         return;
      }

      final PacketQueue queue = session.getReceiveQueue();

      queue.addPacket( packet );

      Packet candidate = queue.peek();
      short processed = session.getLastPacketProcessed();

      while( null != candidate &&
             Protocol.isNextInSequence( candidate.getSequence(),
                                        processed ) )
      {
         queue.pop();
         final DataPacketReadyEvent response =
            new DataPacketReadyEvent( session, candidate );
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
      final Session session = (Session)transport.getUserData();
      if( Session.STATUS_ESTABLISHED != session.getStatus() )
      {
         throw new IOException( "Session not established. " + session );
      }
   }

   private void ensureSessionPresent( final ChannelTransport transport )
      throws IOException
   {
      final Session session = (Session)transport.getUserData();
      if( null == session )
      {
         throw new IOException( "No session specififed." );
      }
   }

   /**
    * Send event indicating that transport should be disconnected.
    *
    * @param transport the transport
    * @param reason the reason for disconnect
    */
   private void
      signalDisconnectTransport( final ChannelTransport transport,
                                 final byte reason )
   {
      final Session session = (Session)transport.getUserData();
      if( null != session )
      {
         output( session, "Error=" + reason );
      }
      else
      {
         System.out.println( "Transport Error=" + reason +
                             " " + transport );
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

   private void output( final Session session, final String text )
   {
      final String message =
         (session.isClient() ? "PACK CL" : "PACK SV") +
         " (" + session.getSessionID() + "): " + text + " -- " +
         session.getUserData();
      System.out.println( message );
   }
}



