/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;
import org.jcomponent.netserve.connection.ConnectionHandler;
import org.jcomponent.netserve.connection.ConnectionHandlerManager;
import org.jcomponent.threadpool.ThreadPool;

/**
 * A helper class that manages acceptor for a single ServerSocket.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.8 $ $Date: 2003-10-24 08:05:22 $
 */
class ConnectionAcceptor
    implements ConnectionHandler
{
    /**
     * The name of the connection.
     */
    private final String m_name;

    /**
     * The ConnectionHandlerManager that we create ConnectionHandlers from.
     */
    private final ConnectionHandlerManager m_handlerManager;

    /**
     * The ConnectionMonitor for event notification.
     */
    private final ConnectionMonitor m_monitor;

    /**
     * The thread pool we use to create threads to handle connections.
     * If null we will create a new thread at time of connection.
     */
    private final ThreadPool m_threadPool;

    /**
     * A map that maps ConnectionRunner to Socket that the runner is handling.
     */
    private final List m_runners = new Vector();

    /**
     * The id of next handler created. Used to decorate the names of connections.
     */
    private long m_id;

    /**
     * Create the acceptor.
     *
     * @param name the name that connection was registered using
     * @param serverSocket the ServerSocket that used to accept connections
     * @param handlerManager the handler manager to aquire/release handlers to/from
     * @param threadPool the threadPool that acceptor associated with (may be null)
     */
    ConnectionAcceptor( final String name,
                        final ServerSocket serverSocket,
                        final ConnectionHandlerManager handlerManager,
                        final ConnectionMonitor monitor,
                        final ThreadPool threadPool )
    {
        if( null == name )
        {
            throw new NullPointerException( "name" );
        }
        if( null == serverSocket )
        {
            throw new NullPointerException( "serverSocket" );
        }
        if( null == handlerManager )
        {
            throw new NullPointerException( "handlerManager" );
        }
        if( null == monitor )
        {
            throw new NullPointerException( "monitor" );
        }
        m_name = name;
        m_handlerManager = handlerManager;
        m_monitor = monitor;
        m_threadPool = threadPool;
    }

    /**
     * Shutdown the acceptor and any active handlers.
     * Wait specified waittime for handlers to gracefully shutdown.
     *
     * @param waitTime the time to wait for graceful shutdown
     * @param forceShutdown true if when we timeout we should forcefully
     *                      shutdown connection
     */
    void close( final int waitTime, final boolean forceShutdown )
    {
        final ConnectionRunner[] runners;
        synchronized( this )
        {
            runners =
                (ConnectionRunner[])m_runners.toArray( new ConnectionRunner[ m_runners.size() ] );
        }
        for( int i = 0; i < runners.length; i++ )
        {
            runners[ i ].close( waitTime, forceShutdown );
        }
    }

    /**
     * Overide toString method to make it easier to debug acceptor.
     *
     * @return the decorated name of acceptor
     */
    public String toString()
    {
        return "ConnectionAcceptor[" + m_name + "]";
    }

    /**
     * Dispose of a ConnectionRunner.
     * This should only be called by the ConnectionRunner unless
     * the ConnectionRunner will not shutdown in which case this may be called to
     * tear down the connection.
     *
     * @param runner the ConnectionRunner
     */
    void disposeRunner( final ConnectionRunner runner,
                        final String name )
    {
        m_monitor.disposingHandler( name, runner.getSocket() );
        synchronized( this )
        {
            if( !m_runners.remove( runner ) )
            {
                m_monitor.errorHandlerAlreadyDisposed( name, runner.getSocket() );
                return;
            }
        }

        m_handlerManager.releaseHandler( runner.getHandler() );
        shutdown( runner.getSocket() );
    }

    /**
     * Close the socket if it is open.
     *
     * @param socket the socket to close
     */
    private void shutdown( final Socket socket )
    {
        if( !socket.isClosed() )
        {
            try
            {
                socket.close();
            }
            catch( final IOException ioe )
            {
                m_monitor.errorClosingSocket( m_name, socket, ioe );
            }
        }
    }

    /**
     * Handle a socket on specified socket.
     *
     * @param socket the socket
     */
    public void handleConnection( final Socket socket )
    {
        final ConnectionHandler handler;
        try
        {
            handler = m_handlerManager.aquireHandler();
        }
        catch( final Exception e )
        {
            shutdown( socket );
            m_monitor.errorAquiringHandler( m_name, e );
            return;
        }

        final String name = m_name + m_id;
        m_id++;

        final ConnectionRunner runner =
            new ConnectionRunner( name, socket, handler, this, m_monitor );
        synchronized( this )
        {
            m_runners.add( runner );
        }
        if( null != m_threadPool )
        {
            m_threadPool.execute( runner );
        }
        else
        {
            final Thread thread = new Thread( runner, toString() );
            thread.start();
        }
    }
}
