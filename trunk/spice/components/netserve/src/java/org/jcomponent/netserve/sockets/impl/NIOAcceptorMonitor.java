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

/**
 * Monitor used to monitor events in the AcceptorManager.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-10 02:38:35 $
 */
public interface NIOAcceptorMonitor
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
     * Selector being shutdown.
     */
    void selectorShutdown();

    /**
     * Error closing selector.
     *
     * @param ioe the exception
     */
    void errorClosingSelector( IOException ioe );

    /**
     * Exiting main loop that accepts connections.
     */
    void exitingSelectorLoop();

    /**
     * Entering select call.
     */
    void enteringSelect();

    /**
     * Select completed and resulted in count
     * accepts being present.
     *
     * @param count the number of accepts that are ready
     */
    void selectCompleted( int count );

    /**
     * Handle a connection.
     *
     * @param name the name of acceptor
     * @param serverSocket the server socket
     * @param socket the connecting socket
     */
    void handlingConnection( String name, ServerSocket serverSocket, Socket socket );
}
