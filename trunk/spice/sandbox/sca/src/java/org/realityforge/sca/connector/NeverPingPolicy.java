package org.realityforge.sca.connector;

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
   public boolean checkPingConnection()
   {
      return false;
   }

   /**
    * @see PingPolicy#nextPingCheck
    */
   public long nextPingCheck()
   {
      return Long.MAX_VALUE;
   }
}
