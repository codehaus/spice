/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets.impl;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

import org.jcomponent.netserve.selector.SelectorMonitor;

/**
 * Monitor used to monitor events in the AcceptorManager.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-10-23 06:34:19 $
 */
public interface NIOAcceptorMonitor
   extends SelectorMonitor
{
    /**
     * Aceptor create with name for specified socket.
     *
     * @param name the acceptor name
     * @param serverSocket the socket
     */
    void acceptorCreated( String name, ServerSocket serverSocket );

    /**
     * About to close down acceptor and stop listening for
     * connections.
     *
     * @param name the acceptor name
     */
    void acceptorClosing( String name );

    /**
     * There was an error accepting client connections.
     *
     * @param name the name of acceptor
     * @param ioe the exception
     */
    void errorAcceptingConnection( String name, IOException ioe );

    /**
     * Handle a connection.
     *
     * @param name the name of acceptor
     * @param serverSocket the server socket
     * @param socket the connecting socket
     */
    void handlingConnection( String name, ServerSocket serverSocket, Socket socket );
}
