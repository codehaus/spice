package org.realityforge.connector;

import junit.framework.TestCase;

public class PeriodicPingPolicyTestCase
   extends TestCase
{
   public void testNeverPingPolicy()
      throws Exception
   {
      final long lastPingTime = System.currentTimeMillis();
      final MockConnector connector = new MockConnector( 0, 0, lastPingTime );
      final PeriodicPingPolicy policy = new PeriodicPingPolicy( 100, connector );
      assertEquals( "nextPingCheck",
                    lastPingTime + 100,
                    policy.nextPingCheck() );
      assertEquals( "checkPingConnection",
                    true,
                    policy.checkPingConnection() );
   }
}
