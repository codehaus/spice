/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netevent.events;

import java.nio.channels.Channel;

/**
 * An Event indicating that ServerSocket can accept a connection.
 * 
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2004-05-17 06:21:38 $
 */
public class AcceptPossibleEvent
    extends ChannelEvent
{
    /**
     * Create event.
     * 
     * @param channel the channel.
     */
    public AcceptPossibleEvent( final Channel channel, final Object userData )
    {
        super( channel, userData );
    }
}
