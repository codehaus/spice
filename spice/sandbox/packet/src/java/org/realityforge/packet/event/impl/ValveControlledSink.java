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
 * @version $Revision: 1.8 $ $Date: 2003-12-09 03:11:46 $
 */
public class ValveControlledSink
    implements EventSink
{
    /** The destination sink. */
    final EventSink m_sink;

    /** The valve that controls whether event passed onto sink. */
    final EventValve m_valve;

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
        m_sink = sink;
        m_valve = valve;
    }

    /**
     * @see EventSink#addEvent(Object)
     */
    public boolean addEvent( final Object event )
    {
        if( !m_valve.acceptEvent( event ) )
        {
            return false;
        }
        else
        {
            return m_sink.addEvent( event );
        }
    }

    /**
     * @see EventSink#addEvents(Object[])
     */
    public boolean addEvents( final Object[] events )
    {
        if( !m_valve.acceptEvents( events ) )
        {
            return false;
        }
        else
        {
            return m_sink.addEvents( events );
        }
    }

    /**
     * @see EventSink#getSinkLock()
     */
    public Object getSinkLock()
    {
        return m_sink.getSinkLock();
    }
}
