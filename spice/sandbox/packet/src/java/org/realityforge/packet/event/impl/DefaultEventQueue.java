package org.realityforge.packet.event.impl;

import org.realityforge.packet.event.EventSink;
import org.realityforge.packet.event.EventSource;
import org.realityforge.packet.event.impl.collections.Buffer;

/**
 * An event queue that acts as a Source and Sink of events.
 * 
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003-12-05 02:21:03 $
 */
public class DefaultEventQueue
    implements EventSource, EventSink
{
    /** The underlying buffer used to store events. */
    private final Buffer _buffer;

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
        _buffer = buffer;
    }

    /**
     * @see EventSource#getEvent()
     */
    public Object getEvent()
    {
        synchronized( getSyncLock() )
        {
            final Buffer buffer = getBuffer();
            final int size = buffer.size();
            if( size > 0 )
            {
                final Object result = buffer.pop();
                getSyncLock().notifyAll();
                return result;
            }
            else
            {
                return null;
            }
        }
    }

    /**
     * @see EventSource#getEvents(int)
     */
    public Object[] getEvents( final int count )
    {
        synchronized( getSyncLock() )
        {
            final Buffer buffer = getBuffer();
            final int size = buffer.size();
            final int resultCount = Math.min( size, count );
            final Object[] objects = new Object[ resultCount ];
            for( int i = 0; i < objects.length; i++ )
            {
                objects[ i ] = buffer.pop();
            }
            getSyncLock().notifyAll();
            return objects;
        }
    }

    /**
     * @see EventSink#addEvent(Object)
     */
    public boolean addEvent( final Object event )
    {
        synchronized( getSyncLock() )
        {
            final boolean result = getBuffer().add( event );
            if( result )
            {
                getSyncLock().notifyAll();
            }
            return result;
        }
    }

    /**
     * @see EventSink#addEvents(Object[])
     */
    public boolean addEvents( final Object[] events )
    {
        synchronized( getSyncLock() )
        {
            final boolean result = getBuffer().addAll( events );
            if( result )
            {
                getSyncLock().notifyAll();
            }
            return result;
        }
    }

    /**
     * @see EventSink#getSyncLock()
     * @see EventSource#getSyncLock()
     */
    public Object getSyncLock()
    {
        return getBuffer();
    }

    /**
     * Return the underlying buffer for events.
     * 
     * @return the underlying buffer for events.
     */
    protected Buffer getBuffer()
    {
        return _buffer;
    }
}
