/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netserve.connection.impl;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;

import org.codehaus.spice.netserve.connection.impl.AcceptorConfig;
import org.codehaus.spice.netserve.connection.impl.AcceptorMonitor;

/**
 * A helper class that manages acceptor for a single ServerSocket.
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-03-21 23:42:59 $
 */
class ConnectionAcceptor
    implements Runnable
{
    /**
     * The configuration for acceptor.
     */
    private final AcceptorConfig m_config;

    /**
     * The AcceptorMonitor for event notification.
     */
    private final AcceptorMonitor m_monitor;

    private boolean m_started;

    private boolean m_active;

    /**
     * The thread in which the main accept loop is running.
     * Setup at start of thread and set to null to shutdown
     * acceptor.
     */
    private Thread m_thread;

    /**
     * Create the acceptor.
     *
     * @param config the config for acceptor
     * @param monitor the monitor
     */
    ConnectionAcceptor( final AcceptorConfig config,
                        final AcceptorMonitor monitor )
    {
        if( null == config )
        {
            throw new NullPointerException( "config" );
        }
        if( null == monitor )
        {
            throw new NullPointerException( "monitor" );
        }
        m_config = config;
        m_monitor = monitor;
        m_monitor.acceptorCreated( m_config.getName(),
                                   m_config.getServerSocket() );
    }

    /**
     * Return true if acceptor has started.
     *
     * @return true if acceptor has started.
     */
    synchronized boolean hasStarted()
    {
        return m_started;
    }

    /**
     * Shutdown the acceptor.
     */
    void close( final long timeout )
    {
        synchronized( this )
        {
            m_active = false;
            m_monitor.acceptorClosing( m_config.getName(),
                                       m_config.getServerSocket() );
            m_thread.interrupt();
            try
            {
                wait( timeout );
            }
            catch( InterruptedException e )
            {
                //ignore
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
            m_started = true;
            m_active = true;
            m_thread = Thread.currentThread();
        }
        while( isRunning() )
        {
            m_monitor.serverSocketListening( m_config.getName(),
                                             m_config.getServerSocket() );
            try
            {
                final Socket socket = m_config.getServerSocket().accept();
                if( isRunning() )
                {
                    m_config.getHandler().handleConnection( socket );
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
                m_monitor.errorAcceptingConnection( m_config.getName(), ioe );
            }
        }

        shutdownServerSocket();
        synchronized( this )
        {
            m_thread = null;
            notifyAll();
        }
    }

    /**
     * Utility method to shutdown serverSocket.
     */
    void shutdownServerSocket()
    {
        try
        {
            m_config.getServerSocket().close();
        }
        catch( final IOException ioe )
        {
            m_monitor.errorClosingServerSocket( m_config.getName(), ioe );
        }
    }

    /**
     * Return true if the acceptor is currently running.
     *
     * @return true if the acceptor is currently running.
     */
    synchronized boolean isRunning()
    {
        return m_active;
    }
}
