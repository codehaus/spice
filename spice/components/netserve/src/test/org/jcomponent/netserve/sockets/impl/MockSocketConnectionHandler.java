/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets.impl;

import org.jcomponent.netserve.sockets.SocketConnectionHandler;
import java.net.Socket;
import java.io.IOException;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-08 08:35:35 $
 */
class MockSocketConnectionHandler
    implements SocketConnectionHandler
{
    private Socket m_socket;

    public void handleConnection( Socket connection )
        throws IOException
    {
        m_socket = connection;
    }

    Socket getSocket()
    {
        return m_socket;
    }
}
