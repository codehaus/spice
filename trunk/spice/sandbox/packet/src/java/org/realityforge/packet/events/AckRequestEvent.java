package org.realityforge.packet.events;

import org.realityforge.packet.session.Session;

/**
 * Event indicating a packet was acked.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-14 01:39:21 $
 */
public class AckRequestEvent
    extends PacketSequenceEvent
{
    /**
     * Create event.
     * 
     * @param session the session
     * @param sequence the sequence
     */
    public AckRequestEvent( final Session session,
                            final short sequence )
    {
        super( session, sequence );
    }
}
