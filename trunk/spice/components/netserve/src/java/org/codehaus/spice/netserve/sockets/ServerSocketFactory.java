/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netserve.sockets;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * Service used to create server sockets. The factory is used so that
 * the exact socket type and underlying transport is abstracted. The
 * sockets created could be proxied, SSL enabled, TLS enabled etc.
 * However clients just care that they return sockets.
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-03-21 23:43:00 $
 */
public interface ServerSocketFactory
{
    /**
     * Creates a socket on specified port.
     *
     * @param port the port (0 indicates any available port)
     * @return the created ServerSocket
     * @throws IOException if unable to create socket
     */
    ServerSocket createServerSocket( int port )
        throws IOException;

    /**
     * Creates a socket on specified port with a specified backlog.
     *
     * @param port the port (0 indicates any available port)
     * @param backlog the backlog
     * @return the created ServerSocket
     * @throws IOException if unable to create socket
     */
    ServerSocket createServerSocket( int port, int backlog )
        throws IOException;

    /**
     * Creates a socket on a particular network interface on specified port
     * with a specified backlog.
     *
     * @param port the port (0 indicates any available port)
     * @param backlog the backlog
     * @param address the network interface to bind to.
     * @return the created ServerSocket
     * @throws IOException if unable to create socket
     */
    ServerSocket createServerSocket( int port, int backlog, InetAddress address )
        throws IOException;
}

