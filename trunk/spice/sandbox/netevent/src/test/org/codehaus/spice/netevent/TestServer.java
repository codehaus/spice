package org.codehaus.spice.netevent;

import java.io.IOException;
import java.io.InputStream;
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
import org.codehaus.spice.netevent.buffers.DefaultBufferManager;
import org.codehaus.spice.netevent.handlers.ChannelEventHandler;
import org.codehaus.spice.netevent.selector.SocketEventSource;
import org.realityforge.sca.selector.impl.DefaultSelectorManager;

/**
 * @author Peter Donald
 * @version $Revision: 1.6 $ $Date: 2004-01-15 06:12:25 $
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
        final DefaultEventQueue queue1 =
            new DefaultEventQueue( new UnboundedFifoBuffer( 15 ) );
        final DefaultEventQueue queue2 =
            new DefaultEventQueue( new UnboundedFifoBuffer( 15 ) );

        final SocketEventSource source =
            new SocketEventSource( selectorManager, queue1 );

        final ServerSocketChannel channel = ServerSocketChannel.open();
        channel.socket().bind( new InetSocketAddress( 1980 ) );
        source.registerChannel( channel,
                                SelectionKey.OP_ACCEPT,
                                null );

        final DefaultBufferManager bufferManager =
            new DefaultBufferManager();

        final ChannelEventHandler handler1 =
            new ChannelEventHandler( source, queue1, queue2, bufferManager );

        final TestSocketEventHandler handler2 = new TestSocketEventHandler();

        final EventPump pump1 = new EventPump( source, handler1 );
        pump1.setBatchSize( 10 );

        final EventPump pump2 = new EventPump( queue2, handler2 );
        pump1.setBatchSize( 10 );

        final Runnable runnable = new Runnable()
        {
            public void run()
            {
                doPump( new EventPump[]{pump1, pump2} );
            }
        };
        final Thread thread = new Thread( runnable );
        thread.start();

        while( !c_done )
        {
            Thread.sleep( 10 );
            final Socket socket = new Socket( InetAddress.getLocalHost(), 1980 );

            writeOutput( socket );
            final Runnable reader = new Runnable()
            {
                public void run()
                {
                    readInput( socket );
                }
            };
            final Thread inputThread = new Thread( reader );
            inputThread.start();
        }
        System.exit( 1 );
    }

    private static void writeOutput( final Socket socket )
        throws IOException
    {
        final int count = Math.abs( RANDOM.nextInt() % 16 * 1024 );
        System.out.println(
            "Sending " + count + "B via " + socket.getLocalPort() );
        final OutputStream outputStream = socket.getOutputStream();
        for( int i = 0; i < count; i++ )
        {
            outputStream.write( '.' );
        }
        outputStream.flush();
    }

    private static void readInput( final Socket socket )
    {
        try
        {
            final InputStream inputStream = socket.getInputStream();
            final StringBuffer sb = new StringBuffer();
            for( int i = 0; i < 4; i++ )
            {
                final int ch = inputStream.read();
                sb.append( (char)ch );
            }
            System.out.println(
                "Response received " + sb + " via " + socket.getLocalPort() );

            socket.close();
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
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
