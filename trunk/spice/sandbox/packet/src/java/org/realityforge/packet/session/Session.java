/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.session;

import org.codehaus.spice.netevent.transport.ChannelTransport;

/**
 * The session object for Client.
 * 
 * @author Peter Donald
 * @version $Revision: 1.5 $ $Date: 2004-01-13 07:00:02 $
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
     * associated Transports that are active. The client has been sent a message
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
    private int _status = STATUS_NOT_CONNECTED;

    /** List of attributes associated with session. */
    private final PacketQueue _messageQueue = new PacketQueue();

    /** The associated transport. */
    private ChannelTransport _transport;

    /** Authentication ID. */
    private final short _authID;

    /**
     * Create session with specified ID.
     * 
     * @param sessionID the session ID
     * @param authID the authID
     */
    public Session( final long sessionID,
                    final short authID )
    {
        _sessionID = sessionID;
        _authID = authID;
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
     * Return the authID.
     * 
     * @return the authID.
     */
    public short getAuthID()
    {
        return _authID;
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
     * Get list of messages that have been transmitted on session but are
     * waiting to be acked.
     * 
     * @return the list of messages
     */
    public PacketQueue getMessageQueue()
    {
        return _messageQueue;
    }

    public ChannelTransport getTransport()
    {
        return _transport;
    }

    public void setTransport( final ChannelTransport transport )
    {
        if( null != _transport )
        {
            _transport.setUserData( null );
            _transport.close();
        }
        _transport = transport;
        if( null != _transport )
        {
            setStatus( Session.STATUS_CONNECTED );
            _transport.setUserData( this );
        }
    }
}
