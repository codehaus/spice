package org.realityforge.connector;

public class NeverPingPolicy
   implements PingPolicy
{
   public boolean checkPingConnection( Connector connector )
   {
      return false;
   }

   public long nextPingCheck( final long lastPingTime )
   {
      return Long.MAX_VALUE;
   }
}
