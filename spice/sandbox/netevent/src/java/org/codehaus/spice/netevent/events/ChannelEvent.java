package org.codehaus.spice.netevent.events;

import java.nio.channels.Channel;
import org.codehaus.spice.event.AbstractEvent;

/**
 * Abstract event about Channels.
 * 
 * @author Peter Donald
 * @version $Revision: 1.4 $ $Date: 2004-01-22 02:10:47 $
 */
public abstract class ChannelEvent
    extends AbstractEvent
{
    /**
     * The Channel.
     */
    private final Channel m_channel;

    /**
     * The associated userdata.
     */
    private final Object m_userData;

    /**
     * Create Event.
     * 
     * @param channel the channel
     */
    public ChannelEvent( final Channel channel,
                         final Object userData )
    {
        if( null == channel )
        {
            throw new NullPointerException( "channel" );
        }
        m_channel = channel;
        m_userData = userData;
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
     * Return the associated user data.
     * 
     * @return the associated user data.
     */
    public Object getUserData()
    {
        return m_userData;
    }

    /**
     * @see AbstractEvent#getEventDescription()
     */
    protected String getEventDescription()
    {
        return String.valueOf( m_channel );
    }
}
