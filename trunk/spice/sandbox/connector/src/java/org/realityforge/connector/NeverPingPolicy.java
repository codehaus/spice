package org.realityforge.connector;

/**
 * Ping policy for never performing ping.
 */
public class NeverPingPolicy
   implements PingPolicy
{
   /**
    * Constant containing instance of never ping policy.
    */
   public static final NeverPingPolicy POLICY = new NeverPingPolicy();

   /**
    * @see PingPolicy#checkPingConnection
    */
   public boolean checkPingConnection( Connector connector )
   {
      return false;
   }

   /**
    * @see PingPolicy#nextPingCheck
    */
   public long nextPingCheck( final long lastPingTime )
   {
      return Long.MAX_VALUE;
   }
}
