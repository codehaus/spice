/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.event.impl;

import org.realityforge.packet.event.EventSink;
import org.realityforge.packet.event.EventValve;

/**
 * The ValveControlledSink passes events onto a destination sink unless they are
 * filtered out by a valve.
 * 
 * @author Peter Donald
 * @version $Revision: 1.5 $ $Date: 2003-12-05 02:11:00 $
 */
public class ValveControlledSink
    implements EventSink
{
    /** The destination sink. */
    private final EventSink _sink;

    /** The valve that controls whether event passed onto sink. */
    private final EventValve _valve;

    /**
     * Create valved sink terminating on specified sink with specified valve.
     * 
     * @param sink the sink
     * @param valve the valve
     */
    public ValveControlledSink( final EventSink sink,
                                final EventValve valve )
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
     * @see EventSink#addEvent(Object)
     */
    public boolean addEvent( final Object event )
    {
        if( !_valve.acceptEvent( event ) )
        {
            return false;
        }
        else
        {
            return _sink.addEvent( event );
        }
    }

    /**
     * @see EventSink#addEvents(Object[])
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

    /**
     * @see EventSink#getSyncLock()
     */
    public Object getSyncLock()
    {
        return _sink.getSyncLock();
    }
}
