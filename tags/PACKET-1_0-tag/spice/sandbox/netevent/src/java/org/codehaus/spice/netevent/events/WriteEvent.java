/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netevent.events;

import org.codehaus.spice.netevent.transport.ChannelTransport;

/**
 * An Event indicating a write.
 * 
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2004-01-20 01:08:30 $
 */
public class WriteEvent
    extends AbstractTransportEvent
{
    /** The number of bytes written. */
    private final int _count;

    /**
     * Create event.
     * 
     * @param transport the transport
     */
    public WriteEvent( final ChannelTransport transport,
                       final int count )
    {
        super( transport );
        _count = count;
    }

    /**
     * Return the number of bytes written.
     * 
     * @return the number of bytes written.
     */
    public int getCount()
    {
        return _count;
    }

    /**
     * @see AbstractTransportEvent#getEventDescription()
     */
    protected String getEventDescription()
    {
        return "count=" + getCount() + " " + super.getEventDescription();
    }
}
