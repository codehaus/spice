/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets.impl;

import java.net.ServerSocket;
import org.jcomponent.netserve.sockets.SocketConnectionHandler;

/**
 * A utility class that contains acceptor configuration.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-09 05:51:08 $
 */
class AcceptorConfig
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
     * Create the acceptor.
     *
     * @param name the name that connection was registered using
     * @param serverSocket the ServerSocket that used to accept connections
     * @param handler the handler for new connections
     */
    AcceptorConfig( final String name,
                        final ServerSocket serverSocket,
                        final SocketConnectionHandler handler )
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
        m_name = name;
        m_serverSocket = serverSocket;
        m_handler = handler;
    }

    /**
     * Return the name acceptor registered under.
     *
     * @return the name acceptor registered under.
     */
    String getName()
    {
        return m_name;
    }

    /**
     * Return the socket that connections accepted from.
     *
     * @return the socket that connections accepted from.
     */
    ServerSocket getServerSocket()
    {
        return m_serverSocket;
    }

    /**
     * Return the handler the connections are handled by.
     *
     * @return the handler the connections are handled by.
     */
    SocketConnectionHandler getHandler()
    {
        return m_handler;
    }
}
