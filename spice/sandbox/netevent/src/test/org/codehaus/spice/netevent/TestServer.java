package org.codehaus.spice.netevent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import org.codehaus.spice.event.impl.DefaultEventQueue;
import org.codehaus.spice.event.impl.EventPump;
import org.codehaus.spice.event.impl.collections.UnboundedFifoBuffer;
import org.codehaus.spice.netevent.buffers.DefaultBufferManager;
import org.codehaus.spice.netevent.handlers.ChannelEventHandler;
import org.codehaus.spice.netevent.selector.SocketEventSource;

/**
 * @author Peter Donald
 * @version $Revision: 1.8 $ $Date: 2004-01-21 23:44:58 $
 */
public class TestServer
{
    private static boolean c_done;
    private static SocketEventSource c_clientSocketSouce;

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

        while( !c_done )
        {
            Thread.sleep( 50 );
            final SocketChannel channel = SocketChannel.open();
            channel.configureBlocking( false );
            c_clientSocketSouce.registerChannel( channel,
                                                 SelectionKey.OP_CONNECT,
                                                 null );
            final InetSocketAddress address =
                new InetSocketAddress( InetAddress.getLocalHost(), 1980 );
            channel.connect( address );
        }
        System.exit( 1 );
    }

    private static EventPump[] createServerSidePumps()
        throws IOException
    {
        final DefaultEventQueue queue1 =
            new DefaultEventQueue( new UnboundedFifoBuffer( 15 ) );
        final DefaultEventQueue queue2 =
            new DefaultEventQueue( new UnboundedFifoBuffer( 15 ) );

        final SocketEventSource source1 = new SocketEventSource( queue1 );

        final ServerSocketChannel channel = ServerSocketChannel.open();
        channel.socket().bind( new InetSocketAddress( 1980 ) );
        source1.registerChannel( channel,
                                 SelectionKey.OP_ACCEPT,
                                 null );

        final DefaultBufferManager bufferManager =
            new DefaultBufferManager();

        final ChannelEventHandler handler1 =
            new ChannelEventHandler( source1, queue1, queue2, bufferManager );

        final TestEventHandler handler2 =
            new TestEventHandler( "SV", 5, -1, false );

        final EventPump pump1 = new EventPump( source1, handler1 );
        pump1.setBatchSize( 10 );

        final EventPump pump2 = new EventPump( queue2, handler2 );
        pump1.setBatchSize( 10 );

        return new EventPump[]{pump1, pump2};
    }

    private static EventPump[] createClientSidePumps()
        throws IOException
    {
        final DefaultEventQueue queue1 =
            new DefaultEventQueue( new UnboundedFifoBuffer( 15 ) );
        final DefaultEventQueue queue2 =
            new DefaultEventQueue( new UnboundedFifoBuffer( 15 ) );

        c_clientSocketSouce = new SocketEventSource( queue1 );

        final DefaultBufferManager bufferManager = new DefaultBufferManager();

        final ChannelEventHandler handler1 =
            new ChannelEventHandler( c_clientSocketSouce, queue1, queue2,
                                     bufferManager );

        final TestEventHandler handler2 =
            new TestEventHandler( "CL", -1, 5, true );

        final EventPump pump1 = new EventPump( c_clientSocketSouce, handler1 );
        pump1.setBatchSize( 10 );

        final EventPump pump2 = new EventPump( queue2, handler2 );
        pump1.setBatchSize( 10 );

        return new EventPump[]{pump1, pump2};
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
