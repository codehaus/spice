/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.session;

import java.nio.ByteBuffer;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.transport.ChannelTransport;
import org.codehaus.spice.timeevent.source.SchedulingKey;
import org.realityforge.packet.Packet;
import org.realityforge.packet.events.PacketWriteRequestEvent;

/**
 * The session object for Client.
 * 
 * @author Peter Donald
 * @version $Revision: 1.20 $ $Date: 2004-02-06 02:58:25 $
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

    /**
     * Status indicating the Client has acknowledged that they are connected.
     */
    public static final int STATUS_ESTABLISHED = 2;

    /**
     * Status indicating transport is no longer active.
     */
    public static final int STATUS_LOST = 3;

    /**
     * Status indicating client is no longer connected.
     */
    public static final int STATUS_DISCONNECTED = 4;

    private final PacketQueue _txQueue = new PacketQueue();

    private final PacketQueue _rxQueue = new PacketQueue();

    /**
     * A unique id for this particula session.
     */
    private long _sessionID;

    /**
     * Authentication ID.
     */
    private short _authID;

    /**
     * Time at which session status last changed.
     */
    private long _timeOfLastStatusChange;

    /**
     * Status of session. Must be one of the STATUS_* constants.
     */
    private int _status = STATUS_NOT_CONNECTED;

    /**
     * The associated transport.
     */
    private ChannelTransport _transport;

    /**
     * Flag indicating whether this is the serverside or clientside session.
     */
    private boolean _client;

    /**
     * Flag indicating whether the session has received any data.
     */
    private boolean _hasReceivedData;

    /**
     * Flag indicating whether the session has received any data.
     */
    private boolean _hasTransmittedData;

    /**
     * Flag indicating whether the session will be disconencted when last
     * messages transmitted.
     */
    private boolean _disconnectRequested;

    /**
     * Flag indicating whether the session will be disconencted when last
     * messages transmitted.
     */
    private boolean _pendingDisconnect;

    /**
     * The sequence of the last packet processed.
     */
    private short _lastPacketProcessed;

    /**
     * The sequence of the last packet received.
     */
    private short _lastPacketReceived;

    /**
     * The sequence of the last packet transmitted.
     */
    private short _lastPacketTransmitted;

    /**
     * The userdata associated with the session.
     */
    private Object _userData;

    private SchedulingKey _timeoutKey;

    /**
     * The EventSink that this Session sends events to.
     */
    private EventSink _sink;

    /**
     * Flag set to true when need to send acke for last received
     * packetProcessed.
     */
    private boolean _needsToSendAck;

    /**
     * Create Serverside session with specified ID.
     * 
     * @param sessionID the session ID
     * @param authID the authID
     */
    public Session( final long sessionID,
                    final short authID )
    {
        this( sessionID, authID, false );
    }

    /**
     * Create clientside session.
     */
    public Session()
    {
        this( -1, (short)0, true );
    }

    /**
     * Create session with specified ID.
     * 
     * @param sessionID the session ID
     * @param authID the authID
     * @param client the client flag
     */
    protected Session( final long sessionID,
                       final short authID,
                       final boolean client )
    {
        _sessionID = sessionID;
        _authID = authID;
        _client = client;
    }

    public void setSink( final EventSink sink )
    {
        _sink = sink;
    }

    public void setTimeoutKey( final SchedulingKey timeoutKey )
    {
        cancelTimeout();
        _timeoutKey = timeoutKey;
    }

    public void cancelTimeout()
    {
        if( null != _timeoutKey )
        {
            _timeoutKey.cancel();
            _timeoutKey = null;
        }
    }

    public boolean hasTransmittedData()
    {
        return _hasTransmittedData;
    }

    public boolean isDisconnectRequested()
    {
        return _disconnectRequested;
    }

    public void setDisconnectRequested()
    {
        _disconnectRequested = true;
    }

    public boolean isPendingDisconnect()
    {
        return _pendingDisconnect;
    }

    public void setPendingDisconnect()
    {
        _pendingDisconnect = true;
    }

    public SchedulingKey getTimeoutKey()
    {
        return _timeoutKey;
    }

    public short getLastPacketReceived()
    {
        return _lastPacketReceived;
    }

    public void setLastPacketReceived( final short lastPacketReceived )
    {
        _lastPacketReceived = lastPacketReceived;
    }

    public boolean hasReceivedData()
    {
        return _hasReceivedData;
    }

    /**
     * Return the sequence of the last packet processed.
     * 
     * @return the sequence of the last packet processed.
     */
    public short getLastPacketProcessed()
    {
        return _lastPacketProcessed;
    }

    /**
     * Set the sequence of the last packet processed.
     * 
     * @param lastPacketProcessed the sequence of the last packet processed.
     */
    public void setLastPacketProcessed( final short lastPacketProcessed )
    {
        if( lastPacketProcessed != _lastPacketProcessed )
        {
            _hasReceivedData = true;
            _needsToSendAck = true;
            _lastPacketProcessed = lastPacketProcessed;
        }
    }

    /**
     * Return true if session is clientside.
     * 
     * @return true if session is clientside.
     */
    public boolean isClient()
    {
        return _client;
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
     * Set the sessionID.
     * 
     * @param sessionID the sessionID
     */
    public void setSessionID( final long sessionID )
    {
        _sessionID = sessionID;
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
     * Set the AuthID.
     * 
     * @param authID the AuthID.
     */
    public void setAuthID( final short authID )
    {
        _authID = authID;
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
        cancelTimeout();
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
    public PacketQueue getTransmitQueue()
    {
        return _txQueue;
    }

    public PacketQueue getReceiveQueue()
    {
        return _rxQueue;
    }

    public short getLastPacketTransmitted()
    {
        return _lastPacketTransmitted;
    }

    public void acked()
    {
        _needsToSendAck = false;
    }

    public boolean needsToSendAck()
    {
        return _needsToSendAck;
    }

    public boolean sendPacket( final ByteBuffer buffer )
    {
        if( isPendingDisconnect() || isDisconnectRequested() )
        {
            return false;
        }
        else
        {
            final Packet packet =
                new Packet( ++_lastPacketTransmitted, 0, buffer );
            _hasTransmittedData = true;
            final PacketWriteRequestEvent ev =
                new PacketWriteRequestEvent( this, packet );
            _sink.addEvent( ev );
            return true;
        }
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
            _transport.getInputStream().close();
            _transport.getOutputStream().close();
        }
        _needsToSendAck = true;
        _transport = transport;
        if( null != _transport )
        {
            setStatus( Session.STATUS_CONNECTED );
            _transport.setUserData( this );
        }
        else
        {
            if( Session.STATUS_DISCONNECTED != _status )
            {
                setStatus( Session.STATUS_LOST );
            }
        }
    }

    public Object getUserData()
    {
        return _userData;
    }

    public void setUserData( final Object userData )
    {
        _userData = userData;
    }

    public String toString()
    {
        final long sessionID = getSessionID();
        final int transportID = (_transport != null) ? _transport.getId() : -1;
        return "Session[SessionID=" + sessionID +
               ", TransportID=" + transportID +
               ", UserData=" + getUserData() +
               ", IsClient=" + isClient() +
               "]";
    }
}
