/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netserve.connection.impl;

import java.net.ServerSocket;
import java.util.Hashtable;
import java.util.Map;
import org.codehaus.spice.netserve.connection.RequestHandler;
import org.codehaus.spice.netserve.connection.SocketAcceptorManager;
import org.codehaus.spice.netserve.connection.impl.AcceptorConfig;
import org.codehaus.spice.netserve.connection.impl.AcceptorMonitor;
import org.codehaus.spice.netserve.connection.impl.ConnectionAcceptor;

/**
 * Default implementation of SocketAcceptorManager that uses
 * a thread per acceptor approach.
 *
 * <p>Note that on some OS/JVM combinations <tt>soTimeout</tt> must
 * be set to non-0 value or else the ServerSocket will never get out
 * of accept() system call and we wont be able to shutdown the server
 * socket properly. However it can introduce performance problems if
 * constantly timing out. </p>
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @author Mauro Talevi
 * @version $Revision: 1.2 $ $Date: 2004-02-28 21:13:23 $
 */
public class DefaultAcceptorManager
    implements SocketAcceptorManager
{
    /**
     * The map of name->acceptor.
     */
    private final Map m_acceptors = new Hashtable();

    /**
     * The monitor that receives notifications of Connection events
     */
    private AcceptorMonitor m_monitor = NullAcceptorMonitor.MONITOR;

    /**
     * Value that we are to set SO_TIMEOUT to if the user
     * has not already set the timeout. Defaults to 1000 (1s timeout).
     */
    private int m_soTimeout = 1000;

    /**
     * Set to the number of milliseconds that we will wait
     * for a connection to shutdown gracefully. Defaults to 0
     * which indicates indefinite wait.
     */
    private int m_shutdownTimeout;

    /**
     * Set the AcceptorMonitor that receives events when changes occur.
     *
     * @param monitor the AcceptorMonitor that receives events when
     *        changes occur.
     */
    public void setMonitor( final AcceptorMonitor monitor )
    {
        m_monitor = monitor;
    }

    /**
     * Set the value that we are to set SO_TIMEOUT to if the user
     * has not already set the timeout. Defaults to 1000 (1s timeout).
     *
     * @param soTimeout the timeout value
     */
    public void setSoTimeout( final int soTimeout )
    {
        m_soTimeout = soTimeout;
    }

    /**
     * Set timeout for shutting down handlers.
     * The timeout defaults to 0 which means wait indefinetly.
     *
     * @param shutdownTimeout the timeout
     */
    public void setShutdownTimeout( final int shutdownTimeout )
    {
        m_shutdownTimeout = shutdownTimeout;
    }

    /**
     * Return the shutdownTimeout.
     *
     * @return the shutdownTimeout
     */
    protected int getShutdownTimeout()
    {
        return m_shutdownTimeout;
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
            disconnect( names[ i ] );
        }
    }

    /**
     * Start accepting connections from a socket and passing connections
     * to specified handler.
     *
     * @param name the name of connection. This serves as a key used to
     *        shutdown acceptor.
     * @param socket the ServerSocket from which connections are accepted
     * @throws java.lang.Exception if unable to initiate connection management. This could
     *         be due to the key already being used for another acceptor,
     *        the serversocket being closed, the handler being null etc.
     */
    public void connect( final String name,
                         final ServerSocket socket,
                         final RequestHandler handler )
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
        if( null == handler )
        {
            throw new NullPointerException( "handler" );
        }

        if( 0 == socket.getSoTimeout() )
        {
            socket.setSoTimeout( m_soTimeout );
        }

        final ConnectionAcceptor acceptor;
        synchronized( m_acceptors )
        {
            if( isConnected( name ) )
            {
                final String message =
                    "Connection already exists with name " + name;
                throw new IllegalArgumentException( message );
            }

            final AcceptorConfig config = new AcceptorConfig( name, socket, handler );
            acceptor = new ConnectionAcceptor( config, getMonitor() );
            m_acceptors.put( name, acceptor );
        }

        final Thread thread =
            new Thread( acceptor, "Acceptor[" + name + "]" );
        thread.start();
        while( !acceptor.hasStarted() )
        {
            Thread.sleep( 5 );
        }
    }

    /**
     * Return true if acceptor with specified name exists.
     *
     * @param name the name
     * @return true if acceptor with specified name exists.
     */
    public boolean isConnected( final String name )
    {
        return m_acceptors.containsKey( name );
    }

    /**
     * This shuts down the acceptor and the associated ServerSocket.
     *
     * @param name the name of connection
     * @throws java.lang.IllegalArgumentException if no connection with specified name
     */
    public void disconnect( final String name )
    {
        final ConnectionAcceptor acceptor =
            (ConnectionAcceptor)m_acceptors.remove( name );
        if( null == acceptor )
        {
            final String message = "No connection with name " + name;
            throw new IllegalArgumentException( message );
        }

        acceptor.close( getShutdownTimeout() );
    }

    /**
     * Return the monitor used by manager.
     *
     * @return the monitor used by manager.
     */
    protected AcceptorMonitor getMonitor()
    {
        return m_monitor;
    }
}
