package org.realityforge.packet.event.impl;

import org.realityforge.packet.event.EventSink;
import org.realityforge.packet.event.EventSource;
import org.realityforge.packet.event.impl.collections.Buffer;

/**
 * An event queue that acts as a Source and Sink of events.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-05 02:15:16 $
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
        final int size = getBuffer().size();
        if( size > 0 )
        {
            return getBuffer().pop();
        }
        else
        {
            return null;
        }
    }

    /**
     * @see EventSource#getEvents(int)
     */
    public Object[] getEvents( final int count )
    {
        final Buffer buffer = getBuffer();
        final int size = buffer.size();
        final int resultCount = Math.min( size, count );
        final Object[] objects = new Object[ resultCount ];
        for( int i = 0; i < objects.length; i++ )
        {
            objects[ i ] = buffer.pop();
        }
        return objects;
    }

    /**
     * @see EventSink#addEvent(Object)
     */
    public boolean addEvent( final Object event )
    {
        return getBuffer().add( event );
    }

    /**
     * @see EventSink#addEvents(Object[])
     */
    public boolean addEvents( final Object[] events )
    {
        return getBuffer().addAll( events );
    }

    /**
     * @see EventSink#getSyncLock()
     * @see EventSource#getSyncLock()
     */
    public Object getSyncLock()
    {
        return _buffer;
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
