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
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.6 $ $Date: 2003-04-23 03:57:20 $
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

    void close( final int waitTime )
    {
        if( getLogger().isInfoEnabled() )
        {
            final String message = "Closing Acceptor " + m_name + ".";
            getLogger().info( message );
        }

        synchronized( this )
        {
            if( isRunning() )
            {
                stopRunning();
            }

            final ConnectionRunner[] runners =
                (ConnectionRunner[])m_runners.toArray( new ConnectionRunner[ m_runners.size() ] );
            for( int i = 0; i < runners.length; i++ )
            {
                runners[ i ].close( waitTime );
            }
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
            try
            {
                final Socket socket = m_serverSocket.accept();
                startConnection( socket );
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

        stopRunning();
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
        final ConnectionRunner runner = new ConnectionRunner( name, socket, handler, this );
        m_runners.add( runner );
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
        if( !m_runners.contains( runner ) )
        {
            final String message = "Attempting to dispose runner " +
                runner + " that has already been disposed.";
            getLogger().warn( message );
            return;
        }
        m_runners.remove( runner );
        m_handlerManager.releaseHandler( runner.getHandler() );
        final Socket socket = runner.getSocket();
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
     * Utility method that tells the acceptor to stop running.
     */
    private synchronized void stopRunning()
    {
        if( isRunning() )
        {
            final Thread thread = m_thread;
            m_thread = null;
            thread.interrupt();
        }
    }

    /**
     * Return true if the acceptor is currently running.
     *
     * @return true if the acceptor is currently running.
     */
    private synchronized boolean isRunning()
    {
        return null != m_thread;
    }
}
