package org.realityforge.packet.session;

import org.realityforge.packet.session.Session;

/**
 * Responsible for managing sessions for messaging
 * framework.
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-05 03:25:38 $
 */
public interface SessionManager
{
    /**
     * Find the session with the specified id.
     *
     * @param id the session ID
     * @return the4 session or null if no such session
     */
    Session findSession( long id );

    /**
     * Create a new session.
     *
     * @return the new session
     */
    Session newSession();

    /**
     * Delete specified session.
     * If session is not managed by this
     * manager then no action is taken.
     *
     * @param session the session
     */
    void deleteSession( Session session );
}
