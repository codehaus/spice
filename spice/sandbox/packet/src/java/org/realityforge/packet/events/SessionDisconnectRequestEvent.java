package org.realityforge.packet.events;

import org.realityforge.packet.session.Session;

/**
 * Event indicating disconnect requested for session.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-13 04:14:50 $
 */
public class SessionDisconnectRequestEvent
    extends SessionEvent
{
    /**
     * Create event.
     * 
     * @param session the session
     */
    public SessionDisconnectRequestEvent( final Session session )
    {
        super( session );
    }
}
