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
import org.codehaus.spice.netevent.selector.SocketEventSource;
import org.realityforge.packet.handlers.PacketEventHandler;
import org.realityforge.packet.session.DefaultSessionManager;
import org.realityforge.packet.session.Session;

/**
 * @author Peter Donald
 * @version $Revision: 1.4 $ $Date: 2004-01-21 23:49:02 $
 */
public class TestServer
{
    private static boolean c_done;
    private static SocketEventSource c_clientSocketSouce;
    private static final DefaultBufferManager BUFFER_MANAGER = new DefaultBufferManager();

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

        int count = 1;
        final Session session = newSession( true );
        makeClientConnection( session );
        int loop = 0;
        while( !c_done )
        {
            loop++;
            if( count < 20 )
            {
                //makeClientConnection( newSession( false ) );
                count++;
            }
            if( Session.STATUS_LOST == session.getStatus() &&
                null == session.getTransport() )
            {
                System.out.println( "Attempting to re-establish a " +
                                    "connection in " + loop );
                makeClientConnection( session );
            }
            Thread.sleep( 50 );
        }
        System.exit( 1 );
    }

    private static void makeClientConnection( final Session session )
        throws IOException
    {
        final SocketChannel channel = SocketChannel.open();
        channel.configureBlocking( false );
        c_clientSocketSouce.registerChannel( channel,
                                             SelectionKey.OP_CONNECT,
                                             session );
        final InetSocketAddress address =
            new InetSocketAddress( InetAddress.getLocalHost(), 1980 );
        channel.socket().setSoLinger( true, 2 );
        channel.connect( address );
    }

    private static Session newSession( final boolean isPersistent )
    {
        final Session session = new Session();
        session.setUserData( (isPersistent) ? Boolean.TRUE : Boolean.FALSE );
        return session;
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

        final SocketEventSource source1 = new SocketEventSource( queue1 );

        final EventHandler handler1 =
            new EchoHandler( null, //"CHAN SV",
                             new ChannelEventHandler( source1,
                                                      queue1,
                                                      queue2,
                                                      BUFFER_MANAGER ) );

        final EventHandler handler2 =
            new EchoHandler( null, //"PACK SV",
                             new PacketEventHandler( queue2,
                                                     queue3,
                                                     BUFFER_MANAGER,
                                                     new DefaultSessionManager() ) );

        final EventHandler handler3 =
            new EchoHandler( "TEST SV",
                             new TestEventHandler( queue2,
                                                   BUFFER_MANAGER,
                                                   "TEST SV",
                                                   2,
                                                   -1,
                                                   false ) );

        final EventPump pump1 = new EventPump( source1, handler1 );
        pump1.setBatchSize( 10 );

        final EventPump pump2 = new EventPump( queue2, handler2 );
        pump1.setBatchSize( 10 );

        final EventPump pump3 = new EventPump( queue3, handler3 );
        pump1.setBatchSize( 10 );

        final ServerSocketChannel channel = ServerSocketChannel.open();
        channel.socket().bind( new InetSocketAddress( 1980 ) );
        source1.registerChannel( channel,
                                 SelectionKey.OP_ACCEPT,
                                 null );

        return new EventPump[]{pump1, pump2, pump3};
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

        final SocketEventSource source1 = new SocketEventSource( queue1 );

        final EventHandler handler1 =
            new EchoHandler( null, //"CHAN CL",
                             new ChannelEventHandler( source1,
                                                      queue1,
                                                      queue2,
                                                      BUFFER_MANAGER ) );

        final EventHandler handler2 =
            new EchoHandler( null, //"PACK CL",
                             new PacketEventHandler( queue2,
                                                     queue3,
                                                     BUFFER_MANAGER,
                                                     new DefaultSessionManager() ) );

        final EventHandler handler3 =
            new EchoHandler( "TEST CL",
                             new TestEventHandler( queue2,
                                                   BUFFER_MANAGER,
                                                   "TEST CL",
                                                   -1,
                                                   2,
                                                   true ) );

        final EventPump pump1 = new EventPump( source1, handler1 );
        pump1.setBatchSize( 10 );

        final EventPump pump2 = new EventPump( queue2, handler2 );
        pump1.setBatchSize( 10 );

        final EventPump pump3 = new EventPump( queue3, handler3 );
        pump1.setBatchSize( 10 );

        c_clientSocketSouce = source1;

        return new EventPump[]{pump1, pump2, pump3};
    }

    private static void doPump( final EventPump[] pumps )
    {
        for( int i = 0; i < 1000; i++ )
        {
            for( int j = 0; j < pumps.length; j++ )
            {
                pumps[ j ].refresh();
                try
                {
                    Thread.sleep( 2 );
                }
                catch( InterruptedException e )
                {
                    e.printStackTrace();
                }
            }
        }
        c_done = true;
    }
}
