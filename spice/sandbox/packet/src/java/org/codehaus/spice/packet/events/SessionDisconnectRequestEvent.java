package org.codehaus.spice.packet.events;

import org.codehaus.spice.packet.session.Session;

/**
 * Event indicating disconnect requested for session.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-03-22 01:17:50 $
 */
public class SessionDisconnectRequestEvent
    extends AbstractSessionEvent
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
