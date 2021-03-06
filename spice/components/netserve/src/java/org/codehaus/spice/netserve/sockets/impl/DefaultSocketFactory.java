/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netserve.sockets.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import org.codehaus.spice.netserve.sockets.SocketFactory;

/**
 * A SocketFactory that creates vanilla sockets.
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-03-21 23:42:59 $
 * @dna.component
 * @dna.service type="SocketFactory"
 */
public class DefaultSocketFactory
    implements SocketFactory
{
    /**
     * Create a socket that connects to specified remote address.
     *
     * @param address the remote address
     * @param port the remote port
     * @return the socket connected to remote address
     * @throws IOException if unable to create socket
     */
    public Socket createSocket( final InetAddress address, final int port )
        throws IOException
    {
        return new Socket( address, port );
    }

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
    public Socket createSocket( final InetAddress address,
                                final int port,
                                final InetAddress localAddress,
                                final int localPort )
        throws IOException
    {
        return new Socket( address, port, localAddress, localPort );
    }
}
