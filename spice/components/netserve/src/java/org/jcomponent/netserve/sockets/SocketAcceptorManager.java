/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets;

import java.net.ServerSocket;

/**
 * This service is used to manage network acceptors.
 * The service takes a ServerSocket and ConnectionHandler and
 * anytime a new connection is accepted the handler is called
 * with the new socket connection.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-10-09 07:06:50 $
 */
public interface SocketAcceptorManager
{
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
    void connect( String name,
                  ServerSocket socket,
                  SocketConnectionHandler handler )
        throws Exception;

    /**
     * This shuts down the named acceptor.
     * NOTE: It is the responsibility of the caller to make
     * sure that the ServerSocket has been closed.
     *
     * @param name the name of connection
     * @throws Exception if error occurs shutting down a handler or connection.
     */
    void disconnect( String name )
        throws Exception;

    /**
     * Return true if acceptor with specified name exists.
     *
     * @param name the name
     * @return true if acceptor with specified name exists.
     */
    boolean isConnected( String name );
}
