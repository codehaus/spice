package org.codehaus.spice.event;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-04-19 08:25:55 $
 */
public class DummyEventSource
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
