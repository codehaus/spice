/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netevent.events;

import java.net.SocketAddress;
import org.codehaus.spice.netevent.transport.TcpTransport;

/**
 * An Event related to a particular transport.
 * 
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-01-07 04:05:29 $
 */
public abstract class AbstractTransportEvent
{
    /** The transport. */
    private final TcpTransport m_transport;

    /**
     * Create event for specified transport.
     * 
     * @param transport the transport
     */
    public AbstractTransportEvent( final TcpTransport transport )
    {
        if( null == transport )
        {
            throw new NullPointerException( "transport" );
        }
        m_transport = transport;
    }

    /**
     * Return the transport.
     * 
     * @return the transport.
     */
    public TcpTransport getTransport()
    {
        return m_transport;
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
        final SocketAddress address =
            getTransport().getChannel().socket().getRemoteSocketAddress();
        return getClass().getName() + "[" + address + "]";
    }
}
