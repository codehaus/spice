/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.session;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A basic in memory SessionManager implementation.
 * 
 * @author Peter Donald
 * @version $Revision: 1.6 $ $Date: 2004-02-06 04:04:56 $
 */
public class DefaultSessionManager
    implements SessionManager
{
    /**
     * generator for authentication IDs.
     */
    private final Random _generator = new Random();

    /**
     * Map of sessionIDs to sessions.
     */
    private final Map _sessions = new HashMap();

    /**
     * The last sessionID allocated. The next session ID will just be the next
     * in sequence.
     */
    private long _lastSessionID = 1;

    /**
     * @see SessionManager#findSession(long)
     */
    public Session findSession( final long id )
    {
        return (Session)_sessions.get( new Long( id ) );
    }

    /**
     * @see SessionManager#getSessionCount()
     */
    public int getSessionCount()
    {
        return _sessions.size();
    }

    /**
     * @see SessionManager#newSession()
     */
    public synchronized Session newSession()
    {
        final Session session =
            new Session( _lastSessionID++,
                         (short)_generator.nextInt() );
        _sessions.put( new Long( session.getSessionID() ),
                       session );
        return session;
    }

    public Collection getSessionIDs()
    {
        return _sessions.keySet();
    }

    /**
     * @see SessionManager#deleteSession(Session)
     */
    public void deleteSession( final Session session )
    {
        _sessions.remove( new Long( session.getSessionID() ) );
    }
}
