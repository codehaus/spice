/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.events;

import org.realityforge.packet.transport.TcpTransport;

/**
 * An Event related to a particular transport.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-17 00:20:44 $
 */
public abstract class AbstractTransportEvent
{
    /** The transport. */
    private final TcpTransport _transport;

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
        _transport = transport;
    }

    /**
     * Return the transport.
     * 
     * @return the transport.
     */
    public TcpTransport getTransport()
    {
        return _transport;
    }
}