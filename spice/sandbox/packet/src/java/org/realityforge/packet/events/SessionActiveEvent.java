package org.realityforge.packet.events;

import org.realityforge.packet.session.Session;

/**
 * Event indicating connect occured for session.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-19 04:45:03 $
 */
public class SessionActiveEvent
    extends SessionEvent
{
    /**
     * Create event.
     * 
     * @param session the session
     */
    public SessionActiveEvent( final Session session )
    {
        super( session );
    }
}
