/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.event.impl;

import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSource;

/**
 * Pump Events from an EventSource to an EventHandler. The Pump attempts to
 * transmit batchSize events every time it performs a refresh.
 * 
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2004-03-18 03:39:07 $
 */
public class EventPump
{
    /**
     * The number events to try to pump from Source to Handler each refresh.
     */
    private int m_batchSize = Integer.MAX_VALUE;

    /**
     * The name of pump. (Useful for debugging).
     */
    final String m_name;

    /**
     * The source of events.
     */
    final EventSource m_source;

    /**
     * The handler for events.
     */
    final EventHandler m_handler;

   /**
     * Create a pump from specified source to specified handler.
     *
     * @param source the source
     * @param handler the handler
     */
    public EventPump( final EventSource source,
                      final EventHandler handler )
    {
       this( "", source, handler );
    }

    /**
     * Create a pump from specified source to specified handler.
     *
     * @param name the name
     * @param source the source
     * @param handler the handler
     */
    public EventPump( final String name,
                      final EventSource source,
                      final EventHandler handler )
    {
        if( null == name )
        {
            throw new NullPointerException( "name" );
        }
        if( null == source )
        {
            throw new NullPointerException( "source" );
        }
        if( null == handler )
        {
            throw new NullPointerException( "handler" );
        }
        m_name = name;
        m_source = source;
        m_handler = handler;
    }

    /**
     * Perform a single refresh and pump events from source to the handler.
     */
    public void refresh()
    {
        final int batchSize = getBatchSize();
        if( batchSize < 2 )
        {
            final Object event = m_source.getEvent();
            if( null != event )
            {
                m_handler.handleEvent( event );
            }
        }
        else
        {
            final Object[] events = m_source.getEvents( batchSize );
            if( 0 != events.length )
            {
                m_handler.handleEvents( events );
            }
        }
    }

    /**
     * Return the number events pumped every refresh.
     * 
     * @return the number events pumped every refresh.
     */
    public int getBatchSize()
    {
        return m_batchSize;
    }

    /**
     * Set the number events pumped every refresh.
     * 
     * @param batchSize the number events pumped every refresh.
     */
    public void setBatchSize( final int batchSize )
    {
        m_batchSize = batchSize;
    }

   /**
    * Return the name.
    *
    * @return the name
    */
    public String getName()
    {
        return m_name;
    }
}
