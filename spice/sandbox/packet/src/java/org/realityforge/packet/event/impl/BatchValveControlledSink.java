/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.event.impl;

import org.realityforge.packet.event.BatchEventValve;
import org.realityforge.packet.event.BatchSink;

/**
 * The ValveControlledSink passes events onto a destination sink unless they are
 * filtered out by a valve.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-11-28 05:00:38 $
 */
public class BatchValveControlledSink
    implements BatchSink
{
    /** The destination sink. */
    private final BatchSink _sink;

    /** The valve that controls whether event passed onto sink. */
    private final BatchEventValve _valve;

    /**
     * Create valved sink terminating on specified sink with specified valve.
     * 
     * @param sink the sink
     * @param valve the valve
     */
    public BatchValveControlledSink( final BatchSink sink,
                                final BatchEventValve valve )
    {
        if( null == sink )
        {
            throw new NullPointerException( "sink" );
        }
        if( null == valve )
        {
            throw new NullPointerException( "valve" );
        }
        _sink = sink;
        _valve = valve;
    }

    /**
     * @see BatchSink#addEvents(Object[])
     */
    public boolean addEvents( final Object[] events )
    {
        if( !_valve.acceptEvents( events ) )
        {
            return false;
        }
        else
        {
            return _sink.addEvents( events );
        }
    }
}
