/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.impl;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;

/**
 * Noop implementation of ConnectionMonitor.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.4 $ $Date: 2003-10-08 07:41:12 $
 */
class NullConnectionMonitor
    implements ConnectionMonitor
{
    public void acceptorDisconnecting( String name, boolean tearDown )
    {
    }

    public void connectionEnding( String name, Socket socket )
    {
    }

    public void connectionStarting( String name, Socket socket )
    {
    }

    public void disposingHandler( String name, Socket socket )
    {
    }

    public void errorHandlerAlreadyDisposed( String name, Socket socket )
    {
    }

    public void errorClosingSocket( String name, Socket socket, Exception e )
    {
    }

    public void errorAquiringHandler( String name, Exception e )
    {
    }

    public void errorHandlingConnection( String name, Socket socket, Exception e )
    {
    }

    public void acceptorCreated( String name, ServerSocket serverSocket )
    {
    }

    public void acceptorClosing( String name, ServerSocket serverSocket )
    {
    }

    public void serverSocketListening( String name, ServerSocket serverSocket )
    {
    }

    public void errorAcceptingConnection( String name, IOException ioe )
    {
    }

    public void errorClosingServerSocket( String name, IOException ioe )
    {
    }
}
