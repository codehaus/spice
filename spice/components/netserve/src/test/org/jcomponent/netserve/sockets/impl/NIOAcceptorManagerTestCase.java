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

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-10-09 07:11:45 $
 */
public class NIOAcceptorManagerTestCase
    extends AbstractAcceptorManagerTestCase
{
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
