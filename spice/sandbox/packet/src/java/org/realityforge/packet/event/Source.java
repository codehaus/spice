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
 * @version $Revision: 1.3 $ $Date: 2003-11-28 05:00:38 $
 */
public interface Source
{
    /**
     * Return next event from source. Returning null indicates that there are no
     * events at this stage.
     * 
     * @return the event or null
     */
    Object getEvent();
}
