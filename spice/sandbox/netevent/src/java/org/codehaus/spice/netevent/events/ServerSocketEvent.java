package org.codehaus.spice.netevent.events;

import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * Abstract event about ServerSockets.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-08 03:41:13 $
 */
public abstract class ServerSocketEvent
    extends AbstractEvent
{
    /** The ServerSocketChannel. */
    private final ServerSocketChannel m_channel;

    /**
     * Create Event.
     * 
     * @param channel the channel
     */
    public ServerSocketEvent( final ServerSocketChannel channel )
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
    public ServerSocketChannel getChannel()
    {
        return m_channel;
    }

    /**
     * @see AbstractEvent#getEventDescription()
     */
    protected String getEventDescription()
    {
        final SocketAddress address =
            m_channel.socket().getLocalSocketAddress();
        return String.valueOf( address );
    }
}
