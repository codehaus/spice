/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.event.impl;

import org.codehaus.spice.event.EventJoin;
import org.codehaus.spice.event.impl.collections.Buffer;

/**
 * An event queue that acts as a Source and Sink of events.
 * 
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-01-22 02:23:29 $
 */
public class DefaultEventQueue
    implements EventJoin
{
    /**
     * An empty array of objects.
     */
    private static final Object[] EMPTY_OBJECT_SET = new Object[ 0 ];

    /**
     * Lock for the sink aspect of queue.
     */
    private final Object m_sinkLock = new Object();

    /**
     * Lock for the source aspect of queue.
     */
    private final Object m_sourceLock = new Object();

    /**
     * The underlying buffer used to store events.
     */
    private final Buffer m_buffer;

    /**
     * Create an Event queue using underlying Buffer.
     * 
     * @param buffer the buffer
     */
    public DefaultEventQueue( final Buffer buffer )
    {
        if( null == buffer )
        {
            throw new NullPointerException( "buffer" );
        }
        m_buffer = buffer;
    }

    /**
     * @see EventJoin#getEvent()
     */
    public Object getEvent()
    {
        final Object lock = getSinkLock();
        synchronized( lock )
        {
            final Buffer buffer = getBuffer();
            final int size = buffer.size();
            if( size > 0 )
            {
                final Object result = buffer.pop();
                lock.notifyAll();
                return result;
            }
            else
            {
                return null;
            }
        }
    }

    /**
     * @see EventJoin#getEvents(int)
     */
    public Object[] getEvents( final int count )
    {
        final Object lock = getSinkLock();
        synchronized( lock )
        {
            final Buffer buffer = getBuffer();
            final int size = buffer.size();
            final int resultCount = Math.min( size, count );
            if( resultCount > 0 )
            {
                final Object[] objects = new Object[ resultCount ];
                for( int i = 0; i < objects.length; i++ )
                {
                    objects[ i ] = buffer.pop();
                }
                lock.notifyAll();
                return objects;
            }
            else
            {
                return EMPTY_OBJECT_SET;
            }
        }
    }

    /**
     * @see EventJoin#addEvent(Object)
     */
    public boolean addEvent( final Object event )
    {
        final Object lock = getSourceLock();
        synchronized( lock )
        {
            final boolean result = getBuffer().add( event );
            if( result )
            {
                lock.notifyAll();
            }
            return result;
        }
    }

    /**
     * @see EventJoin#addEvents(Object[])
     */
    public boolean addEvents( final Object[] events )
    {
        final Object lock = getSourceLock();
        synchronized( lock )
        {
            final boolean result = getBuffer().addAll( events );
            if( result )
            {
                lock.notifyAll();
            }
            return result;
        }
    }

    /**
     * @see EventJoin#getSinkLock()
     */
    public Object getSinkLock()
    {
        return m_sinkLock;
    }

    /**
     * @see EventJoin#getSourceLock()
     */
    public Object getSourceLock()
    {
        return m_sourceLock;
    }

    /**
     * Return the underlying buffer for events.
     * 
     * @return the underlying buffer for events.
     */
    protected Buffer getBuffer()
    {
        return m_buffer;
    }
}
