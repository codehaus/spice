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
 * @version $Revision: 1.3 $ $Date: 2004-05-17 06:21:38 $
 */
public class ConnectPossibleEvent
    extends ChannelEvent
{
    /**
     * Key that used to generate event.
     */
    private final SelectionKey _key;

    /**
     * Create event.
     * 
     * @param channel the channel.
     */
    public ConnectPossibleEvent( final Channel channel,
                                 final Object userData,
                                 final SelectionKey key )
    {
        super( channel, userData );
        _key = key;
    }

    public SelectionKey getKey()
    {
        return _key;
    }
}