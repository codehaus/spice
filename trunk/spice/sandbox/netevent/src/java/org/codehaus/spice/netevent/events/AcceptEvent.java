package org.codehaus.spice.netevent.events;

import java.nio.channels.Channel;

/**
 * Event to indicate Channel was accepted.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-08 04:03:58 $
 */
public class AcceptEvent
    extends AbstractEvent
{
    /** The Channel. */
    private final Channel m_channel;

    /**
     * Create Event.
     * 
     * @param channel the channel
     */
    public AcceptEvent( final Channel channel )
    {
        if( null == channel )
        {
            throw new NullPointerException( "channel" );
        }
        m_channel = channel;
    }

    /**
     * Return the socket channel.
     * 
     * @return the socket channel.
     */
    public Channel getChannel()
    {
        return m_channel;
    }

    /**
     * @see AbstractEvent#getEventDescription()
     */
    protected String getEventDescription()
    {
        return String.valueOf( m_channel );
    }
}
