/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netserve.connection.impl;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Monitor used to monitor events in the AcceptorManager.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-11-20 00:25:05 $
 */
public interface AcceptorMonitor
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
     * @param serverSocket the socket
     */
    void acceptorClosing( String name, ServerSocket serverSocket );

    /**
     * Listening for connection attempts in acceptor.
     *
     * @param name the acceptor name
     * @param serverSocket the socket
     */
    void serverSocketListening( String name, ServerSocket serverSocket );

    /**
     * There was an error accepting client connections.
     *
     * @param name the name of acceptor
     * @param ioe the exception
     */
    void errorAcceptingConnection( String name, IOException ioe );

    /**
     * There was an error closing server socket.
     *
     * @param name the name of acceptor
     * @param ioe the exception
     */
    void errorClosingServerSocket( String name, IOException ioe );
}
