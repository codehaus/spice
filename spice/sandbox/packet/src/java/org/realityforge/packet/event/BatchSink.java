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
 * @version $Revision: 1.1 $ $Date: 2003-11-28 05:00:38 $
 */
public interface BatchSink
{
    /**
     * Attempt to add a set of events to the sink. If unable to add one event to
     * sink then do not add any event and return false.
     * 
     * @param events the events
     * @return false if unable to add events.
     */
    boolean addEvents( Object[] events );
}
