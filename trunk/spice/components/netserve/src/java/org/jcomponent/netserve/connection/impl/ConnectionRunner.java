/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.impl;

import java.net.Socket;
import org.jcomponent.netserve.connection.ConnectionHandler;

/**
 * This class is responsible for handling a single connection.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.4 $ $Date: 2003-10-09 05:30:00 $
 */
class ConnectionRunner
    implements Runnable
{
    /**
     * The name of the connection. Uniquely identifies
     * connection but only used for debugging purposes.
     */
    private final String m_name;

    /**
     * The socket that is being handled.
     */
    private final Socket m_socket;

    /**
     * The handler that will be run on the socket.
     */
    private final ConnectionHandler m_handler;

    /**
     * This is the acceptor that initially spawned the runner.
     * Used by runner to dispose of its resources when handling is done.
     */
    private final ConnectionAcceptor m_acceptor;

    /**
     * The ConnectionMonitor for event notification.
     */
    private final ConnectionMonitor m_monitor;

    /**
     * The thread that this runner is operating in. Will be set to null
     * when runner is done.
     */
    private Thread m_thread;

    /**
     * The flag set to true when connection is handled.
     */
    private boolean m_done;

    /**
     * Create a ConnectionRunner.
     *
     * @param name the name of the runner
     * @param socket the socket
     * @param handler the handler that will do the handling
     * @param acceptor the acceptor that created the runner
     */
    ConnectionRunner( final String name,
                      final Socket socket,
                      final ConnectionHandler handler,
                      final ConnectionAcceptor acceptor,
                      final ConnectionMonitor monitor )
    {
        if( null == name )
        {
            throw new NullPointerException( "name" );
        }
        if( null == socket )
        {
            throw new NullPointerException( "socket" );
        }
        if( null == handler )
        {
            throw new NullPointerException( "handler" );
        }
        if( null == acceptor )
        {
            throw new NullPointerException( "acceptor" );
        }
        if( null == monitor )
        {
            throw new NullPointerException( "monitor" );
        }
        m_name = name;
        m_socket = socket;
        m_acceptor = acceptor;
        m_handler = handler;
        m_monitor = monitor;
    }

    /**
     * Attempt to close the runner. The runner will wait upto
     * the specified waitTime to allow the runner the chance to gracefully
     * shutdown the connection.
     *
     * @param waitTime the time to wait when attempting to gracefully
     *                 shutdown connection. 0 means wait indefinetly
     * @param forceShutdown true if when we timeout we should forcefully
     *                      shutdown connection
     */
    synchronized void close( final int waitTime, final boolean forceShutdown )
    {
        if( !m_done )
        {
            if( null != m_thread )
            {
                m_thread.interrupt();
            }

            //wait "waitTime" or until m_done is true
            try
            {
                wait( waitTime );
            }
            catch( InterruptedException e )
            {
            }

            //Do we need to forcefully shut the conenction down?
            if( !m_done && forceShutdown )
            {
                shutdown();
            }
        }
    }

    /**
     * Actually handle the connection
     */
    public void run()
    {
        synchronized( this )
        {
            m_thread = Thread.currentThread();
        }
        try
        {
            m_monitor.connectionStarting( m_name, m_socket );
            m_handler.handleConnection( m_socket );
            m_monitor.connectionEnding( m_name, m_socket );
        }
        catch( final Exception e )
        {
            m_monitor.errorHandlingConnection( m_name, m_socket, e );
        }
        finally
        {
            shutdown();
            synchronized( this )
            {
                notifyAll();
            }
        }
    }

    /**
     * Return the socket associated with runner.
     *
     * @return the socket associated with runner.
     */
    Socket getSocket()
    {
        return m_socket;
    }

    /**
     * Return the handler associated with runner.
     *
     * @return the handler associated with runner
     */
    ConnectionHandler getHandler()
    {
        return m_handler;
    }

    /**
     * Overide toString to return name of connection.
     *
     * @return the name of connection
     */
    public String toString()
    {
        return m_name;
    }

    /**
     * Shutdown runner.
     */
    private synchronized void shutdown()
    {
        m_done = true;
        m_thread = null;
        m_acceptor.disposeRunner( this, m_name );
    }
}
