package org.realityforge.packet.events;

import org.realityforge.packet.Packet;
import org.realityforge.packet.session.Session;

/**
 * Event request a packet be written.
 * 
 * @author Peter Donald
 * @version $Revision: 1.4 $ $Date: 2004-02-03 06:19:08 $
 */
public class PacketWriteRequestEvent
    extends AbstractPacketEvent
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
