/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.event;

/**
 * The Source represents a source of events.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-11-28 05:00:38 $
 */
public interface BatchSource
{
    /**
     * Return a set of events from source. The maximum number of events returned
     * is specified by the count parameter.
     * 
     * @param count the maximum number of events returned
     * @return the set of events
     */
    Object[] getEvents( int count );
}
