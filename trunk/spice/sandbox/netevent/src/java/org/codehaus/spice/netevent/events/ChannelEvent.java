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
 * Event indicating IO event on Channel.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-07 07:06:13 $
 */
public abstract class ChannelEvent
{
    /** The associated channel. */
    private final Channel m_channel;

    /** The associated SelectionKey. May be null. */
    private final SelectionKey m_key;

    /** Userdata associated with channel. */
    private final Object m_userData;

    /**
     * Create Event.
     * 
     * @param channel the associated channel
     * @param key the SelectionKey if any
     * @param userData the userData if any
     */
    protected ChannelEvent( final Channel channel,
                            final SelectionKey key,
                            final Object userData )
    {
        if( null == channel )
        {
            throw new NullPointerException( "channel" );
        }
        m_channel = channel;
        m_key = key;
        m_userData = userData;
    }

    /**
     * Return the associated channel.
     * 
     * @return the associated channel.
     */
    public Channel getChannel()
    {
        return m_channel;
    }

    /**
     * Return the SelectionKey associated with channel. May be null.
     * 
     * @return the SelectionKey associated with channel.
     */
    public SelectionKey getKey()
    {
        return m_key;
    }

    /**
     * Return the userdata associated with channel.
     * 
     * @return the userdata associated with channel.
     */
    public Object getUserData()
    {
        return m_userData;
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
        final String shortName = getShortName();
        return shortName + "[" + getEventDescription() + "]";
    }

    /**
     * Return the description of event.
     * 
     * @return the description of event.
     */
    protected String getEventDescription()
    {
        return String.valueOf( getChannel() );
    }

    /**
     * Return the "Short" name of class sans package name.
     * 
     * @return the short name of class
     */
    String getShortName()
    {
        final String name = getClass().getName();
        final int index = name.lastIndexOf( '.' ) + 1;
        return name.substring( index );
    }
}
