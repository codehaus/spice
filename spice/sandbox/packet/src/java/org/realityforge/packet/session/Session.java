/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.session;

import java.util.HashMap;
import java.util.Map;

/**
 * The session object for Client.
 * 
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003-12-17 04:12:12 $
 */
public class Session
{
    /**
     * Status indicating client is not yet connected. Should only be set when
     * session is initially created.
     */
    public static final int STATUS_NOT_CONNECTED = 0;

    /**
     * Status indicating the Client is connected and there is one or more
     * associated Transports that are active. A message has been sent a message
     * indicating that they are connected.
     */
    public static final int STATUS_CONNECTED = 1;

    /** Status indicating the Client has acknowledged that they are connected. */
    public static final int STATUS_ESTABLISHED = 2;

    /** Status indicating transport is no longer active. */
    public static final int STATUS_LOST = 3;

    /** Status indicating client is no longer connected. */
    public static final int STATUS_DISCONNECTED = 4;

    /** A unique id for this particula session. */
    private final long _sessionID;

    /** Time at which session status last changed. */
    private long _timeOfLastStatusChange;

    /** Status of session. Must be one of the STATUS_* constants. */
    private int _status;

    /** List of attributes associated with session. */
    private final Map _attributes = new HashMap();

    /**
     * Create session with specified ID.
     * 
     * @param sessionID the session ID
     */
    public Session( final long sessionID )
    {
        _sessionID = sessionID;
    }

    /**
     * Return the session ID.
     * 
     * @return the session ID.
     */
    public long getSessionID()
    {
        return _sessionID;
    }

    /**
     * Return the the status.
     * 
     * @return the the status.
     */
    public int getStatus()
    {
        return _status;
    }

    /**
     * Set the sessions status.
     * 
     * @param status the status.
     */
    public void setStatus( final int status )
    {
        _status = status;
        _timeOfLastStatusChange = System.currentTimeMillis();
    }

    /**
     * Return the time at which the status of session last changed.
     * 
     * @return the time at which the status of session last changed.
     */
    public long getTimeOfLastStatusChange()
    {
        return _timeOfLastStatusChange;
    }

    /**
     * Return the attribute with specified key or null if no such attribute.
     * 
     * @param key the attributes key
     * @return the attribute with specified key or null if no such attribute.
     */
    public Object getAttribute( final String key )
    {
        return _attributes.get( key );
    }

    /**
     * Set attribute with specified key to specified value.
     * 
     * @param key the key
     * @param value the value
     */
    public void setAttribute( final String key,
                              final Object value )
    {
        _attributes.put( key, value );
    }
}
