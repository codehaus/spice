package org.codehaus.spice.netevent;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Random;
import org.codehaus.spice.event.impl.DefaultEventQueue;
import org.codehaus.spice.event.impl.EventPump;
import org.codehaus.spice.event.impl.collections.UnboundedFifoBuffer;
import org.codehaus.spice.netevent.selector.ServerSocketSelectorEventHandler;
import org.codehaus.spice.netevent.selector.SocketEventSource;
import org.codehaus.spice.netevent.selector.SocketSelectorEventHandler;
import org.realityforge.sca.selector.impl.DefaultSelectorManager;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-07 06:26:17 $
 */
public class TestServer
{
    private static boolean c_done;
    private static final Random RANDOM = new Random();

    public static void main( final String[] args )
        throws Exception
    {
        final DefaultSelectorManager selectorManager =
            new DefaultSelectorManager();
        selectorManager.setRunning( true );
        selectorManager.setSelector( Selector.open() );
        final DefaultEventQueue queue =
            new DefaultEventQueue( new UnboundedFifoBuffer( 15 ) );
        final SocketSelectorEventHandler sHandler =
            new SocketSelectorEventHandler( queue );
        final ServerSocketSelectorEventHandler ssHandler =
            new ServerSocketSelectorEventHandler( queue );

        final SocketEventSource source =
            new SocketEventSource( selectorManager, queue );

        final ServerSocketChannel channel = ServerSocketChannel.open();
        final InetSocketAddress address = new InetSocketAddress( 1980 );
        channel.socket().bind( address );
        selectorManager.registerChannel( channel,
                                         SelectionKey.OP_ACCEPT,
                                         ssHandler,
                                         null );

        final SocketEventHandler handler =
            new SocketEventHandler( selectorManager, sHandler );

        final EventPump pump1 = new EventPump( source, handler );
        pump1.setBatchSize( 10 );

        final Runnable runnable = new Runnable()
        {
            public void run()
            {
                doPump( new EventPump[]{pump1} );
            }
        };
        final Thread thread = new Thread( runnable );
        thread.start();

        while( !c_done )
        {
            Thread.sleep( 10 );
            final Socket socket = new Socket( InetAddress.getLocalHost(), 1980 );
            final int count = Math.abs( RANDOM.nextInt() % 16 * 1024 );
            final int port = socket.getLocalPort();
            System.out.println( "Sending " + count + "B via " + port );
            final OutputStream outputStream = socket.getOutputStream();
            for( int i = 0; i < count; i++ )
            {
                outputStream.write( '.' );
            }
            outputStream.flush();
            outputStream.close();
        }
        System.exit( 1 );
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
                    Thread.sleep( 5 );
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
