package org.realityforge.packet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
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
 * @version $Revision: 1.13 $ $Date: 2004-02-11 03:52:56 $
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

        for( int i = 0; i < SESSIONS.length; i++ )
        {
            SESSIONS[ i ] = new Session();
            SESSIONS[ i ].setUserData( new SessionData( SESSIONS[ i ] ) );
        }

        boolean started = false;
        while( !c_done )
        {
            for( int i = 0; i < SESSIONS.length; i++ )
            {
                final Session session = SESSIONS[ i ];
                final SessionData sd = (SessionData)session.getUserData();
                final int status = session.getStatus();
                if( Session.STATUS_LOST == status ||
                    Session.STATUS_CONNECT_FAILED == status ||
                    Session.STATUS_NOT_CONNECTED == status )
                {
                    if( Session.STATUS_CONNECT_FAILED == status )
                    {
                        final long change =
                            session.getTimeOfLastStatusChange() + 1000;
                        if( change < System.currentTimeMillis() )
                        {
                            System.out.println( "Rejigging conenct that failed " +
                                                "but now ready to go again " +
                                                session +
                                                " time=" +
                                                session.getTimeOfLastStatusChange()
                                                + " now " +
                                                System.currentTimeMillis() );
                            session.setStatus( Session.STATUS_NOT_CONNECTED );
                        }
                    }

                    if( sd.getConnectionCount() == session.getConnections() &&
                        !session.isConnecting() )
                    {
                        if( sd.getConnectionCount() > 0 )
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
                        session.setConnecting( true );
                        makeClientConnection( session );
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

    private static void makeClientConnection( final Session session )
        throws IOException
    {
        final SocketChannel channel = SocketChannel.open();
        synchronized( c_clientSocketSouce )
        {
            c_clientSocketSouce.registerChannel( channel,
                                                 SelectionKey.OP_CONNECT,
                                                 session );
            final InetSocketAddress address =
                new InetSocketAddress( InetAddress.getLocalHost(), 1980 );
            channel.socket().setSoLinger( true, 2 );
            channel.connect( address );
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
                             new TestEventHandler( source3,
                                                   queue1,
                                                   BUFFER_MANAGER,
                                                   "TEST SV" ) );

        final EventPump pump1 = new EventPump( source1, handler1 );
        pump1.setBatchSize( Integer.MAX_VALUE );

        final EventPump pump2 = new EventPump( queue2, handler2 );
        pump2.setBatchSize( Integer.MAX_VALUE );

        final EventPump pump3 = new EventPump( queue3, handler3 );
        pump3.setBatchSize( Integer.MAX_VALUE );

        final EventPump pump4 = new EventPump( source2, handler2 );
        pump4.setBatchSize( Integer.MAX_VALUE );

        final EventPump pump5 = new EventPump( source3, handler3 );
        pump5.setBatchSize( Integer.MAX_VALUE );

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
                             new TestEventHandler( source3,
                                                   queue1,
                                                   BUFFER_MANAGER,
                                                   "TEST CL" ) );

        final EventPump pump1 = new EventPump( source1, handler1 );
        pump1.setBatchSize( Integer.MAX_VALUE );

        final EventPump pump2 = new EventPump( queue2, handler2 );
        pump2.setBatchSize( Integer.MAX_VALUE );

        final EventPump pump3 = new EventPump( queue3, handler3 );
        pump3.setBatchSize( Integer.MAX_VALUE );

        final EventPump pump4 = new EventPump( source2, handler2 );
        pump4.setBatchSize( Integer.MAX_VALUE );

        final EventPump pump5 = new EventPump( source3, handler3 );
        pump5.setBatchSize( Integer.MAX_VALUE );

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
