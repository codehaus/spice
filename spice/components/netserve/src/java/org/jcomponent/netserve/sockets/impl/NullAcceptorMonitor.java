/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets.impl;

import java.net.ServerSocket;
import java.io.IOException;

/**
 * A noop monitor.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-10-10 02:41:12 $
 */
public class NullAcceptorMonitor
    implements AcceptorMonitor
{
    /**
     * Add constant for instance of Null Acceptor.
     */
    public static final NullAcceptorMonitor MONITOR = new NullAcceptorMonitor();

    /**
     * @see AcceptorMonitor#acceptorCreated
     */
    public void acceptorCreated( String name, ServerSocket serverSocket )
    {
    }

    /**
     * @see AcceptorMonitor#acceptorClosing
     */
    public void acceptorClosing( String name, ServerSocket serverSocket )
    {
    }

    /**
     * @see AcceptorMonitor#serverSocketListening
     */
    public void serverSocketListening( String name, ServerSocket serverSocket )
    {
    }

    /**
     * @see AcceptorMonitor#errorAcceptingConnection
     */
    public void errorAcceptingConnection( String name, IOException ioe )
    {
    }

    /**
     * @see AcceptorMonitor#errorClosingServerSocket
     */
    public void errorClosingServerSocket( String name, IOException ioe )
    {
    }
}
