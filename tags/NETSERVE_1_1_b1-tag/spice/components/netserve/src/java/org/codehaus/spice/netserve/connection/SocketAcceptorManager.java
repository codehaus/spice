/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netserve.connection;

import java.net.ServerSocket;
import org.codehaus.spice.netserve.connection.RequestHandler;

/**
 * This service is used to manage network acceptors.
 * The service takes a ServerSocket and RequestHandler and
 * anytime a new connection is accepted the handler is called
 * with the new socket connection.
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-03-21 23:43:00 $
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
     * @throws java.lang.Exception if unable to initiate connection management. This could
     *         be due to the key already being used for another acceptor,
     *        the serversocket being closed, the handler being null etc.
     */
    void connect( String name,
                  ServerSocket socket,
                  RequestHandler handler )
        throws Exception;

    /**
     * This shuts down the named acceptor.
     * NOTE: It is the responsibility of the caller to make
     * sure that the ServerSocket has been closed.
     *
     * @param name the name of connection
     * @throws java.lang.IllegalArgumentException if no connection with specified name
     */
    void disconnect( String name );

    /**
     * Return true if acceptor with specified name exists.
     *
     * @param name the name
     * @return true if acceptor with specified name exists.
     */
    boolean isConnected( String name );
}
