/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.timeevent.events;

import org.codehaus.spice.event.AbstractEvent;
import org.codehaus.spice.timeevent.source.SchedulingKey;

/**
 * An Event related to time trigger firing.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-22 03:32:52 $
 */
public class TimeEvent
    extends AbstractEvent
{
    /**
     * The SchedulingKey.
     */
    private final SchedulingKey _key;

    /**
     * Crete event.
     *
     * @param key the SchedulingKey
     */
    public TimeEvent( final SchedulingKey key )
    {
        _key = key;
    }

    /**
     * Return the SchedulingKey.
     *
     * @return the SchedulingKey.
     */
    public SchedulingKey getKey()
    {
        return _key;
    }

    /**
     * @see AbstractEvent#getEventDescription()
     */
    protected String getEventDescription()
    {
        return String.valueOf( getKey().getUserData() );
    }
}
