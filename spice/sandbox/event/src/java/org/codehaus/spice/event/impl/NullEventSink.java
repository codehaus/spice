package org.codehaus.spice.event.impl;

import org.codehaus.spice.event.EventSink;

/**
 * A Null implementation of EventSink interface.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-12 03:03:50 $
 */
public class NullEventSink
    implements EventSink
{
    /**
     * @see EventSink#addEvent(Object)
     */
    public boolean addEvent( final Object event )
    {
        return false;
    }

    /**
     * @see EventSink#addEvents(Object[])
     */
    public boolean addEvents( final Object[] events )
    {
        return false;
    }

    /**
     * @see EventSink#getSinkLock()
     */
    public Object getSinkLock()
    {
        return this;
    }
}
