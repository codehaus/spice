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
 * @version $Revision: 1.2 $ $Date: 2003-12-05 06:57:12 $
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
     * Delete specified session. If session is not managed by this manager then
     * no action is taken.
     *
     * @param session the session
     */
    void deleteSession( Session session );
}
