package org.codehaus.spice.packet;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import org.codehaus.spice.event.impl.EventPump;
import org.codehaus.spice.packet.session.Session;

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-03-26 02:23:01 $
 */
public class TestServer
{
   private static boolean c_done;

   private static final int SESSION_COUNT = 5; //153;
   static final int TX_SIZE_FACTOR = 1024 * 2;
   static final int MESSAGE_COUNT = 5;

   public static void main( final String[] args )
      throws Exception
   {
      final NetRuntime serverRuntime = NetRuntime.createRuntime( "SV" );
      final NetRuntime clientRuntime = NetRuntime.createRuntime( "CL" );

      final ServerSocketChannel channel = ServerSocketChannel.open();
      serverRuntime.getSource().
         registerChannel( channel, SelectionKey.OP_ACCEPT, null );
      channel.socket().bind( new InetSocketAddress( 1980 ) );

      startPumps( serverRuntime.getPumps() );
      startPumps( clientRuntime.getPumps() );

      final InetSocketAddress address =
         new InetSocketAddress( InetAddress.getLocalHost(), 1980 );

      final Session[] sessions = initSessions( address, clientRuntime );

      boolean started = false;
      while( !c_done )
      {
         for( int i = 0; i < sessions.length; i++ )
         {
            final Session session = sessions[ i ];
            final long change = session.getLastCommTime() + 1000;
            if( Session.STATUS_NOT_CONNECTED == session.getStatus() &&
                change < System.currentTimeMillis() &&
                !session.isConnecting() )
            {
               session.startConnection();

               final String sessionDesc =
                  "  [" +
                  serverRuntime.getSessionManager().getSessionCount() + ":" +
                  clientRuntime.getSessionManager().getSessionCount() + "]";

               if( session.getConnections() > 0 &&
                   -1 != session.getSessionID() )
               {
                  final String message = "Re-establish SessionID=" +
                                         session.getSessionID() +
                                         sessionDesc;
                  System.out.println( message );
               }
               else
               {
                  final String message =
                     "Establish conenction to Server." + sessionDesc;
                  System.out.println( message );
               }
            }
         }

         if( serverRuntime.getSessionManager().getSessionCount() > 0 )
         {
            started = true;
         }
         if( started &&
             0 == serverRuntime.getSessionManager().getSessionCount() &&
             0 == clientRuntime.getSessionManager().getSessionCount() )
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

   private static Session[] initSessions( final InetSocketAddress address,
                                          final NetRuntime clientRuntime )
   {
      final Session[] sessions = new Session[ SESSION_COUNT ];
      for( int i = 0; i < sessions.length; i++ )
      {
         sessions[ i ] = new Session( address,
                                      clientRuntime.getSessionSink() );
      }
      return sessions;
   }

   private static void startPumps( final EventPump[] pumps )
   {
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
            doPump( pump );
         }
      };
      final Thread thread = new Thread( runnable );
      thread.setName( pump.getName() );
      thread.start();

      thread.setPriority( Thread.NORM_PRIORITY - 1 );
   }

   private static void doPump( final EventPump pump )
   {
      try
      {
         System.out.println( "Entering Thread " +
                             Thread.currentThread().getName() );
         while( !c_done )
         {
            pump.refresh();
         }
         System.out.println( "Exiting Thread " +
                             Thread.currentThread().getName() );
      }
      catch( final Throwable e )
      {
         e.printStackTrace();
      }
   }
}
