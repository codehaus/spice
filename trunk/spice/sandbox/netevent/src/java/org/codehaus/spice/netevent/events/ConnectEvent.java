/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netevent.events;

import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * An Event indicating that ServerSocket can accept a connection.
 * 
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2004-01-07 04:08:13 $
 */
public class ConnectEvent
{
    /** The ServerSocketChannel. */
    private final ServerSocketChannel m_serverSocketChannel;

    /**
     * Create event for channel.
     * 
     * @param channel the channel.
     */
    public ConnectEvent( final ServerSocketChannel channel )
    {
        if( null == channel )
        {
            throw new NullPointerException( "channel" );
        }
        m_serverSocketChannel = channel;
    }

    /**
     * Return the socket channel.
     * 
     * @return the socket channel.
     */
    public ServerSocketChannel getServerSocketChannel()
    {
        return m_serverSocketChannel;
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
        final SocketAddress address =
            m_serverSocketChannel.socket().getLocalSocketAddress();
        return getClass().getName() + "[" + address + "]";
    }
}
