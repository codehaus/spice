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
 * @version $Revision: 1.1 $ $Date: 2003-11-28 03:43:36 $
 */
public interface Sink
{
    /**
     * Add an event to sink.
     * 
     * @param event the event
     * @throws Exception if unable to add event
     */
    void addEvent( Object event )
        throws Exception;

    /**
     * Add a set of events to sink.
     * If one event can not be added then
     * no events are added and an exception is thrown.
     * 
     * @param events the events
     * @throws Exception if unable to add events
     */
    void addEvents( Object[] events )
        throws Exception;
}
