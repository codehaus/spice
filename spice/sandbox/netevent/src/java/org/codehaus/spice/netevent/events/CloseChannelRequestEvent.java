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
 * Event indicating that it is possible to read from the socket.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-12 23:55:18 $
 */
public class CloseChannelRequestEvent
    extends AbstractTransportEvent
{
    /**
     * Create Event.
     * 
     * @param transport the transport
     */
    public CloseChannelRequestEvent( final ChannelTransport transport )
    {
        super( transport );
    }
}
