/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets.impl;

import org.jcomponent.netserve.sockets.SocketAcceptorManager;
import java.nio.channels.ServerSocketChannel;
import java.net.ServerSocket;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.6 $ $Date: 2003-10-09 09:58:23 $
 */
public class NIOAcceptorManagerTestCase
    extends AbstractAcceptorManagerTestCase
{
    private static int c_index;

    public void testHandleChannelWithNonExistentEntry()
        throws Exception
    {
        final NIOAcceptorManager manager = new NIOAcceptorManager();
        final ServerSocketChannel channel = ServerSocketChannel.open();
        manager.handleChannel( channel );
    }

    public void testStartupAndShutdown()
        throws Exception
    {
        final NIOAcceptorManager manager = new NIOAcceptorManager();
        manager.setMonitor( new NullAcceptorMonitor() );
        assertEquals( "isRunning() pre startup", false, manager.isRunning() );
        manager.startupSelector();
        assertEquals( "isRunning() post startup", true, manager.isRunning() );
        manager.shutdownSelector();
        assertEquals( "isRunning() post shutdown", false, manager.isRunning() );
    }

    public void testShutdownWithoutStartup()
        throws Exception
    {
        final NIOAcceptorManager manager = new NIOAcceptorManager();
        manager.setMonitor( new NullAcceptorMonitor() );
        assertEquals( "isRunning() pre shutdown", false, manager.isRunning() );
        manager.shutdownSelector();
        assertEquals( "isRunning() post shutdown", false, manager.isRunning() );
    }

    public void testDuplicateConnect()
        throws Exception
    {
        final SocketAcceptorManager manager = createAcceptorManager();
        final String name = "name";
        assertEquals( "isConnected pre connect", false, manager.isConnected( name ) );
        final ServerSocketChannel channel = ServerSocketChannel.open();
        manager.connect( name,
                         channel.socket(),
                         new MockSocketConnectionHandler() );
        assertEquals( "isConnected pre disconnect", true, manager.isConnected( name ) );
        try
        {
            manager.connect( name,
                             new ExceptOnAcceptServerSocket( true ),
                             new MockSocketConnectionHandler() );
        }
        catch( final IllegalArgumentException iae )
        {
            return;
        }
        finally
        {
            shutdownAcceptorManager( manager );
            channel.close();
            assertEquals( "isConnected post disconnect", false, manager.isConnected( name ) );
        }
        fail( "Expected to fail due to duplicate connect" );
    }

    public void testAcceptConnections()
        throws Exception
    {
        final SocketAcceptorManager manager = createAcceptorManager();
        final String name = "name";
        assertEquals( "isConnected pre connect", false, manager.isConnected( name ) );
        final ServerSocketChannel channel = ServerSocketChannel.open();
        try
        {
            final ServerSocket socket = channel.socket();
            socket.setReuseAddress( true );
            final InetAddress localAddress = InetAddress.getLocalHost();
            final int port = nextPort();
            final InetSocketAddress address = new InetSocketAddress( localAddress, port );
            socket.bind( address );
            manager.connect( name,
                             socket,
                             new ClosingSocketConnectionHandler() );
            assertEquals( "isConnected pre disconnect", true, manager.isConnected( name ) );

            final Socket clientSocket = new Socket( localAddress, port );
            final InputStream inputStream = clientSocket.getInputStream();
            final StringBuffer sb = new StringBuffer();
            while( sb.length() < ClosingSocketConnectionHandler.MESSAGE.length() )
            {
                final int ch = inputStream.read();
                if( -1 != ch )
                {
                    sb.append( (char)ch );
                }
            }
            clientSocket.close();
            final String message = sb.toString();
            assertEquals( "message", ClosingSocketConnectionHandler.MESSAGE, message );
        }
        finally
        {
            shutdownAcceptorManager( manager );
            channel.close();
            assertEquals( "isConnected post disconnect", false, manager.isConnected( name ) );
        }
    }

    public void testExceptingHandler()
        throws Exception
    {
        final NIOAcceptorManager manager = new NIOAcceptorManager();
        manager.startupSelector();
        final RecordingAcceptorMonitor monitor = new RecordingAcceptorMonitor();
        manager.setMonitor( monitor );
        manager.setTimeout( 500 );

        final String name = "name";
        assertEquals( "isConnected pre connect", false, manager.isConnected( name ) );
        final ServerSocketChannel channel = ServerSocketChannel.open();
        try
        {
            final ServerSocket socket = channel.socket();
            socket.setReuseAddress( true );
            final InetAddress localAddress = InetAddress.getLocalHost();
            final int port = nextPort();
            final InetSocketAddress address =
                new InetSocketAddress( localAddress, port );
            socket.bind( address );
            manager.connect( name,
                             socket,
                             new ExceptingSocketConnectionHandler() );
            assertEquals( "isConnected pre disconnect",
                          true,
                          manager.isConnected( name ) );

            final Socket clientSocket = new Socket( localAddress, port );
            final OutputStream outputStream = clientSocket.getOutputStream();
            outputStream.write( 'a' );
            outputStream.flush();
            clientSocket.close();

            try
            {
                Thread.sleep( 50 );
            }
            catch( InterruptedException e )
            {
            }

            assertEquals( "error",
                          ExceptingSocketConnectionHandler.EXCEPTION,
                          monitor.getErrorAcceptingConnection() );
        }
        finally
        {
            shutdownAcceptorManager( manager );
            channel.close();
            assertEquals( "isConnected post disconnect", false, manager.isConnected( name ) );
        }
    }

    private int nextPort()
    {
        final int random = new Random().nextInt() % 100;
        return 1400 + c_index++ + random;
    }

    protected SocketAcceptorManager createAcceptorManager()
        throws Exception
    {
        final NIOAcceptorManager manager = new NIOAcceptorManager();
        manager.startupSelector();
        return manager;
    }

    protected void shutdownAcceptorManager( SocketAcceptorManager manager )
        throws Exception
    {
        ( (NIOAcceptorManager)manager ).shutdownSelector();
    }
}

