package org.realityforge.sca.connector;

/**
 * Ping policy that pings when Connector is innactive for a period of time.
 */
public class InactivityPingPolicy
    implements PingPolicy
{
    /**
     * The period of transmission inactivity that will force a ping. -1 means
     * that inactivitity not monitored for transmissions.
     */
    private final long _txInactivity;

    /**
     * The period of retrieve inactivity that will force a ping. -1 means that
     * inactivitity not monitored for receives.
     */
    private final long _rxInactivity;

    /** The associated connector. */
    private final Connector _connector;

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
        _txInactivity = txInactivity;
        _rxInactivity = rxInactivity;
        _connector = connector;
    }

    /**
     * @see PingPolicy#checkPingConnection()
     */
    public boolean checkPingConnection()
    {
        final long now = System.currentTimeMillis();

        if( -1 != _txInactivity )
        {
            final long period = now - calcLastTxTime();
            if( period > _txInactivity )
            {
                return true;
            }
        }

        if( -1 != _rxInactivity )
        {
            final long period = now - calcLastRxTime();
            if( period > _rxInactivity )
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
        if( -1 != _txInactivity )
        {
            txPeriod = calcLastTxTime() + _txInactivity;
        }
        else
        {
            txPeriod = Long.MAX_VALUE;
        }

        final long rxPeriod;
        if( -1 != _rxInactivity )
        {
            rxPeriod = calcLastRxTime() + _rxInactivity;
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
        final long lastRxTime = _connector.getLastRxTime();
        final long lastConnectionTime = _connector.getLastConnectionTime();
        final long lastPingTime = _connector.getLastPingTime();
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
        final long lastTxTime = _connector.getLastTxTime();
        final long lastConnectionTime = _connector.getLastConnectionTime();
        final long lastPingTime = _connector.getLastPingTime();
        return Math.max( lastPingTime,
                         Math.max( lastTxTime, lastConnectionTime ) );
    }
}
