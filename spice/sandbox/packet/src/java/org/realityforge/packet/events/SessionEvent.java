package org.realityforge.packet.events;

import org.codehaus.spice.event.AbstractEvent;
import org.codehaus.spice.netevent.transport.ChannelTransport;
import org.realityforge.packet.session.Session;

/**
 * An event about a Session.
 * 
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2004-01-22 05:52:16 $
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
        final Session session = getSession();
        final ChannelTransport transport = session.getTransport();
        final int transportID = (null != transport) ? transport.getId() : -1;
        return "SessionID=" + session.getSessionID() +
               ", TransportID=" + transportID;
    }
}
