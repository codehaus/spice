/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netevent.events;

import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;

/**
 * Event indicating that it is possible to read from the socket.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-07 07:06:13 $
 */
public class WritePossibleEvent
    extends ChannelEvent
{
    /**
     * Create Event.
     * 
     * @param channel the associated channel.
     * @param key the SelectionKey if any
     * @param userData the userData if any
     */
    public WritePossibleEvent( final Channel channel,
                               final SelectionKey key,
                               final Object userData )
    {
        super( channel, key, userData );
    }
}
