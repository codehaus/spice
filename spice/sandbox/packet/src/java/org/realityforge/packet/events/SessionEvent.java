package org.realityforge.packet.events;

import org.codehaus.spice.event.AbstractEvent;
import org.realityforge.packet.session.Session;

/**
 * An event about a Session.
 * 
 * @author Peter Donald
 * @version $Revision: 1.4 $ $Date: 2004-01-29 05:48:23 $
 */
public abstract class SessionEvent
    extends AbstractEvent
{
    /**
     * The session.
     */
    private final Session _session;

    /**
     * Create event.
     * 
     * @param session the session
     */
    public SessionEvent( final Session session )
    {
        if( null == session )
        {
            throw new NullPointerException( "session" );
        }
        _session = session;
    }

    /**
     * Return the session.
     * 
     * @return the session.
     */
    public Session getSession()
    {
        return _session;
    }

    /**
     * @see AbstractEvent#getEventDescription()
     */
    protected String getEventDescription()
    {
        return String.valueOf( getSession() );
    }
}
