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
 * Event indicating more data ready in stream were read.
 * 
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-01-20 01:08:30 $
 */
public class InputDataPresentEvent
    extends DataPresentEvent
{
    /**
     * Create event.
     * 
     * @param transport the transport
     */
    public InputDataPresentEvent( final ChannelTransport transport,
                                  final int count )
    {
        super( transport, count );
    }
}
