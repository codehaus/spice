package org.realityforge.packet.events;

import org.realityforge.packet.Packet;
import org.realityforge.packet.session.Session;

/**
 * Event indicating a packet was read.
 * 
 * @author Peter Donald
 * @version $Revision: 1.4 $ $Date: 2004-02-03 06:09:14 $
 */
public class PacketReadEvent
    extends AbstractPacketEvent
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
