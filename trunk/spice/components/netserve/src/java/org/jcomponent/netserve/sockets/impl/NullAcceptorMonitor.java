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
 * A noop connection acceptor.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-08 08:25:49 $
 */
public class NullAcceptorMonitor
    implements AcceptorMonitor
{
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
