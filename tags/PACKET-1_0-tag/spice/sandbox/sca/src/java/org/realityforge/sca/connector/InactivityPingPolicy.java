/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.sca.connector;

/**
 * Ping policy that pings when Connector is innactive for a period of time.
 *
 * @author Peter Donald
 * @version $Revision: 1.5 $ $Date: 2003-12-05 05:39:33 $
 */
public class InactivityPingPolicy
    implements PingPolicy
{
    /**
     * The period of transmission inactivity that will force a ping. -1 means
     * that inactivitity not monitored for transmissions.
     */
    private final long m_txInactivity;

    /**
     * The period of retrieve inactivity that will force a ping. -1 means that
     * inactivitity not monitored for receives.
     */
    private final long m_rxInactivity;

    /** The associated connector. */
    private final Connector m_connector;

    /**
     * Create policy with specified periods.
     *
     * @param period the inactivity period for either receivees or
     * transmissions
     * @param connector the associated connector
     */
    public InactivityPingPolicy( final long period,
                                 final Connector connector )
    {
        this( period, period, connector );
    }

    /**
     * Create policy with specified periods.
     *
     * @param txInactivity the inactivity period for transmissions
     * @param rxInactivity the inactivity period for receives
     * @param connector the associated connector
     */
    public InactivityPingPolicy( final long txInactivity,
                                 final long rxInactivity,
                                 final Connector connector )
    {
        m_txInactivity = txInactivity;
        m_rxInactivity = rxInactivity;
        m_connector = connector;
    }

    /**
     * @see PingPolicy#checkPingConnection()
     */
    public boolean checkPingConnection()
    {
        final long now = System.currentTimeMillis();

        if( -1 != m_txInactivity )
        {
            final long period = now - calcLastTxTime();
            if( period > m_txInactivity )
            {
                return true;
            }
        }

        if( -1 != m_rxInactivity )
        {
            final long period = now - calcLastRxTime();
            if( period > m_rxInactivity )
            {
                return true;
            }
        }

        return false;
    }

    /**
     * @see PingPolicy#nextPingCheck()
     */
    public long nextPingCheck()
    {
        final long txPeriod;
        if( -1 != m_txInactivity )
        {
            txPeriod = calcLastTxTime() + m_txInactivity;
        }
        else
        {
            txPeriod = Long.MAX_VALUE;
        }

        final long rxPeriod;
        if( -1 != m_rxInactivity )
        {
            rxPeriod = calcLastRxTime() + m_rxInactivity;
        }
        else
        {
            rxPeriod = Long.MAX_VALUE;
        }

        return Math.min( txPeriod, rxPeriod );
    }

    /**
     * Get last time of RX transmission. The time is either the last RX time,
     * the last connection  time or the last ping time.
     *
     * @return the last rx time
     */
    private long calcLastRxTime()
    {
        final long lastRxTime = m_connector.getLastRxTime();
        final long lastConnectionTime = m_connector.getLastConnectionTime();
        final long lastPingTime = m_connector.getLastPingTime();
        return Math.max( lastPingTime,
                         Math.max( lastRxTime, lastConnectionTime ) );
    }

    /**
     * Get last time of TX transmission. The time is either the last TX time,
     * the last connection  time or the last ping time.
     *
     * @return the last tx time
     */
    private long calcLastTxTime()
    {
        final long lastTxTime = m_connector.getLastTxTime();
        final long lastConnectionTime = m_connector.getLastConnectionTime();
        final long lastPingTime = m_connector.getLastPingTime();
        return Math.max( lastPingTime,
                         Math.max( lastTxTime, lastConnectionTime ) );
    }
}
