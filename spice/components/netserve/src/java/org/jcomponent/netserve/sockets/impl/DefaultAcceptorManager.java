/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets.impl;

import java.net.ServerSocket;
import java.util.Hashtable;
import java.util.Map;
import org.jcomponent.netserve.sockets.SocketAcceptorManager;
import org.jcomponent.netserve.sockets.SocketConnectionHandler;

/**
 * Abstract implementation of {@link SocketAcceptorManager} that uses
 * a thread per acceptor approach.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @version $Revision: 1.2 $ $Date: 2003-10-08 07:06:37 $
 * @dna.component
 * @dna.service type="ConnectionManager"
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
    private AcceptorMonitor m_monitor;

    /**
     * Value that we are to set SO_TIMEOUT to if the user
     * has not already set the timeout. Defaults to 1000 (1s timeout).
     */
    private int m_soTimeout = 1000;

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
     * @throws Exception if unable to initiate connection management. This could
     *         be due to the key already being used for another acceptor,
     *        the serversocket being closed, the handler being null etc.
     */
    public void connect( final String name,
                         final ServerSocket socket,
                         final SocketConnectionHandler handler )
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
            if( m_acceptors.containsKey( name ) )
            {
                final String message =
                    "Connection already exists with name " + name;
                throw new IllegalArgumentException( message );
            }

            acceptor =
                new ConnectionAcceptor( name, socket, handler, m_monitor );
            m_acceptors.put( name, acceptor );
        }

        final Thread thread =
            new Thread( acceptor, "Acceptor[" + name + "]" );
        thread.start();
    }

    /**
     * This shuts down all handlers and the associated ServerSocket.
     * If tearDown is true then it will forcefully shutdown all connections and try
     * to return as soon as possible. Otherwise it will wait for each handler
     * to gracefully shutdown.
     *
     * @param name the name of connection
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

        acceptor.close();
    }
}
