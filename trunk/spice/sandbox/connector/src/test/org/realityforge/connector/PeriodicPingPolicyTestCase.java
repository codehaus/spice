package org.realityforge.connector;

import junit.framework.TestCase;

public class PeriodicPingPolicyTestCase
   extends TestCase
{
   public void testNeverPingPolicy()
      throws Exception
   {
      final PeriodicPingPolicy policy = new PeriodicPingPolicy( 100 );
      final long lastPingTime = System.currentTimeMillis();
      assertEquals( "nextPingCheck",
                    lastPingTime + 100,
                    policy.nextPingCheck( lastPingTime ) );
      assertEquals( "checkPingConnection",
                    true,
                    policy.checkPingConnection( null ) );
   }
}
