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
import org.jcomponent.netserve.sockets.impl.DefaultAcceptorManager;
import org.jcomponent.threadpool.ThreadPool;

/**
 * An implementation of {@link ConnectionManager} which is PicoContainer compatible.
 *
 * <p>A sample of configuration parameters for the component are given below. Note that on some OS/JVM
 * combinations <tt>soTimeout</tt> must be set to non-0 value or else the ServerSocket will
 * never get out of accept() system call and we wont be able to shutdown the server
 * socket properly. However it can introduce performance problems if constantly
 * timing out. <tt>shutdownTimeout</tt> indicates how long we should wait to see if
 * incoming connections will shutdown gracefully when asked. If they dont shutdown
 * gracefully and <tt>forceShutdown</tt> is true then the connection will be forced
 * to be shutdown if the user asked for connection to be "tearedDown".</p>
 * <ul>
 *  <li>soTimeout=500 -- 500 ms timeouts on Server Sockets --</li>
 *  <li>forceShutdown=true -- forcefully shutdown connections if they don't shutdown gracefully --</li>
 *  <li>shutdownTimeout=200 -- wait 200ms for connections to gracefully shutdown --</li>
 * </ul>
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @version $Revision: 1.2 $ $Date: 2003-10-14 04:12:43 $
 */
public class PicoConnectionManager
    extends AbstractConnectionManager
    implements ConnectionManager
{
    public static class Default
        extends PicoConnectionManager
    {
        public Default( final ThreadPool defaultThreadPool )
        {
            super( new NullConnectionMonitor(),
                   defaultThreadPool,
                   new DefaultAcceptorManager(),
                   false,
                   200 );
        }
    }

    public static class WithMonitor
        extends PicoConnectionManager
    {
        public WithMonitor( final ConnectionMonitor monitor,
                            final ThreadPool defaultThreadPool )
        {
            super( monitor,
                   defaultThreadPool,
                   new DefaultAcceptorManager(),
                   false,
                   200 );
        }
    }

    public static class WithMonitorAndConfig
        extends PicoConnectionManager
    {
        public WithMonitorAndConfig( final ConnectionMonitor monitor,
                                     final ThreadPool defaultThreadPool,
                                     final SocketAcceptorManager acceptorManager,
                                     final boolean forceShutdown,
                                     final int shutdownTimeout )
        {
            super( monitor, defaultThreadPool, acceptorManager, forceShutdown, shutdownTimeout );
        }
    }

    /**
     * Constructor.
     *
     * @param monitor the ConnectionMonitor
     * @param defaultThreadPool the ThreadPool used as default is not set in the
     *         {@link ConnectionManager#connect}
     * @param acceptorManager the AcceptorManager
     * @param forceShutdown boolean <code>true</code> if we need to force connections shutdown when they don't gracefully in specified time-period.
     * @param shutdownTimeout  the number of milliseconds to wait for connection to shutdown gracefully.
     */
    protected PicoConnectionManager( final ConnectionMonitor monitor,
                                     final ThreadPool defaultThreadPool,
                                     final SocketAcceptorManager acceptorManager,
                                     final boolean forceShutdown,
                                     final int shutdownTimeout )
    {
        setMonitor( monitor );
        setAcceptorManager( acceptorManager );
        setDefaultThreadPool( defaultThreadPool );
        setForceShutdown( forceShutdown );
        setShutdownTimeout( shutdownTimeout );
    }

    /**
     * Dispose the ConnectionManager which involves shutting down all
     * the connected acceptors.
     */
    public void dispose()
    {
        shutdownAcceptors();
    }
}
