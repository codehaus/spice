/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.event;

/**
 * A Sink represents the destination for events.
 * 
 * @author Peter Donald
 * @version $Revision: 1.4 $ $Date: 2003-11-28 05:00:38 $
 */
public interface Sink
{
    /**
     * Attempt to add an event to the sink. Return false if unable to add
     * event.
     * 
     * @param event the event
     * @return false if unable to add event.
     */
    boolean addEvent( Object event );
}
