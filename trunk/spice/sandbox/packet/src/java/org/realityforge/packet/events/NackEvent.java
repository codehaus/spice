package org.realityforge.packet.events;

import org.realityforge.packet.session.Session;

/**
 * Event indicating a packet was acked.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-13 06:32:50 $
 */
public class NackEvent
    extends PacketSequenceEvent
{
    /**
     * Create event.
     * 
     * @param session the session
     * @param sequence the sequence
     */
    public NackEvent( final Session session,
                      final short sequence )
    {
        super( session, sequence );
    }
}
