/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.event;

/**
 * The EventSource represents a source of events.
 * 
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2003-12-05 02:11:00 $
 */
public interface EventSource
{
    /**
     * Return next event from source. Returning null indicates that there are no
     * events at this stage.
     * 
     * @return the event or null
     */
    Object getEvent();

    /**
     * Return a set of events from source. The maximum number of events returned
     * is specified by the count parameter.
     * 
     * @param count the maximum number of events returned
     * @return the set of events
     */
    Object[] getEvents( int count );

    /**
     * Return the underlying object that is synchronized upon if synchronization
     * is necessary.
     * 
     * @return the lock object
     */
    Object getSyncLock();
}
