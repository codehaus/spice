/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.impl;

import java.net.ServerSocket;
import java.util.Hashtable;
import java.util.Map;
import org.jcomponent.netserve.connection.ConnectionHandlerManager;
import org.jcomponent.netserve.connection.ConnectionManager;
import org.jcomponent.netserve.sockets.impl.DefaultAcceptorManager;
import org.jcomponent.threadpool.ThreadPool;

/**
 * Abstract implementation of {@link ConnectionManager}.  Concrete subclasses
 * will be container-specific.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @version $Revision: 1.6 $ $Date: 2003-10-10 04:18:38 $
 * @phoenix.component
 * @phoenix.service type="ConnectionManager"
 */
public abstract class AbstractConnectionManager
    implements ConnectionManager
{
    /**
     * The AcceptorManager
     */
    private final DefaultAcceptorManager m_acceptorManager = new DefaultAcceptorManager();

    /**
     * The map of name->acceptor.
     */
    private final Map m_acceptors = new Hashtable();

    /**
     * The monitor that receives notifications of Connection events
     */
    private ConnectionMonitor m_monitor;

    /**
     * The default ThreadPool dependency will be used to initiate
     * connections if clients do not specify threadPool. Is an
     * optional dependency and if not specified a new thread will
     * be instantiated as is needed.
     */
    private ThreadPool m_defaultThreadPool;

    /**
     * Set to the number of milliseconds that we will wait
     * for a connection to shutdown gracefully. Defaults to 0
     * which indicates indefinite wait.
     */
    private int m_shutdownTimeout;

    /**
     * Set to true if we need to force shutdown
     * connections if they don't shutdown gracefully
     * in specified time-period.
     */
    private boolean m_forceShutdown;

    /**
     * Set the ConnectionMonitor that receives events when changes occur.
     *
     * @param monitor the ConnectionMonitor that receives events when
     *        changes occur.
     */
    public void setMonitor( final ConnectionMonitor monitor )
    {
        m_monitor = monitor;
    }

    protected void setShutdownTimeout( final int shutdownTimeout )
    {
        m_shutdownTimeout = shutdownTimeout;
    }

    protected void setForceShutdown( final boolean forceShutdown )
    {
        m_forceShutdown = forceShutdown;
    }

    protected void setSoTimeout( final int soTimeout )
    {
        m_acceptorManager.setSoTimeout( soTimeout );
    }

    protected void setDefaultThreadPool( final ThreadPool threadPool )
    {
        m_defaultThreadPool = threadPool;
    }

    /**
     * Dispose the ConnectionManager which involves shutting down all
     * the connected acceptors.
     */
    public void shutdownAcceptors()
    {
        final String[] names;
        synchronized( m_acceptors )
        {
            names = (String[])m_acceptors.keySet().toArray( new String[ 0 ] );
        }
        for( int i = 0; i < names.length; i++ )
        {
            disconnect( names[ i ], true );
        }
    }

    /**
     * Start managing a connection. Once a connection is managed by
     * this service it will accept connections from ServerSocket and pass
     * the connections to the ConnectionHandlers. The ConnectionHandlers
     * will be called in a Thread aquired from the specified ThreadPool.
     *
     * @param name the name of connection. This serves as a key
     * @param socket the ServerSocket from which connections are accepted
     * @param handlerManager the manager from which to aquire handlers and
     *                       release them afterwards
     * @param threadPool the threadPool that threads are aquired from to handle
     *                   the connections.
     * @throws Exception if unable to initiate connection management. This could
     *                   be due to the key already being used for another connection
     *                   the serversocket being closed, the handlerManager being null etc.
     */
    public void connect( String name,
                         ServerSocket socket,
                         ConnectionHandlerManager handlerManager,
                         ThreadPool threadPool )
        throws Exception
    {
        doConnect( name, socket, handlerManager, threadPool );
    }

    /**
     * Start managing a connection. Once a connection is managed by
     * this service it will accept connections from ServerSocket and pass
     * the connections to the ConnectionHandlers. This method may use an
     * underlying "common" ThreadPool or may create threads to handle
     * connections. The exact workings is an implementation detail.
     *
     * @param name the name of connection. This serves as a key
     * @param socket the ServerSocket from which connections are accepted
     * @param handlerManager the manager from which to aquire handlers and
     *                       release them afterwards
     * @throws Exception if unable to initiate connection management. This could
     *                   be due to the key already being used for another connection
     *                   the serversocket being closed, the handlerManager being null etc.
     */
    public void connect( String name,
                         ServerSocket socket,
                         ConnectionHandlerManager handlerManager )
        throws Exception
    {
        doConnect( name, socket, handlerManager, m_defaultThreadPool );
    }

    /**
     * This shuts down all handlers and the associated ServerSocket.
     * If tearDown is true then it will forcefully shutdown all connections and try
     * to return as soon as possible. Otherwise it will wait for each handler
     * to gracefully shutdown.
     *
     * @param name the name of connection
     * @param tearDown if true will forcefully tear down all handlers
     */
    public void disconnect( final String name, final boolean tearDown )
    {
        final ConnectionAcceptor acceptor = (ConnectionAcceptor)m_acceptors.remove( name );
        if( null == acceptor )
        {
            final String message = "No connection with name " + name;
            throw new IllegalArgumentException( message );
        }

        m_monitor.acceptorDisconnecting( name, tearDown );

        if( !tearDown )
        {
            acceptor.close( 0, m_forceShutdown );
        }
        else
        {
            acceptor.close( m_shutdownTimeout, m_forceShutdown );
        }
    }

    /**
     * Actually perform the establishment of acceptor.
     *
     * @param name the name of connection
     * @param socket the serversocket we accept connections from
     * @param handlerManager the manager
     * @param threadPool the threadPool to use (may be null)
     * @throws Exception if unable to setup socket properly
     */
    private void doConnect( final String name,
                            final ServerSocket socket,
                            final ConnectionHandlerManager handlerManager,
                            final ThreadPool threadPool )
        throws Exception
    {
        synchronized( m_acceptors )
        {
            final ConnectionAcceptor acceptor =
                new ConnectionAcceptor( name, socket, handlerManager, m_monitor, threadPool );
            m_acceptorManager.connect( name, socket, acceptor );
            m_acceptors.put( name, acceptor );
        }
    }
}
