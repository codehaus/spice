/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.netserve.connection.impl;

import java.net.ServerSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.realityforge.netserve.connection.ConnectionHandlerManager;
import org.realityforge.netserve.connection.ConnectionManager;
import org.realityforge.threadpool.ThreadPool;

/**
 * This is the service through which ConnectionManagement occurs.
 *
 * @author <a href="mailto:donaldp@apache.org">Peter Donald</a>
 */
public class DefaultConnectionManager
    extends AbstractLogEnabled
    implements ConnectionManager, Serviceable, Disposable
{
    private final Map m_connections = new HashMap();
    private ThreadPool m_threadpool;

    public void service( final ServiceManager manager )
        throws ServiceException
    {
        if( manager.hasService( ThreadPool.ROLE ) )
        {
            m_threadpool = (ThreadPool)manager.lookup( ThreadPool.ROLE );
        }
    }

    public void dispose()
    {
        final String[] names = (String[])m_connections.keySet().toArray( new String[ 0 ] );
        for( int i = 0; i < names.length; i++ )
        {
            try
            {
                disconnect( names[ i ], true );
            }
            catch( final Exception e )
            {
                getLogger().warn( "Error disconnecting " + names[ i ], e );
            }
        }
    }

    /**
     * Start managing a connection.
     * Management involves accepting connections and farming them out to threads
     * from pool to be handled.
     *
     * @param name the name of connection
     * @param socket the ServerSocket from which to
     * @param handlerFactory the factory from which to aquire handlers
     * @param threadPool the thread pool to use
     * @exception Exception if an error occurs
     */
    public void connect( String name,
                         ServerSocket socket,
                         ConnectionHandlerManager handlerFactory,
                         ThreadPool threadPool )
        throws Exception
    {
        doConnect( name, socket, handlerFactory, threadPool );
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
        doConnect( name, socket, handlerManager, m_threadpool );
    }

    /**
     * This shuts down all handlers and the associated ServerSocket.
     * If tearDown is true then it will forcefully shutdown all connections and try
     * to return as soon as possible. Otherwise it will wait for each handler
     * to gracefully shutdown.
     *
     * @param name the name of connection
     * @param tearDown if true will forcefully tear down all handlers
     * @exception Exception if error occurs shutting down a handler or connection.
     *                      Note that shutdown of all handlers will be attempted
     *                      even if the first handler fails to shutdown gracefully.
     */
    public void disconnect( final String name, final boolean tearDown )
        throws Exception
    {
        final ConnectionAcceptor acceptor = (ConnectionAcceptor)m_connections.remove( name );
        if( null == acceptor )
        {
            final String message = "No connection with name " + name;
            throw new IllegalArgumentException( message );
        }

        if( !tearDown )
        {
            acceptor.close( 0 );
        }
        else
        {
            //TODO: Stop ignoring tearDown
            acceptor.close( 0 );
        }
    }

    private void doConnect( final String name,
                            final ServerSocket socket,
                            final ConnectionHandlerManager handlerFactory,
                            final ThreadPool threadPool )
        throws SocketException
    {
        if( null != m_connections.get( name ) )
        {
            throw new IllegalArgumentException( "Connection already exists with name " +
                                                name );
        }

        socket.setSoTimeout( 500 );

        final ConnectionAcceptor runner = new ConnectionAcceptor( name, socket, handlerFactory, threadPool );
        setupLogger( runner );
        m_connections.put( name, runner );

        if( null != threadPool )
        {
            threadPool.execute( runner );
        }
        else
        {
            final Thread thread = new Thread( runner, runner.toString() );
            thread.start();
        }
    }
}
