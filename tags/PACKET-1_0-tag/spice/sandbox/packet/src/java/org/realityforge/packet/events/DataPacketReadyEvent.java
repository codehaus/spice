package org.realityforge.packet.events;

import org.realityforge.packet.Packet;
import org.realityforge.packet.session.Session;

/**
 * Event indicating a packet is ready to be processed by next layer.
 * 
 * @author Peter Donald
 * @version $Revision: 1.4 $ $Date: 2004-02-03 06:19:08 $
 */
public class DataPacketReadyEvent
    extends AbstractPacketEvent
{
    /**
     * Create event.
     * 
     * @param session the session
     * @param packet the packet
     */
    public DataPacketReadyEvent( final Session session,
                                 final Packet packet )
    {
        super( session, packet );
    }
}
