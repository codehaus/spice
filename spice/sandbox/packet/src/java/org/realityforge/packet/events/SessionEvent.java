package org.realityforge.packet.events;

import org.codehaus.spice.netevent.events.AbstractEvent;
import org.realityforge.packet.session.Session;

/**
 * An event about a Session.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-13 04:14:50 $
 */
public abstract class SessionEvent
    extends AbstractEvent
{
    /** The session. */
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
        return "SessionID: " + getSession().getSessionID();
    }
}
