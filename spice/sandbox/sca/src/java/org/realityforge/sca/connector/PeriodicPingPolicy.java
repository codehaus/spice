package org.realityforge.sca.connector;

/**
 * Ping policy that pings on specified period.
 */
public class PeriodicPingPolicy
    implements PingPolicy
{
    /** The period between pings. */
    private final long _period;

    /** The associated connector. */
    private final Connector _connector;

    /**
     * Create policy with specified period.
     * 
     * @param period the period
     * @param connector the associated connector
     */
    public PeriodicPingPolicy( final long period,
                               final Connector connector )
    {
        _period = period;
        _connector = connector;
    }

    /**
     * @see PingPolicy#checkPingConnection
     */
    public boolean checkPingConnection()
    {
        return true;
    }

    /**
     * @see PingPolicy#nextPingCheck
     */
    public long nextPingCheck()
    {
        return _connector.getLastPingTime() + _period;
    }
}
