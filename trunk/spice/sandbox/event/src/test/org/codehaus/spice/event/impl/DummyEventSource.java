package org.codehaus.spice.event.impl;

import org.codehaus.spice.event.EventSource;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-16 02:03:12 $
 */
class DummyEventSource
    implements EventSource
{
    public Object[] getEvents( final int count )
    {
        return new Object[]{getEvent()};
    }

    public Object getSourceLock()
    {
        return this;
    }

    public Object getEvent()
    {
        return "Event";
    }
}
