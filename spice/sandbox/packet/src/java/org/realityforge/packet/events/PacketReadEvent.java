package org.realityforge.packet.events;

import org.realityforge.packet.Packet;
import org.realityforge.packet.session.Session;

/**
 * Event indicating a packet was read.
 * 
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2004-01-19 04:41:53 $
 */
public class PacketReadEvent
    extends PacketEvent
{

    /**
     * Create event.
     * 
     * @param session the session
     * @param packet the packet
     */
    public PacketReadEvent( final Session session,
                            final Packet packet )
    {
        super( session, packet );
    }
}
