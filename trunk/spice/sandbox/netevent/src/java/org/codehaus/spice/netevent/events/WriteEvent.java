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
 * @version $Revision: 1.2 $ $Date: 2004-01-08 03:41:13 $
 */
public class WriteEvent
    extends AbstractTransportEvent
{
    /**
     * Create event.
     * 
     * @param transport the transport
     */
    public WriteEvent( final ChannelTransport transport )
    {
        super( transport );
    }
}
