/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.net;

import java.net.SocketAddress;
import org.realityforge.packet.net.transport.TcpTransport;

/**
 * Event indicating IO occured.
 * 
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-01-07 02:40:43 $
 */
public abstract class IOEvent
    extends AbstractTransportEvent
{
    /** The number of bytes transferred. */
    private final int _count;

    /**
     * Create event.
     * 
     * @param transport the transport
     * @param count the byte count
     */
    public IOEvent( final TcpTransport transport, final int count )
    {
        super( transport );
        _count = count;
    }

    /**
     * Return the number of bytes transferred.
     * 
     * @return the number of bytes transferred.
     */
    public int getCount()
    {
        return _count;
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
        final SocketAddress address =
            getTransport().getChannel().socket().getRemoteSocketAddress();
        return getClass().getName() + "[" + getCount() +
               " bytes to " + address + "]";
    }
}
