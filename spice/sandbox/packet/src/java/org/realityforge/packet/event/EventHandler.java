/*
* Copyright (C) The Spice Group. All rights reserved.
*
* This software is published under the terms of the Spice
* Software License version 1.1, a copy of which has been included
* with this distribution in the LICENSE.txt file.
*/
package org.realityforge.packet.event;

/**
 * The event handler is responsible for processing events.
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-11-28 03:43:36 $
 */
public interface EventHandler
{
    /**
     * Handle an event.
     *
     * @param event the event
     */
    void handleEvent( Object event );

    /**
     * Handle a set of events.
     *
     * @param events the events
     */
    void handleEvents( Object[] events );
}
