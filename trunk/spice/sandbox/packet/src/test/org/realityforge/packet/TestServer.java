package org.realityforge.packet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSource;
import org.codehaus.spice.event.impl.BlockingEventSource;
import org.codehaus.spice.event.impl.DefaultEventQueue;
import org.codehaus.spice.event.impl.EventPump;
import org.codehaus.spice.event.impl.collections.UnboundedFifoBuffer;
import org.codehaus.spice.event.impl.collections.BoundedFifoBuffer;
import org.codehaus.spice.netevent.buffers.DefaultBufferManager;
import org.codehaus.spice.netevent.handlers.ChannelEventHandler;
import org.codehaus.spice.netevent.source.SelectableChannelEventSource;
import org.codehaus.spice.timeevent.source.TimeEventSource;
import org.realityforge.packet.handlers.PacketIOEventHandler;
import org.realityforge.packet.session.DefaultSessionManager;
import org.realityforge.packet.session.Session;

/**
 * @author Peter Donald
 * @version $Revision: 1.19 $ $Date: 2004-03-21 23:32:36 $
 */
public class TestServer
{
   private static boolean c_done;

   private static final DefaultBufferManager BUFFER_MANAGER = new DefaultBufferManager();
   public static final DefaultSessionManager SESSION_MANAGER = new DefaultSessionManager();
   private static final int SESSION_COUNT = 50; //153;
   private static final Session[] SESSIONS = new Session[ SESSION_COUNT ];
   static final int TX_SIZE_FACTOR = 1024 * 2;
   static final int MESSAGE_COUNT = 5;
   private static DefaultEventQueue c_sessionQueue;

   public static void main( final String[] args )
      throws Exception
   {
      final EventPump[] serverSidePumps = createServerSidePumps();
      final EventPump[] clientSidePumps = createClientSidePumps();
      final ArrayList pumpList = new ArrayList();
      pumpList.addAll( Arrays.asList( serverSidePumps ) );
      pumpList.addAll( Arrays.asList( clientSidePumps ) );
      final EventPump[] pumps =
         (EventPump[])pumpList.toArray( new EventPump[ pumpList.size() ] );

      startPumps( pumps );

      final InetSocketAddress address =
         new InetSocketAddress( InetAddress.getLocalHost(), 1980 );
      for( int i = 0; i < SESSIONS.length; i++ )
      {
         SESSIONS[ i ] = new Session( address, c_sessionQueue );
      }

      boolean started = false;
      while( !c_done )
      {
         for( int i = 0; i < SESSIONS.length; i++ )
         {
            final Session session = SESSIONS[ i ];
            final long change = session.getLastCommTime() + 1000;
            if( Session.STATUS_NOT_CONNECTED == session.getStatus() &&
                change < System.currentTimeMillis() &&
                !session.isConnecting() )
            {
               session.startConnection();
               if( session.getConnections() > 0 )
               {
                  final String message =
                     "Re-establish SessionID=" + session.getSessionID();
                  System.out.println( message );
               }
               else
               {
                  final String message =
                     "Establish conenction to Server.";
                  System.out.println( message );
               }
            }
         }

         if( SESSION_MANAGER.getSessionCount() > 0 )
         {
            started = true;
         }
         if( started && 0 == SESSION_MANAGER.getSessionCount() )
         {
            c_done = true;
         }
         try
         {
            Thread.sleep( 5 );
         }
         catch( InterruptedException e )
         {
            e.printStackTrace();
         }
      }
   }

   private static void startPumps( final EventPump[] pumps )
   {
      /*
      final Runnable runnable = new Runnable()
      {
         public void run()
         {
            doPump( pumps );
         }
      };
      startThread( runnable );
      */
      for( int i = 0; i < pumps.length; i++ )
      {
         final EventPump pump = pumps[ i ];
         startThread( pump );
      }
   }

   private static void startThread( final EventPump pump )
   {
      final Runnable runnable = new Runnable()
      {
         public void run()
         {
             try
             {
                 doPump( pump );
             }
             catch( final Throwable e )
             {
                 e.printStackTrace( );
             }
         }
      };
      final Thread thread = new Thread( runnable );
      thread.setName( pump.getName() );
      thread.start();

      thread.setPriority( Thread.NORM_PRIORITY - 1 );
   }

   private static EventPump[] createServerSidePumps()
      throws IOException
   {
      final DefaultEventQueue queue1 =
         new DefaultEventQueue( new BoundedFifoBuffer( 15 ) );
      final DefaultEventQueue queue2 =
         new DefaultEventQueue( new UnboundedFifoBuffer( 15 ) );
      final DefaultEventQueue queue3 =
         new DefaultEventQueue( new UnboundedFifoBuffer( 15 ) );
      final DefaultEventQueue queue4 =
         new DefaultEventQueue( new UnboundedFifoBuffer( 15 ) );
      final DefaultEventQueue queue5 =
         new DefaultEventQueue( new UnboundedFifoBuffer( 15 ) );

      final SelectableChannelEventSource source1 =
         new SelectableChannelEventSource( queue1 );
      source1.setSelectTimeout( 0 );
      final TimeEventSource source2 = new TimeEventSource( queue4 );
      final TimeEventSource source3 = new TimeEventSource( queue5 );

      final EventHandler handler1 =
         new EchoHandler( "CHAN SV",
                          new ChannelEventHandler( source1,
                                                   queue1,
                                                   queue2,
                                                   BUFFER_MANAGER ) );

      final EventHandler handler2 =
         new EchoHandler( "PACK SV",
                          new PacketIOEventHandler( source2,
                                                    source1,
                                                    queue2,
                                                    queue3,
                                                    BUFFER_MANAGER,
                                                    SESSION_MANAGER ) );

      final EventHandler handler3 =
         new EchoHandler( "TEST SV",
                          new TestEventHandler( queue1,
                                                BUFFER_MANAGER,
                                                "TEST SV" ) );

      final EventPump pump1 = newPump( "CHAN SV", source1, handler1 );
      final EventPump pump2 = newPump( "PACK SV", queue2, handler2 );
      final EventPump pump3 = newPump( "TEST SV", queue3, handler3 );
      final EventPump pump4 = newPump( "PACK TIME SV", source2, handler2 );
      final EventPump pump5 = newPump( "TEST TIME SV", source3, handler3 );
      final ServerSocketChannel channel = ServerSocketChannel.open();
      source1.registerChannel( channel,
                               SelectionKey.OP_ACCEPT,
                               null );
      channel.socket().bind( new InetSocketAddress( 1980 ) );

      return new EventPump[]{pump1, pump2, pump3, pump4, pump5};
   }

   private static EventPump newPump( final String name,
                                     final EventSource source,
                                     final EventHandler handler )
   {
      return new EventPump( name, new BlockingEventSource( source ), handler );
   }

   private static EventPump[] createClientSidePumps()
      throws IOException
   {
      final DefaultEventQueue queue1 =
         new DefaultEventQueue( new BoundedFifoBuffer( 15 ) );
      c_sessionQueue = new DefaultEventQueue( new UnboundedFifoBuffer( 15 ) );
      final DefaultEventQueue queue3 =
         new DefaultEventQueue( new UnboundedFifoBuffer( 15 ) );
      final DefaultEventQueue queue4 =
         new DefaultEventQueue( new UnboundedFifoBuffer( 15 ) );
      final DefaultEventQueue queue5 =
         new DefaultEventQueue( new UnboundedFifoBuffer( 15 ) );

      final SelectableChannelEventSource source1 =
         new SelectableChannelEventSource( queue1 );
      source1.setSelectTimeout( 0 );
      final TimeEventSource source2 = new TimeEventSource( queue4 );
      final TimeEventSource source3 = new TimeEventSource( queue5 );

      final EventHandler handler1 =
         new EchoHandler( "CHAN CL",
                          new ChannelEventHandler( source1,
                                                   queue1,
                                                   c_sessionQueue,
                                                   BUFFER_MANAGER ) );

      final EventHandler handler2 =
         new EchoHandler( "PACK CL",
                          new PacketIOEventHandler( source2,
                                                    source1,
                                                    c_sessionQueue,
                                                    queue3,
                                                    BUFFER_MANAGER,
                                                    new DefaultSessionManager() ) );

      final EventHandler handler3 =
         new EchoHandler( "TEST CL",
                          new TestEventHandler( queue1,
                                                BUFFER_MANAGER,
                                                "TEST CL" ) );

      final EventPump pump1 = newPump( "CHAN CL", source1, handler1 );
      final EventPump pump2 = newPump( "PACK CL", c_sessionQueue, handler2 );
      final EventPump pump3 = newPump( "TEST CL", queue3, handler3 );
      final EventPump pump4 = newPump( "PACK TIME CL", source2, handler2 );
      final EventPump pump5 = newPump( "TEST TIME CL", source3, handler3 );

      return new EventPump[]{pump1, pump2, pump3, pump4, pump5};
   }

   private static void doPump( final EventPump pump )
   {
      while( !c_done )
      {
         pump.refresh();
      }
   }
}
