/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Service used to create client sockets. The factory is used so that
 * the exact socket type and underlying transport is abstracted. The
 * sockets created could be proxied, SSL enabled, TLS enabled etc.
 * However clients just care that they return sockets.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-09-02 04:14:29 $
 */
public interface SocketFactory
{
    /**
     * Create a socket that connects to specified remote address.
     *
     * @param address the remote address
     * @param port the remote port
     * @return the socket connected to remote address
     * @throws IOException if unable to create socket
     */
    Socket createSocket( InetAddress address, int port )
        throws IOException;

    /**
     * Create a socket that connects to specified remote address and
     * originates from specified local address.
     *
     * @param address the remote address
     * @param port the remote port
     * @param localAddress the local address
     * @param localPort the local port
     * @return the socket connected to remote address
     * @throws IOException if unable to create socket
     */
    Socket createSocket( InetAddress address, int port,
                         InetAddress localAddress, int localPort )
        throws IOException;
}
