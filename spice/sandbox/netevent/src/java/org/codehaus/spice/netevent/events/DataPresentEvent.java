package org.codehaus.spice.netevent.events;

import org.codehaus.spice.netevent.transport.ChannelTransport;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2004-01-20 01:08:30 $
 */
public abstract class DataPresentEvent
    extends AbstractTransportEvent
{
    /** The number of bytes present. */
    private final int _count;

    public DataPresentEvent( final ChannelTransport transport,
                             final int count )
    {
        super( transport );
        _count = count;
    }

    /**
     * @see AbstractTransportEvent#getEventDescription()
     */
    protected String getEventDescription()
    {
        return "Count: " + _count + " " + super.getEventDescription();
    }
}
