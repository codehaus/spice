/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.events;

import org.codehaus.spice.netevent.events.AbstractTransportEvent;
import org.codehaus.spice.netevent.transport.ChannelTransport;

/**
 * Event indicating that transport has disconnected.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-13 05:56:48 $
 */
public class TransportDisconnectEvent
    extends AbstractTransportEvent
{
    /** The reason for disconnecting transport. */
    private final int _reason;

    /**
     * Create Event.
     * 
     * @param transport the transport
     * @param reason the reason
     */
    public TransportDisconnectEvent( final ChannelTransport transport,
                                     final int reason )
    {
        super( transport );
        _reason = reason;
    }

    /**
     * Return the reason for disconnecting transport.
     * 
     * @return the reason for disconnecting transport.
     */
    public int getReason()
    {
        return _reason;
    }
}
