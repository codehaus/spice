package org.realityforge.packet.events;

import org.realityforge.packet.Packet;
import org.realityforge.packet.session.Session;

/**
 * Event indicating a packet is ready to be processed by next layer.
 * 
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2004-02-03 06:09:14 $
 */
public class DataPacketReadyEvent
    extends AbstractSessionEvent
{
    /**
     * The associated packet.
     */
    private final Packet _packet;

    /**
     * Create event.
     * 
     * @param session the session
     * @param packet the packet
     */
    public DataPacketReadyEvent( final Session session,
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

    /**
     * @see AbstractSessionEvent#getEventDescription()
     */
    protected String getEventDescription()
    {
        return super.getEventDescription() +
               ", Sequence=" + _packet.getSequence() +
               ", Size=" + _packet.getData().limit();
    }
}
