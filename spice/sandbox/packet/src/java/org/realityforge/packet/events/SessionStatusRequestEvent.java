package org.realityforge.packet.events;

import org.realityforge.packet.session.Session;

/**
 * Event indicating connect occured for session.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-29 05:48:23 $
 */
public class SessionStatusRequestEvent
    extends SessionEvent
{
    /**
     * Create event.
     * 
     * @param session the session
     */
    public SessionStatusRequestEvent( final Session session )
    {
        super( session );
    }
}
