package org.realityforge.packet.events;

import org.realityforge.packet.Packet;
import org.realityforge.packet.session.Session;

/**
 * An abstract event about a packet in a session.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-19 04:41:53 $
 */
public abstract class PacketEvent
    extends SessionEvent
{
    /** The associated packet. */
    private final Packet _packet;

    public PacketEvent( final Session session,
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
     * @see SessionEvent#getEventDescription()
     */
    protected String getEventDescription()
    {
        return super.getEventDescription() +
               " Sequence: " + _packet.getSequence();
    }
}
