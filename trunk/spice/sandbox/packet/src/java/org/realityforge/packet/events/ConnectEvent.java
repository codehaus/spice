/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.events;

import java.nio.channels.SocketChannel;

/**
 * An Event indicating that specified Socket was connected.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-17 00:20:44 $
 */
public class ConnectEvent
{
    /** The connected spocket channel. */
    private final SocketChannel _socketChannel;

    /**
     * Create event for channel.
     * 
     * @param socketChannel the channel.
     */
    public ConnectEvent( final SocketChannel socketChannel )
    {
        if( null == socketChannel )
        {
            throw new NullPointerException( "socketChannel" );
        }
        _socketChannel = socketChannel;
    }

    /**
     * Return the socket channel.
     * 
     * @return the socket channel.
     */
    public SocketChannel getSocketChannel()
    {
        return _socketChannel;
    }
}
