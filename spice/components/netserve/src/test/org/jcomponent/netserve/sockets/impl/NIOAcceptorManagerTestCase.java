/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets.impl;

import org.jcomponent.netserve.sockets.SocketAcceptorManager;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-10-09 07:02:25 $
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
