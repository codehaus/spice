/*
* Copyright (C) The Spice Group. All rights reserved.
*
* This software is published under the terms of the Spice
* Software License version 1.1, a copy of which has been included
* with this distribution in the LICENSE.txt file.
*/
package org.realityforge.packet.event;

/**
 * The valve controls the flow events into a sink.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-11-28 04:24:00 $
 */
public interface EventValve
{
    /**
     * Return true if event should be accepted.
     * 
     * @param event the event
     * @return true if event should be accepted.
     */
    boolean acceptEvent( Object event );

    /**
     * Return true if events should be accepted.
     * 
     * @param events the events
     * @return true if events should be accepted.
     */
    boolean acceptEvents( Object[] events );
}
