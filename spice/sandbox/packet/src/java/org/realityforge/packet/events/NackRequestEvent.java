package org.realityforge.packet.events;

import org.realityforge.packet.session.Session;

/**
 * Event indicating a request to write a nack.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-14 02:58:55 $
 */
public class NackRequestEvent
    extends PacketSequenceEvent
{
    /**
     * Create event.
     * 
     * @param session the session
     * @param sequence the sequence
     */
    public NackRequestEvent( final Session session,
                             final short sequence )
    {
        super( session, sequence );
    }
}
