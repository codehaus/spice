/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets.impl;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.jcomponent.netserve.sockets.SocketConnectionHandler;

/**
 * A helper class that manages acceptor for a single ServerSocket.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-08 05:02:10 $
 */
class ConnectionAcceptor
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
    private final SocketConnectionHandler m_handler;

    /**
     * The AcceptorMonitor for event notification.
     */
    private final AcceptorMonitor m_monitor;

    /**
     * The thread in which the main accept loop is running.
     * Setup at start of thread and set to null to shutdown
     * acceptor.
     */
    private Thread m_thread;

    /**
     * Create the acceptor.
     *
     * @param name the name that connection was registered using
     * @param serverSocket the ServerSocket that used to accept connections
     * @param handler the handler for new connections
     */
    ConnectionAcceptor( final String name,
                        final ServerSocket serverSocket,
                        final SocketConnectionHandler handler,
                        final AcceptorMonitor monitor )
    {
        if( null == name )
        {
            throw new NullPointerException( "name" );
        }
        if( null == serverSocket )
        {
            throw new NullPointerException( "serverSocket" );
        }
        if( null == handler )
        {
            throw new NullPointerException( "handler" );
        }
        if( null == monitor )
        {
            throw new NullPointerException( "monitor" );
        }
        m_name = name;
        m_serverSocket = serverSocket;
        m_handler = handler;
        m_monitor = monitor;
    }

    /**
     * Shutdown the acceptor.
     */
    void close()
    {
        synchronized( this )
        {
            if( null != m_thread )
            {
                m_monitor.acceptorClosed( m_name, m_serverSocket );
                m_thread.interrupt();
                m_thread = null;
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
            m_monitor.serverSocketListening( m_name, m_serverSocket );
            try
            {
                final Socket socket = m_serverSocket.accept();
                if( isRunning() )
                {
                    m_handler.handleConnection( socket );
                }
                else
                {
                    try
                    {
                        socket.close();
                    }
                    catch( final Exception e )
                    {
                        //Ignore
                    }
                }
            }
            catch( final InterruptedIOException iioe )
            {
                //Consume exception
            }
            catch( final IOException ioe )
            {
                m_monitor.errorAcceptingConnection( m_name, ioe );
            }
        }

        shutdownServerSocket();
    }

    /**
     * Utility method to shutdown serverSocket.
     */
    private synchronized void shutdownServerSocket()
    {
        try
        {
            m_serverSocket.close();
        }
        catch( final IOException ioe )
        {
            m_monitor.errorClosingServerSocket( m_name, ioe );
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
