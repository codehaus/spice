package org.realityforge.packet.event.impl;

import org.realityforge.packet.event.EventSource;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-09 05:27:05 $
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
