package org.codehaus.spice.netevent.events;

import java.nio.channels.Channel;

/**
 * Event to indicate Channel was accepted.
 * 
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2004-05-17 06:21:38 $
 */
public class AcceptEvent
    extends ChannelEvent
{
    /**
     * Create Event.
     * 
     * @param channel the channel
     */
    public AcceptEvent( final Channel channel )
    {
        super( channel, null );
    }
}
