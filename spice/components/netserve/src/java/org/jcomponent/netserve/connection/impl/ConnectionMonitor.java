/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.impl;

import java.net.Socket;
import org.jcomponent.netserve.sockets.impl.AcceptorMonitor;

/**
 * Monitor interface for Connection.
 * Provides a facade to support different types of events, including logging.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 */
public interface ConnectionMonitor
    extends AcceptorMonitor
{
    void acceptorDisconnecting( String name, boolean tearDown );

    void connectionEnding( String name, Socket socket );

    void connectionStarting( String name, Socket socket );

    void disposingHandler( String name, Socket socket );

    void errorHandlerAlreadyDisposed( String name, Socket socket );

    void errorClosingSocket( String name, Socket socket, Exception e );

    void errorAquiringHandler( String name, Exception e );

    void errorHandlingConnection( String name, Socket socket, Exception e );
}
