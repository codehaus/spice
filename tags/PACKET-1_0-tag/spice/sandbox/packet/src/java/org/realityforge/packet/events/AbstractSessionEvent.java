package org.realityforge.packet.events;

import org.codehaus.spice.event.AbstractEvent;
import org.realityforge.packet.session.Session;

/**
 * An event about a Session.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-02-03 06:09:14 $
 */
public abstract class AbstractSessionEvent
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
    public AbstractSessionEvent( final Session session )
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
