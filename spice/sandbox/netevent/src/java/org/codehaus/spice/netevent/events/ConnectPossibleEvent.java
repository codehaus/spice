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
 * Event indicating that it is possible to read from the socket.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-16 00:17:04 $
 */
public class ConnectPossibleEvent
    extends ChannelEvent
{
    /**
     * Create event.
     * 
     * @param channel the channel.
     */
    public ConnectPossibleEvent( final Channel channel,
                                 final Object userData )
    {
        super( channel, userData );
    }
}