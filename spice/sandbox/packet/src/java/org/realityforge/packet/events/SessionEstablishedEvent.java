package org.realityforge.packet.events;

import org.realityforge.packet.session.Session;

/**
 * Event indicating connect occured for session.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-13 06:43:35 $
 */
public class SessionEstablishedEvent
    extends SessionEvent
{
    /**
     * Create event.
     * 
     * @param session the session
     */
    public SessionEstablishedEvent( final Session session )
    {
        super( session );
    }
}
