/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection;

import java.net.ServerSocket;
import org.jcomponent.threadpool.ThreadPool;

/**
 * This service is used to manage serverside network acceptors.
 * To establish a connection the service is provided with a
 * ServerSocket and a ConnectionHandlerManager. The service will start
 * accepting connections to ServerSocket and then pass the accepted socket
 * to ConnectionHandler instances to handle the connection.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-09-02 04:14:13 $
 */
public interface ConnectionManager
{
    /**
     * Start managing a connection. Once a connection is managed by
     * this service it will accept connections from ServerSocket and pass
     * the connections to the ConnectionHandlers. The ConnectionHandlers
     * will be called in a Thread aquired from the specified ThreadPool.
     *
     * @param name the name of connection. This serves as a key
     * @param socket the ServerSocket from which connections are accepted
     * @param handlerManager the manager from which to aquire handlers and
     *                       release them afterwards
     * @param threadPool the threadPool that threads are aquired from to handle
     *                   the connections.
     * @throws Exception if unable to initiate connection management. This could
     *                   be due to the key already being used for another connection
     *                   the serversocket being closed, the handlerManager being null etc.
     */
    void connect( String name,
                  ServerSocket socket,
                  ConnectionHandlerManager handlerManager,
                  ThreadPool threadPool )
        throws Exception;

    /**
     * Start managing a connection. Once a connection is managed by
     * this service it will accept connections from ServerSocket and pass
     * the connections to the ConnectionHandlers. This method may use an
     * underlying "common" ThreadPool or may create threads to handle
     * connections. The exact workings is an implementation detail.
     *
     * @param name the name of connection. This serves as a key
     * @param socket the ServerSocket from which connections are accepted
     * @param handlerManager the manager from which to aquire handlers and
     *                       release them afterwards
     * @throws Exception if unable to initiate connection management. This could
     *                   be due to the key already being used for another connection
     *                   the serversocket being closed, the handlerManager being null etc.
     */
    void connect( String name,
                  ServerSocket socket,
                  ConnectionHandlerManager handlerManager )
        throws Exception;

    /**
     * This shuts down all handlers and the associated ServerSocket.
     * If tearDown is true then it will forcefully shutdown all connections and try
     * to return as soon as possible. Otherwise it will wait for each handler
     * to gracefully shutdown.
     *
     * @param name the name of connection
     * @param tearDown if true will forcefully tear down all handlers
     * @exception Exception if error occurs shutting down a handler or connection.
     *                      Note that shutdown of all handlers will be attempted
     *                      even if the first handler fails to shutdown gracefully.
     */
    void disconnect( String name, boolean tearDown )
        throws Exception;
}
