package org.codehaus.spice.packet.events;

import org.codehaus.spice.packet.Packet;
import org.codehaus.spice.packet.session.Session;

/**
 * Event indicating a packet is ready to be processed by next layer.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-03-22 01:17:50 $
 */
public class DataPacketReadyEvent
    extends AbstractPacketEvent
{
    /**
     * Create event.
     *
     * @param session the session
     * @param packet  the packet
     */
    public DataPacketReadyEvent( final Session session, final Packet packet )
    {
        super( session, packet );
    }
}
