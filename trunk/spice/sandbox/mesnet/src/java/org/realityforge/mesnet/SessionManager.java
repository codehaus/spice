package org.realityforge.mesnet;

/**
 * Responsible for managing sessions for messaging
 * framework.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-11-10 03:17:43 $
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
