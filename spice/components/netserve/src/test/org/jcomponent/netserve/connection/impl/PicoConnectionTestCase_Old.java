/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.impl;

import org.jcomponent.netserve.connection.ConnectionManager;
import org.jcomponent.netserve.sockets.SocketAcceptorManager;
import org.jcomponent.threadpool.ThreadPool;

/**
 * TestCase for {@link PicoConnectionManager}.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-14 04:12:18 $
 */
public class PicoConnectionTestCase_Old
    extends AbstractConnectionTestCase
{
    protected void setUp() throws Exception
    {
        setMonitor( createConnectionMonitor() );
    }

    protected ConnectionManager createConnectionManager( boolean addThreadPool,
                                                         final SocketAcceptorManager socketManager,
                                                         final boolean forceShutdown,
                                                         final int shutdownTimeout )
        throws Exception
    {

        ThreadPool threadPool = null;
        if( addThreadPool )
        {
            threadPool = new TestThreadPool();
        }
        final ConnectionManager cm =
            new PicoConnectionManager.WithMonitorAndConfig( createConnectionMonitor(),
                                                            threadPool,
                                                            socketManager,
                                                            forceShutdown,
                                                            shutdownTimeout );
        return cm;
    }

    protected ConnectionMonitor createConnectionMonitor()
    {
        return new NullConnectionMonitor();
    }

    protected ConnectionMonitor createConnectionMonitorNoLogging()
    {
        return new NullConnectionMonitor();
    }

    protected void disposeConnectionManager( final ConnectionManager cm )
    {
        final PicoConnectionManager pico = (PicoConnectionManager)cm;
        pico.dispose();
    }
}
