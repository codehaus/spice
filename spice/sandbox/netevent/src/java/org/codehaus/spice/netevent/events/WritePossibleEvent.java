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
 * @version $Revision: 1.3 $ $Date: 2004-05-17 06:21:38 $
 */
public class WritePossibleEvent
    extends AbstractTransportEvent
{
    /**
     * Create Event.
     * 
     * @param transport the transport
     */
    public WritePossibleEvent( final ChannelTransport transport )
    {
        super( transport );
    }
}
