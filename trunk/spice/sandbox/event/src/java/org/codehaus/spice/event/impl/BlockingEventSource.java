package org.codehaus.spice.event.impl;

import org.codehaus.spice.event.EventSource;

/**
 * A BlockingEventSource will wait until notified prior to attempting to
 * retrieve events from the underlying EventSource.
 *
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2004-05-17 03:52:23 $
 */
public class BlockingEventSource
    implements EventSource
{
    /**
     * The underlying EventSource to retrieve events from.
     */
    private final EventSource m_source;

    /**
     * The duration to wait before unblocking.
     */
    private long m_duration;

    /**
     * Create an instance.
     *
     * @param source the source to wrap.
     */
    public BlockingEventSource( final EventSource source, final long duration )
    {
        if( null == source )
        {
            throw new NullPointerException( "source" );
        }
        m_source = source;
        m_duration = duration;
    }

    /**
     * @see EventSource#getEvent()
     */
    public Object getEvent()
    {
        final Object lock = getSourceLock();
        synchronized( lock )
        {
            Object result = m_source.getEvent();
            if( null == result )
            {
                try
                {
                    lock.wait( m_duration );
                }
                catch( final InterruptedException e )
                {
                }
                result = m_source.getEvent();
            }
            return result;
        }
    }

    /**
     * @see EventSource#getEvents(int)
     */
    public Object[] getEvents( final int count )
    {
        final Object lock = getSourceLock();
        synchronized( lock )
        {
            Object[] results = m_source.getEvents( count );
            if( 0 == results.length )
            {
                try
                {
                    lock.wait( m_duration );
                }
                catch( final InterruptedException ie )
                {
                }
                results = m_source.getEvents( count );
            }
            return results;
        }
    }

    /**
     * @see EventSource#getSourceLock()
     */
    public Object getSourceLock()
    {
        return m_source.getSourceLock();
    }
}
