package org.realityforge.packet.events;

import org.realityforge.packet.session.Session;

/**
 * Event indicating disconnect occured for session.
 * 
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-02-03 06:09:14 $
 */
public class SessionDisconnectEvent
    extends AbstractSessionEvent
{
    /**
     * Create event.
     * 
     * @param session the session
     */
    public SessionDisconnectEvent( final Session session )
    {
        super( session );
    }
}
