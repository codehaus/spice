package org.codehaus.spice.event.impl;

import org.codehaus.spice.event.EventJoin;
import org.codehaus.spice.event.EventSource;

/**
 * An abstract class for implementing EventSource objects.
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-22 02:37:19 $
 */
public abstract class AbstractEventSource
    implements EventSource
{
    /**
     * The underlying join to add events to.
     */
    private final EventJoin _join;

    /**
     * Create Source.
     *
     * @param join the underlying join.
     */
    protected AbstractEventSource( final EventJoin join )
    {
        if( null == join )
        {
            throw new NullPointerException( "join" );
        }
        _join = join;
    }

    /**
     * Return the underlying EventJoin.
     *
     * @return the underlying EventJoin.
     */
    protected EventJoin getJoin()
    {
        return _join;
    }

    /**
     * @see EventSource#getEvent()
     */
    public Object getEvent()
    {
        refresh();
        return _join.getEvent();
    }

    /**
     * @see EventSource#getEvents(int)
     */
    public Object[] getEvents( final int count )
    {
        refresh();
        return _join.getEvents( count );
    }

    /**
     * @see EventSource#getSourceLock()
     */
    public Object getSourceLock()
    {
        return _join.getSourceLock();
    }

    /**
     * Open associated resources.
     *
     * @throws Exception if unable to open resources
     */
    public void open()
        throws Exception
    {
    }

    /**
     * Close associated resources.
     *
     * @throws Exception if unable to close resources
     */
    public void close()
        throws Exception
    {
    }

    /**
     * Perform refresh on the underlying resources and thus populate the
     * EventJoin.
     */
    protected abstract void refresh();
}
