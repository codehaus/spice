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
 * Event indicating that it is possible to read from the socket.
 * 
 * @author Peter Donald
 * @version $Revision: 1.4 $ $Date: 2004-01-19 04:47:15 $
 */
public class TransportDisconnectRequestEvent
    extends AbstractTransportEvent
{
    /** The reason for disconnecting transport. */
    private final byte _reason;

    /**
     * Create Event.
     * 
     * @param transport the transport
     * @param reason the reason
     */
    public TransportDisconnectRequestEvent( final ChannelTransport transport,
                                            final byte reason )
    {
        super( transport );
        _reason = reason;
    }

    /**
     * Return the reason for disconnecting transport.
     * 
     * @return the reason for disconnecting transport.
     */
    public byte getReason()
    {
        return _reason;
    }

    /**
     * @see AbstractTransportEvent#getEventDescription()
     */
    protected String getEventDescription()
    {
        return super.getEventDescription() + " Reason: " + getReason();
    }
}
