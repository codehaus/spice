/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets;

import java.io.IOException;
import java.net.Socket;

/**
 * Implement this interface to process incoming socket connections.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-08 02:00:22 $
 */
public interface SocketConnectionHandler
{
    /**
     * Processes specified connection as they occur.
     *
     * @param connection the connection
     * @throws IOException if an error occurs handling connection
     */
    void handleConnection( Socket connection )
        throws IOException;
}
