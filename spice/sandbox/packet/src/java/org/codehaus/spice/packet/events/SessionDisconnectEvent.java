package org.codehaus.spice.packet.events;

import org.codehaus.spice.packet.session.Session;

/**
 * Event indicating disconnect occured for session.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-03-22 01:17:50 $
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
