/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.netserve.connection.impl;

import java.net.ServerSocket;
import java.util.Hashtable;
import java.util.Map;
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.realityforge.netserve.connection.ConnectionHandlerManager;
import org.realityforge.netserve.connection.ConnectionManager;
import org.realityforge.threadpool.ThreadPool;

/**
 * This component is used to manage serverside network acceptors.
 * To establish a connection the service is provided with a
 * ServerSocket and a ConnectionHandlerManager. The service will start
 * accepting connections to ServerSocket and then pass the accepted socket
 * to ConnectionHandler instances to handle the connection.
 *
 * <p>A sample configuration for the component is below. Note that on some OS/JVM
 * combinations <tt>soTimeout</tt> must be set to non-0 value or else the ServerSocket will
 * never get out of accept() system call and we wont be able to shutdown the server
 * socket properly. However it can introduce performance problems if constantly
 * timing out. <tt>shutdownTimeout</tt> indicates how long we should wait to see if
 * incoming connections will shutdown gracefully when asked. If they dont shutdown
 * gracefully and <tt>forceShutdown</tt> is true then the connection will be forced
 * to be shutdown if the user asked for connection to be "tearedDown".</p>
 * <pre>
 *  &lt;soTimeout&gt;500&lt;/soTimeout&gt; &lt;!-- 500 ms timeouts on Server Sockets --&gt;
 *  &lt;forceShutdown&gt;true&lt;/forceShutdown&gt; &lt;!-- forcefully shutdown connections
 *                                           if they dont shutdown gracefully --&gt;
 *  &lt;shutdownTimeout&gt;200&lt;/shutdownTimeout&gt; &lt;!-- wait 200ms for connections to gracefully
 *                                              shutdown --&gt;
 * </pre>
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.10 $ $Date: 2003-04-23 10:28:57 $
 * @phoenix.component
 * @phoenix.service type="ConnectionManager"
 */
public class DefaultConnectionManager
    extends AbstractLogEnabled
    implements ConnectionManager, Serviceable, Configurable, Disposable
{
    /**
     * The map of name->acceptor.
     */
    private final Map m_acceptors = new Hashtable();

    /**
     * The ThreadPool dependency that will be used to initiate
     * connections if clients do not specify threadPool. Is an
     * optional dependency and if not specified a new thread will
     * be instantiated as is needed.
     */
    private ThreadPool m_threadpool;

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
     * Value that we are to set SO_TIMEOUT to if the user
     * has not already set the timeout. Defaults to 0 (inifinite timeout).
     */
    private int m_soTimeout;

    /**
     * Get ThreadPool service if present.
     *
     * @param manager the manager to retrieve services from
     * @throws ServiceException if unable to aquire ThreadPool
     * @phoenix.dependency type="ThreadPool" optional="true"
     */
    public void service( final ServiceManager manager )
        throws ServiceException
    {
        if( manager.hasService( ThreadPool.ROLE ) )
        {
            m_threadpool = (ThreadPool)manager.lookup( ThreadPool.ROLE );
        }
    }

    /**
     * Configure the ConnectionManager.
     *
     * @param configuration the configuration
     * @throws ConfigurationException if error reading configuration
     * @phoenix.configuration type="http://relaxng.org/ns/structure/1.0"
     *    location="DefaultConnectionManager-schema.xml"
     */
    public void configure( final Configuration configuration )
        throws ConfigurationException
    {
        m_soTimeout = configuration.getChild( "soTimeout" ).getValueAsInteger( 0 );
        m_forceShutdown = configuration.getChild( "forceShutdown" ).getValueAsBoolean( false );
        m_shutdownTimeout = configuration.getChild( "shutdownTimeout" ).getValueAsInteger( 0 );
    }

    /**
     * Dispose the ConnectionManager which involves shutting down all
     * the connected acceptors.
     */
    public void dispose()
    {
        final String[] names;
        synchronized( m_acceptors )
        {
            names = (String[])m_acceptors.keySet().toArray( new String[ 0 ] );
        }

        for( int i = 0; i < names.length; i++ )
        {
            final String name = names[ i ];
            try
            {
                disconnect( name, true );
            }
            catch( final Exception e )
            {
                final String message =
                    "Error disconnecting " + name + "due to: " + e;
                getLogger().warn( message, e );
            }
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
        final ConnectionAcceptor acceptor = (ConnectionAcceptor)m_acceptors.remove( name );
        if( null == acceptor )
        {
            final String message = "No connection with name " + name;
            throw new IllegalArgumentException( message );
        }

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
        if( null == name )
        {
            throw new NullPointerException( "name" );
        }
        if( null == socket )
        {
            throw new NullPointerException( "socket" );
        }
        if( null == handlerManager )
        {
            throw new NullPointerException( "handlerManager" );
        }

        if( 0 == socket.getSoTimeout() )
        {
            socket.setSoTimeout( m_soTimeout );
        }

        final ConnectionAcceptor runner = new ConnectionAcceptor( name, socket, handlerManager, threadPool );
        setupLogger( runner );

        synchronized( m_acceptors )
        {
            if( null != m_acceptors.get( name ) )
            {
                final String message =
                    "Connection already exists with name " + name;
                throw new IllegalArgumentException( message );
            }

            m_acceptors.put( name, runner );
        }

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
