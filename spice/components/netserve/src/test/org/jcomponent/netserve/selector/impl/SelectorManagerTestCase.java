package org.jcomponent.netserve.selector.impl;

import com.mockobjects.dynamic.C;
import com.mockobjects.dynamic.Mock;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Random;
import junit.framework.TestCase;

public class SelectorManagerTestCase
    extends TestCase
{
    public void testSetMonitorUsingNonNull()
        throws Exception
    {
        final DefaultSelectorManager manager = new DefaultSelectorManager();
        final NullSelectorMonitor monitor = new NullSelectorMonitor();
        manager.setMonitor( monitor );
        assertEquals( "monitor", monitor, manager.getMonitor() );
    }

    public void testSetMonitorUsingNull()
        throws Exception
    {
        final DefaultSelectorManager manager = new DefaultSelectorManager();
        try
        {
            manager.setMonitor( null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "monitor", npe.getMessage() );
            return;
        }
        fail( "expected to fail with npe" );
    }

    public void testStartupAndShutdownSelectorManager()
        throws Exception
    {
        final Mock mockMonitor = new Mock( SelectorMonitor.class );

        mockMonitor.expect( "selectorStartup", C.NO_ARGS );
        mockMonitor.expect( "enteringSelectorLoop", C.NO_ARGS );
        mockMonitor.expect( "enteringSelect", C.NO_ARGS );
        mockMonitor.expect( "selectCompleted", C.args( C.eq( 0 ) ) );
        mockMonitor.expect( "exitingSelectorLoop", C.NO_ARGS );
        mockMonitor.expect( "selectorShutdown", C.NO_ARGS );
        final SelectorMonitor monitor = (SelectorMonitor)mockMonitor.proxy();

        final DefaultSelectorManager manager = new DefaultSelectorManager();
        manager.setTimeout( 4000 );
        manager.setMonitor( monitor );
        assertEquals( "isRunning pre start", false, manager.isRunning() );
        assertNullSelector( manager );
        manager.startup();
        assertEquals( "isRunning post start", true, manager.isRunning() );
        assertNotNull( "getSelector post start", manager.getSelector() );
        Thread.sleep( 150 );
        manager.shutdown();
        assertNullSelector( manager );
        assertEquals( "isRunning post shutdown", false, manager.isRunning() );

        mockMonitor.verify();
    }

    public void testShutdownSelectorManagerWithoutStatrup()
        throws Exception
    {
        final Mock mockMonitor = new Mock( SelectorMonitor.class );
        final SelectorMonitor monitor = (SelectorMonitor)mockMonitor.proxy();

        final DefaultSelectorManager manager = new DefaultSelectorManager();
        manager.setMonitor( monitor );
        manager.setTimeout( 400 );
        assertEquals( "isRunning pre shutdown", false, manager.isRunning() );
        assertNullSelector( manager );
        manager.shutdown();
        assertNullSelector( manager );
        assertEquals( "isRunning post shutdown", false, manager.isRunning() );

        mockMonitor.verify();
    }

    public void testShutdownSelectorInErrorState()
        throws Exception
    {
        final Mock mockMonitor = new Mock( SelectorMonitor.class );

        mockMonitor.expect( "selectorShutdown", C.NO_ARGS );
        mockMonitor.expect( "errorClosingSelector", C.args( C.eq( MockSelector.EXCEPTION ) ) );
        final SelectorMonitor monitor = (SelectorMonitor)mockMonitor.proxy();

        final DefaultSelectorManager manager = new DefaultSelectorManager();
        manager.setTimeout( 4000 );
        manager.setMonitor( monitor );

        final Selector selector = new MockSelector();
        manager.setSelector( selector );
        manager.shutdownSelector();
        assertNotNull( "getSelector post start", manager.getSelector() );

        mockMonitor.verify();
    }

    public void testSimpleConnect()
        throws Exception
    {
        final ServerSocketChannel channel = ServerSocketChannel.open();
        channel.socket().setReuseAddress( true );
        final InetAddress localAddress = InetAddress.getLocalHost();
        final Random random = new Random();
        final int port = Math.abs( random.nextInt() % 5000 ) + 1024;
        final InetSocketAddress address = new InetSocketAddress( localAddress, port );
        channel.socket().bind( address );
        System.out.println( "address = " + address );
        //Wait for binding to go through
        Thread.sleep( 200 );

        final Mock mockMonitor = new Mock( SelectorMonitor.class );

        mockMonitor.expect( "selectorStartup", C.NO_ARGS );
        mockMonitor.expect( "enteringSelectorLoop", C.NO_ARGS );
        mockMonitor.expect( "enteringSelect", C.NO_ARGS );
        mockMonitor.expect( "selectCompleted", C.args( C.eq( 0 ) ) );
        mockMonitor.expect( "enteringSelect", C.NO_ARGS );
        mockMonitor.expect( "selectCompleted", C.args( C.eq( 1 ) ) );
        mockMonitor.expect( "enteringSelect", C.NO_ARGS );
        mockMonitor.expect( "selectorShutdown", C.NO_ARGS );
        mockMonitor.expect( "selectCompleted", C.args( C.eq( 0 ) ) );
        mockMonitor.expect( "exitingSelectorLoop", C.NO_ARGS );
        final SelectorMonitor monitor = (SelectorMonitor)mockMonitor.proxy();

        final DefaultSelectorManager manager = new DefaultSelectorManager();
        manager.setTimeout( 5000 );
        manager.setMonitor( monitor );

        assertEquals( "isRunning pre start", false, manager.isRunning() );
        assertNullSelector( manager );
        manager.startup();
        assertEquals( "isRunning post start", true, manager.isRunning() );
        assertNotNull( "getSelector post start", manager.getSelector() );

        manager.registerChannel( channel,
                                 SelectionKey.OP_ACCEPT,
                                 new HelloSelectorEventHandler(),
                                 null );

        final Socket clientSocket = new Socket( localAddress, port );
        System.out.print( "Socket Connecting" );
        while( !clientSocket.isConnected() )
        {
            System.out.print( "." );
            Thread.yield();
        }
        System.out.println( " - Connected" );

        final InputStream inputStream = clientSocket.getInputStream();
        final StringBuffer sb = new StringBuffer();
        while( sb.length() < HelloSelectorEventHandler.MESSAGE.length )
        {
            System.out.print( "-" );
            final int ch = inputStream.read();
            if( -1 != ch )
            {
                sb.append( (char)ch );
            }
        }
        clientSocket.close();
        System.out.println( "Message " + sb );
        assertEquals( "message",
                      new String( HelloSelectorEventHandler.MESSAGE ),
                      sb.toString() );

        manager.shutdown();
        assertNullSelector( manager );
        assertEquals( "isRunning post shutdown", false, manager.isRunning() );

        mockMonitor.verify();
    }

    private void assertNullSelector( final DefaultSelectorManager manager )
    {
        try
        {
            manager.getSelector();
            fail( "Expected NPE in getSelector" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "getMessage()", "selector", npe.getMessage() );
        }
    }
}
