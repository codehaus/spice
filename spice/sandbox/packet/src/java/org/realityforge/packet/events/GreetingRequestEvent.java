package org.realityforge.packet.events;

import org.realityforge.packet.session.Session;

/**
 * Event indicating connect occured for session.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-15 03:28:28 $
 */
public class GreetingRequestEvent
    extends SessionEvent
{
    /**
     * Create event.
     * 
     * @param session the session
     */
    public GreetingRequestEvent( final Session session )
    {
        super( session );
    }
}
