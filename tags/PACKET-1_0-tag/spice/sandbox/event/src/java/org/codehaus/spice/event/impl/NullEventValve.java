/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.event.impl;

import org.codehaus.spice.event.EventValve;

/**
 * A valve that accepts all events.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-16 04:22:50 $
 */
public class NullEventValve
    implements EventValve
{
    /** Constant for instance of null event valve. */
    public static final NullEventValve VALVE = new NullEventValve();

    /**
     * @see EventValve#acceptEvent(Object)
     */
    public boolean acceptEvent( final Object event )
    {
        return true;
    }

    /**
     * @see EventValve#acceptEvents(Object[])
     */
    public boolean acceptEvents( final Object[] events )
    {
        return true;
    }
}
