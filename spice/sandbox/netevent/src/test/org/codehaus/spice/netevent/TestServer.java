package org.codehaus.spice.netevent;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import org.codehaus.spice.event.impl.EventPump;

/**
 * @author Peter Donald
 * @version $Revision: 1.13 $ $Date: 2004-05-17 06:15:21 $
 */
public class TestServer
{
    private static final int MAX_CONNECTIONS = 500;
    private static final int SERVER_PORT = 1980;

    private static boolean c_done;

    public static void main( final String[] args )
        throws Exception
    {
        final NetRuntime server = NetRuntime.createRuntime( "SV", 5, -1, false );
        final NetRuntime client = NetRuntime.createRuntime( "CL", -1, 5, true );

        startPumps( server.getPumps() );
        startPumps( client.getPumps() );

        final TestEventHandler ch = client.getTestEventHandler();

        final ServerSocketChannel ss = ServerSocketChannel.open();
        server.getSource().registerChannel( ss, SelectionKey.OP_ACCEPT, null );
        ss.socket().bind( new InetSocketAddress( SERVER_PORT ) );

        int count = 0;
        while( !c_done )
        {
            Thread.sleep( 50 );
            if( MAX_CONNECTIONS > count )
            {
                count++;
                final SocketChannel channel = SocketChannel.open();
                System.out.println( "Creating Client Connection: " + count );
                client.getSource().registerChannel( channel, SelectionKey.OP_CONNECT, null );
                final InetSocketAddress address = new InetSocketAddress(
                    InetAddress.getLocalHost(), SERVER_PORT );
                channel.connect( address );
            }

            if( MAX_CONNECTIONS <= ch.getConnectCount() &&
                ch.getConnectCount() == ch.getDisconnectCount() )
            {
                c_done = true;
            }
        }
        System.exit( 1 );
    }

    private static void startPumps( final EventPump[] pumps )
    {
        for( int i = 0; i < pumps.length; i++ )
        {
            final EventPump pump = pumps[i];
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
            System.out.println( "Entering Thread " + Thread.currentThread().getName() );
            while( !c_done )
            {
                pump.refresh();
            }
            System.out.println( "Exiting Thread " + Thread.currentThread().getName() );
        }
        catch( final Throwable e )
        {
            e.printStackTrace();
        }
    }
}
