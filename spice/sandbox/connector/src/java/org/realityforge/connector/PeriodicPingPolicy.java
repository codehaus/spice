package org.realityforge.connector;

/**
 * Ping policy that pings on specified period.
 */
public class PeriodicPingPolicy
   implements PingPolicy
{
   /**
    * The period between pings.
    */
   private final long _period;

   /**
    * Create policy with specified period.
    *
    * @param period the period
    */
   public PeriodicPingPolicy( final long period )
   {
      _period = period;
   }

   /**
    * @see org.realityforge.connector.PingPolicy#checkPingConnection
    */
   public boolean checkPingConnection( Connector connector )
   {
      return true;
   }

   /**
    * @see org.realityforge.connector.PingPolicy#nextPingCheck
    */
   public long nextPingCheck( final long lastPingTime )
   {
      return lastPingTime + _period;
   }
}
