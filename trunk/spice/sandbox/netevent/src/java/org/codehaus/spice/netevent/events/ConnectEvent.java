package org.codehaus.spice.netevent.events;

import org.codehaus.spice.netevent.transport.ChannelTransport;

/**
 * Event to indicate Channel was accepted.
 * 
 * @author Peter Donald
 * @version $Revision: 1.6 $ $Date: 2004-05-17 06:21:38 $
 */
public class ConnectEvent
    extends AbstractTransportEvent
{
    /**
     * Create Event.
     * 
     * @param transport the transport
     */
    public ConnectEvent( final ChannelTransport transport )
    {
        super( transport );
    }
}
