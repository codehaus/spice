package org.realityforge.connector;

import junit.framework.TestCase;

public class NeverPingPolicyTestCase
   extends TestCase
{
   public void testNeverPingPolicy()
      throws Exception
   {
      final NeverPingPolicy policy = NeverPingPolicy.POLICY;
      final long lastPingTime = System.currentTimeMillis();
      assertEquals( "nextPingCheck",
                    Long.MAX_VALUE,
                    policy.nextPingCheck( lastPingTime ) );
      assertEquals( "checkPingConnection",
                    false,
                    policy.checkPingConnection( null ) );
   }
}
