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
 * A noop monitor.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-10-10 04:06:12 $
 */
public class NullNIOAcceptorMonitor
    implements NIOAcceptorMonitor
{
    /**
     * Add constant for instance of Null Monitor.
     */
    public static final NullNIOAcceptorMonitor MONITOR = new NullNIOAcceptorMonitor();

    /**
     * @see NIOAcceptorMonitor#acceptorCreated
     */
    public void acceptorCreated( final String name,
                                 final ServerSocket serverSocket )
    {
    }

    /**
     * @see NIOAcceptorMonitor#acceptorClosing
     */
    public void acceptorClosing( final String name )
    {
    }

    /**
     * @see NIOAcceptorMonitor#errorAcceptingConnection
     */
    public void errorAcceptingConnection( final String name,
                                          final IOException ioe )
    {
    }

    /**
     * @see NIOAcceptorMonitor#selectorShutdown
     */
    public void selectorShutdown()
    {
    }

    /**
     * @see NIOAcceptorMonitor#errorClosingSelector
     */
    public void errorClosingSelector( final IOException ioe )
    {
    }

    /**
     * @see NIOAcceptorMonitor#exitingSelectorLoop
     */
    public void exitingSelectorLoop()
    {
    }

    /**
     * @see NIOAcceptorMonitor#enteringSelect
     */
    public void enteringSelect()
    {
    }

    /**
     * @see NIOAcceptorMonitor#selectCompleted
     */
    public void selectCompleted( final int count )
    {
    }

    /**
     * @see NIOAcceptorMonitor#handlingConnection
     */
    public void handlingConnection( final String name,
                                    final ServerSocket serverSocket,
                                    final Socket socket )
    {
    }
}
