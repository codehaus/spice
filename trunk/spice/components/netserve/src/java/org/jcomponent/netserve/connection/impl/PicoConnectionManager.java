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
 *  <li>forceShutdown=true -- forcefully shutdown connections if they don't shutdown gracefully --</li>
 *  <li>shutdownTimeout=200 -- wait 200ms for connections to gracefully shutdown --</li>
 * </ul>
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @version $Revision: 1.3 $ $Date: 2003-10-14 04:19:49 $
 */
public class PicoConnectionManager
    extends AbstractConnectionManager
    implements ConnectionManager
{
    /**
     * Create component but only specify the default thread pool.
     *
     * @param defaultThreadPool the default thread pool
     */
    public PicoConnectionManager( final ThreadPool defaultThreadPool )
    {
        this( new NullConnectionMonitor(),
              defaultThreadPool,
              new DefaultAcceptorManager(),
              false,
              200 );
    }

    /**
     * Create ConnectionManager specifying monitor and thread pool.
     *
     * @param monitor the monitor
     * @param defaultThreadPool the default thread pool
     */
    public PicoConnectionManager( final ConnectionMonitor monitor,
                                  final ThreadPool defaultThreadPool )
    {
        this( monitor,
              defaultThreadPool,
              new DefaultAcceptorManager(),
              false,
              200 );
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
    public PicoConnectionManager( final ConnectionMonitor monitor,
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
