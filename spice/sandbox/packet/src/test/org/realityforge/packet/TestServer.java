package org.realityforge.packet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.impl.DefaultEventQueue;
import org.codehaus.spice.event.impl.EventPump;
import org.codehaus.spice.event.impl.collections.UnboundedFifoBuffer;
import org.codehaus.spice.netevent.buffers.DefaultBufferManager;
import org.codehaus.spice.netevent.handlers.ChannelEventHandler;
import org.codehaus.spice.netevent.source.SelectableChannelEventSource;
import org.codehaus.spice.timeevent.source.TimeEventSource;
import org.realityforge.packet.handlers.PacketIOEventHandler;
import org.realityforge.packet.session.DefaultSessionManager;
import org.realityforge.packet.session.Session;

/**
 * @author Peter Donald
 * @version $Revision: 1.16 $ $Date: 2004-02-18 02:33:38 $
 */
public class TestServer
{
   public static final long START_TIME = System.currentTimeMillis();
   private static boolean c_done;

   private static SelectableChannelEventSource c_clientSocketSouce;
   private static final DefaultBufferManager BUFFER_MANAGER = new DefaultBufferManager();
   public static final DefaultSessionManager SESSION_MANAGER = new DefaultSessionManager();
   private static final int SESSION_COUNT = 53;
   private static final Session[] SESSIONS = new Session[ SESSION_COUNT ];

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

      final Runnable runnable = new Runnable()
      {
         public void run()
         {
            doPump( pumps );
         }
      };
      final Thread thread = new Thread( runnable );
      thread.start();
      thread.setPriority( Thread.NORM_PRIORITY - 1 );

      final InetSocketAddress address =
         new InetSocketAddress( InetAddress.getLocalHost(), 1980 );
      for( int i = 0; i < SESSIONS.length; i++ )
      {
         SESSIONS[ i ] = new Session( address );
      }

      boolean started = false;
      while( !c_done )
      {
         for( int i = 0; i < SESSIONS.length; i++ )
         {
            final Session session = SESSIONS[ i ];
            if( Session.STATUS_NOT_CONNECTED == session.getStatus() )
            {
               final long change =
                  session.getTimeOfLastStatusChange() + 1000;
               if( change < System.currentTimeMillis() &&
                   !session.isConnecting() )
               {
                  if( session.getConnections() > 0 )
                  {
                     final String message =
                        "Re-establish " + session.getSessionID();
                     System.out.println( message );
                  }
                  else
                  {
                     final String message =
                        "Establish conenction to Server.";
                     System.out.println( message );
                  }
                  session.startConnection( c_clientSocketSouce );
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

   private static EventPump[] createServerSidePumps()
      throws IOException
   {
      final DefaultEventQueue queue1 =
         new DefaultEventQueue( new UnboundedFifoBuffer( 15 ) );
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
                                                    queue2,
                                                    queue3,
                                                    BUFFER_MANAGER,
                                                    SESSION_MANAGER ) );

      final EventHandler handler3 =
         new EchoHandler( "TEST SV",
                          new TestEventHandler( queue1,
                                                BUFFER_MANAGER,
                                                "TEST SV" ) );

      final EventPump pump1 = new EventPump( source1, handler1 );
      final EventPump pump2 = new EventPump( queue2, handler2 );
      final EventPump pump3 = new EventPump( queue3, handler3 );
      final EventPump pump4 = new EventPump( source2, handler2 );
      final EventPump pump5 = new EventPump( source3, handler3 );
      final ServerSocketChannel channel = ServerSocketChannel.open();
      source1.registerChannel( channel,
                               SelectionKey.OP_ACCEPT,
                               null );
      channel.socket().bind( new InetSocketAddress( 1980 ) );

      return new EventPump[]{pump1, pump2, pump3, pump4, pump5};
   }

   private static EventPump[] createClientSidePumps()
      throws IOException
   {
      final DefaultEventQueue queue1 =
         new DefaultEventQueue( new UnboundedFifoBuffer( 15 ) );
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
      final TimeEventSource source2 = new TimeEventSource( queue4 );
      final TimeEventSource source3 = new TimeEventSource( queue5 );

      final EventHandler handler1 =
         new EchoHandler( "CHAN CL",
                          new ChannelEventHandler( source1,
                                                   queue1,
                                                   queue2,
                                                   BUFFER_MANAGER ) );

      final EventHandler handler2 =
         new EchoHandler( "PACK CL",
                          new PacketIOEventHandler( source2,
                                                    queue2,
                                                    queue3,
                                                    BUFFER_MANAGER,
                                                    new DefaultSessionManager() ) );

      final EventHandler handler3 =
         new EchoHandler( "TEST CL",
                          new TestEventHandler( queue1,
                                                BUFFER_MANAGER,
                                                "TEST CL" ) );

      final EventPump pump1 = new EventPump( source1, handler1 );
      final EventPump pump2 = new EventPump( queue2, handler2 );
      final EventPump pump3 = new EventPump( queue3, handler3 );
      final EventPump pump4 = new EventPump( source2, handler2 );
      final EventPump pump5 = new EventPump( source3, handler3 );

      c_clientSocketSouce = source1;

      return new EventPump[]{pump1, pump2, pump3, pump4, pump5};
   }

   private static void doPump( final EventPump[] pumps )
   {
      while( !c_done )
      {
         for( int j = 0; j < pumps.length; j++ )
         {
            pumps[ j ].refresh();
         }
      }
   }
}
