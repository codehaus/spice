package org.realityforge.packet.events;

import org.realityforge.packet.Packet;
import org.realityforge.packet.session.Session;

/**
 * Event indicating a packet was read.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-13 05:56:23 $
 */
public class PacketReadEvent
    extends SessionEvent
{
    /** The associated packet. */
    private final Packet _packet;

    /**
     * Create event.
     * 
     * @param session the session
     * @param packet the packet
     */
    public PacketReadEvent( final Session session,
                            final Packet packet )
    {
        super( session );
        if( null == packet )
        {
            throw new NullPointerException( "packet" );
        }
        _packet = packet;
    }

    /**
     * Return the packet.
     * 
     * @return the packet.
     */
    public Packet getPacket()
    {
        return _packet;
    }
}
