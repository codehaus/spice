/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.impl;

import java.io.IOException;
import java.net.ServerSocket;

import org.jcomponent.netserve.connection.impl.AcceptorMonitor;

/**
 * A noop monitor.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-27 05:26:55 $
 */
public class NullAcceptorMonitor
    implements AcceptorMonitor
{
    /**
     * Add constant for instance of Null Monitor.
     */
    public static final NullAcceptorMonitor MONITOR = new NullAcceptorMonitor();

    /**
     * @see org.jcomponent.netserve.connection.impl.AcceptorMonitor#acceptorCreated
     */
    public void acceptorCreated( String name, ServerSocket serverSocket )
    {
    }

    /**
     * @see org.jcomponent.netserve.connection.impl.AcceptorMonitor#acceptorClosing
     */
    public void acceptorClosing( String name, ServerSocket serverSocket )
    {
    }

    /**
     * @see org.jcomponent.netserve.connection.impl.AcceptorMonitor#serverSocketListening
     */
    public void serverSocketListening( String name, ServerSocket serverSocket )
    {
    }

    /**
     * @see org.jcomponent.netserve.connection.impl.AcceptorMonitor#errorAcceptingConnection
     */
    public void errorAcceptingConnection( String name, IOException ioe )
    {
    }

    /**
     * @see org.jcomponent.netserve.connection.impl.AcceptorMonitor#errorClosingServerSocket
     */
    public void errorClosingServerSocket( String name, IOException ioe )
    {
    }
}
