/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets.impl;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-10-09 03:54:18 $
 */
class RecordingAcceptorMonitor
    extends NullAcceptorMonitor
{
    private IOException m_errorClosingServerSocket;
    private IOException m_errorAcceptingConnection;
    private int m_listenCount;

    public void serverSocketListening( String name, ServerSocket serverSocket )
    {
        m_listenCount++;
        super.serverSocketListening( name, serverSocket );
    }

    public void errorAcceptingConnection( String name, IOException ioe )
    {
        m_errorAcceptingConnection = ioe;
        super.errorAcceptingConnection( name, ioe );
    }

    public void errorClosingServerSocket( String name, IOException ioe )
    {
        m_errorClosingServerSocket = ioe;
        super.errorClosingServerSocket( name, ioe );
    }

    IOException getErrorClosingServerSocket()
    {
        return m_errorClosingServerSocket;
    }

    IOException getErrorAcceptingConnection()
    {
        return m_errorAcceptingConnection;
    }

    int getListenCount()
    {
        return m_listenCount;
    }
}
