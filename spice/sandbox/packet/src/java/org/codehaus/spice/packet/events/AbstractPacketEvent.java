package org.codehaus.spice.packet.events;

import org.codehaus.spice.packet.Packet;
import org.codehaus.spice.packet.session.Session;

/**
 * An abstract event about a packet in a session.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-03-22 01:17:50 $
 */
public abstract class AbstractPacketEvent
    extends AbstractSessionEvent
{
    /**
     * The associated packet.
     */
    private final Packet _packet;

    public AbstractPacketEvent( final Session session, final Packet packet )
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
        return super.getEventDescription() + ", sequence=" + _packet.getSequence() + ", count=" + _packet.getData().limit();
    }
}
