package org.realityforge.packet.events;

import org.realityforge.packet.Packet;
import org.realityforge.packet.session.Session;

/**
 * Event request a packet be written.
 * 
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-01-19 04:41:53 $
 */
public class PacketWriteRequestEvent
    extends PacketEvent
{
    /**
     * Create event.
     * 
     * @param session the session
     * @param packet the packet
     */
    public PacketWriteRequestEvent( final Session session,
                                    final Packet packet )
    {
        super( session, packet );
    }
}
