/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.session;

/**
 * Responsible for managing sessions for messaging framework.
 *
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2004-02-03 04:08:04 $
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
     * return the number of sessions.
     *
     * @return the number of sessions
     */
    int getSessionCount();

    /**
     * Create a new session.
     *
     * @return the new session
     */
    Session newSession();

    /**
     * Delete specified session. If session is not managed by this manager then
     * no action is taken.
     *
     * @param session the session
     */
    void deleteSession( Session session );
}
