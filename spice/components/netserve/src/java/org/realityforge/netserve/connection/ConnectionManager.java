/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.netserve.connection;

import java.net.ServerSocket;
import org.realityforge.threadpool.ThreadPool;

/**
 * @author <a href="mailto:donaldp@apache.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-23 01:45:04 $
 */
public interface ConnectionManager
{
    String ROLE = ConnectionManager.class.getName();

    void connect( String name,
                  ServerSocket socket,
                  ConnectionHandlerManager handlerFactory,
                  ThreadPool threadPool )
        throws Exception;

    void connect( String name,
                  ServerSocket socket,
                  ConnectionHandlerManager handlerFactory )
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
