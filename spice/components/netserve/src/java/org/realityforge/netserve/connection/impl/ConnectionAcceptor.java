/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.netserve.connection.impl;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.realityforge.netserve.connection.ConnectionHandler;
import org.realityforge.netserve.connection.ConnectionHandlerManager;
import org.realityforge.threadpool.ThreadPool;

/**
 * A helper class that manages acceptor for a single ServerSocket.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.18 $ $Date: 2003-04-23 09:12:21 $
 */
class ConnectionAcceptor
    extends AbstractLogEnabled
    implements Runnable
{
    /**
     * The name of the connection.
     */
    private final String m_name;

    /**
     * The ServerSocket that we are accepting connections from.
     */
    private final ServerSocket m_serverSocket;

    /**
     * The ConnectionHandlerManager that we create ConnectionHandlers from.
     */
    private final ConnectionHandlerManager m_handlerManager;

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
     * The thread in which the main accept loop is running.
     * Setup at start of thread and set to null to shutdown
     * acceptor.
     */
    private Thread m_thread;

    /**
     * The id of next handler created. Used to decorate the names of connections.
     */
    private long m_id;

    /**
     * True when acceptor is finished.
     */
    private boolean m_done;

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
        m_name = name;
        m_serverSocket = serverSocket;
        m_handlerManager = handlerManager;
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
        if( getLogger().isInfoEnabled() )
        {
            final String message = "Closing Acceptor " + m_name + ".";
            getLogger().info( message );
        }

        final ConnectionRunner[] runners;
        synchronized( this )
        {
            stopRunning( waitTime, forceShutdown );
            runners =
                (ConnectionRunner[])m_runners.toArray( new ConnectionRunner[ m_runners.size() ] );
        }
        for( int i = 0; i < runners.length; i++ )
        {
            runners[ i ].close( waitTime, forceShutdown );
        }
    }

    /**
     * The main accept & handle loop for acceptor.
     */
    public void run()
    {
        //Setup thread to indicate that we are currently running
        synchronized( this )
        {
            m_thread = Thread.currentThread();
        }

        while( isRunning() )
        {
            if( getLogger().isDebugEnabled() )
            {
                final String message =
                    "About to call accept() on ServerSocket '" + m_name + "'.";
                getLogger().debug( message );
            }
            try
            {
                final Socket socket = m_serverSocket.accept();
                if( !isRunning() )
                {
                    shutdown( socket );
                }
                else
                {
                    startConnection( socket );
                }
            }
            catch( final InterruptedIOException iioe )
            {
                //Consume exception
            }
            catch( final IOException ioe )
            {
                final String message =
                    "Exception accepting connection due to: " + ioe;
                getLogger().error( message, ioe );
            }
        }

        m_thread = null;
        shutdownServerSocket();

        synchronized( this )
        {
            notifyAll();
        }
    }

    /**
     * Utility method to shutdown serverSocket.
     */
    private void shutdownServerSocket()
    {
        if( getLogger().isDebugEnabled() )
        {
            final String message = "Closing ServerSocket " + m_name + ".";
            getLogger().debug( message );
        }
        m_done = true;
        try
        {
            m_serverSocket.close();
        }
        catch( final IOException ioe )
        {
            final String message =
                "Error closing ServerSocket on " + m_name + " due to: " + ioe;
            getLogger().error( message, ioe );
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
    void disposeRunner( final ConnectionRunner runner )
    {
        if( getLogger().isDebugEnabled() )
        {
            final String message = "Disposing runner " + runner + ".";
            getLogger().debug( message );
        }
        synchronized( this )
        {
            if( !m_runners.remove( runner ) )
            {
                final String message = "Attempting to dispose runner " +
                    runner + " that has already been disposed.";
                getLogger().warn( message );
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
                if( getLogger().isInfoEnabled() )
                {
                    final String message = "Error closing socket " + socket + ".";
                    getLogger().info( message, ioe );
                }
            }
        }
    }

    /**
     * Start a connection on specified socket.
     *
     * @param socket the socket
     */
    private void startConnection( final Socket socket )
    {
        final ConnectionHandler handler;
        try
        {
            handler = m_handlerManager.aquireHandler();
        }
        catch( final Exception e )
        {
            try
            {
                socket.close();
            }
            catch( IOException ioe )
            {
            }
            final String message =
                "Unable to create ConnectionHandler due to: " + e;
            getLogger().error( message, e );
            return;
        }

        final String name = m_name + m_id;
        m_id++;

        if( getLogger().isDebugEnabled() )
        {
            final String message = "Creating ConnectionRunner " + name + ".";
            getLogger().debug( message );
        }
        final ConnectionRunner runner = new ConnectionRunner( name, socket, handler, this );
        synchronized( this )
        {
            m_runners.add( runner );
        }
        setupLogger( runner );
        if( null != m_threadPool )
        {
            m_threadPool.execute( runner );
        }
        else
        {
            final Thread thread = new Thread( runner, name );
            thread.start();
        }
    }

    /**
     * Utility method that tells the acceptor to stop running.
     *
     * @param waitTime the time to wait for graceful shutdown
     * @param forceShutdown true if when we timeout we should forcefully
     *                      shutdown connection
     */
    private synchronized void stopRunning( final int waitTime,
                                           final boolean forceShutdown )
    {
        if( getLogger().isDebugEnabled() )
        {
            final String message = "Stopping Acceptor " + m_name + ".";
            getLogger().debug( message );
        }
        if( isRunning() )
        {
            m_done = true;
            final Thread thread = m_thread;
            m_thread = null;
            if( null != thread )
            {
                thread.interrupt();
            }

            if( !m_serverSocket.isClosed() )
            {
                try
                {
                    wait( waitTime );
                }
                catch( InterruptedException e )
                {
                }
            }

            if( forceShutdown && !m_serverSocket.isClosed() )
            {
                shutdownServerSocket();
            }
        }
    }

    /**
     * Return true if the acceptor is currently running.
     *
     * @return true if the acceptor is currently running.
     */
    private synchronized boolean isRunning()
    {
        return !m_done;
    }
}
