/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.sca.connector;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003-12-05 05:39:34 $
 */
class MockConnector
    extends Connector
{
    private final long _lastTxTime;
    private final long _lastRxTime;
    private long _lastPingTime;
    private int _pingCount;

    MockConnector( final long lastTxTime,
                   final long lastRxTime,
                   final long lastPingTime )
    {
        _lastTxTime = lastTxTime;
        _lastRxTime = lastRxTime;
        _lastPingTime = lastPingTime;
    }

    public long getLastPingTime()
    {
        return _lastPingTime;
    }

    public long getLastTxTime()
    {
        return _lastTxTime;
    }

    public long getLastRxTime()
    {
        return _lastRxTime;
    }

    public boolean ping()
    {
        _lastPingTime = System.currentTimeMillis();
        _pingCount++;
        return true;
    }

    int getPingCount()
    {
        return _pingCount;
    }
}
