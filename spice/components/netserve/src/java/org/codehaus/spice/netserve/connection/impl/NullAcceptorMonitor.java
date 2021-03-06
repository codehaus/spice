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

import org.codehaus.spice.netserve.connection.impl.AcceptorMonitor;

/**
 * A noop monitor.
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-03-21 23:42:59 $
 */
public class NullAcceptorMonitor
    implements AcceptorMonitor
{
    /**
     * Add constant for instance of Null Monitor.
     */
    public static final NullAcceptorMonitor MONITOR = new NullAcceptorMonitor();

    /**
     * @see AcceptorMonitor#acceptorCreated
     */
    public void acceptorCreated( final String name,
                                 final ServerSocket serverSocket )
    {
    }

    /**
     * @see AcceptorMonitor#acceptorClosing
     */
    public void acceptorClosing( final String name,
                                 final ServerSocket serverSocket )
    {
    }

    /**
     * @see AcceptorMonitor#serverSocketListening
     */
    public void serverSocketListening( final String name,
                                       final ServerSocket serverSocket )
    {
    }

    /**
     * @see AcceptorMonitor#errorAcceptingConnection
     */
    public void errorAcceptingConnection( final String name,
                                          final IOException ioe )
    {
    }

    /**
     * @see AcceptorMonitor#errorClosingServerSocket
     */
    public void errorClosingServerSocket( final String name,
                                          final IOException ioe )
    {
    }
}
