package org.realityforge.connection.policys;

import junit.framework.TestCase;

public class AlwaysReconnectPolicyTestCase
   extends TestCase
{
   public void testLimitingReconnectPolicy()
      throws Exception
   {
      final LimitingReconnectPolicy policy = new LimitingReconnectPolicy( 3, 3000 );
      final long now = System.currentTimeMillis();
      assertEquals( "attemptConnection",
                    true,
                    policy.attemptConnection( now, 0 ) );
      assertEquals( "disconnectOnError",
                    true,
                    policy.disconnectOnError( new Throwable() ) );
      assertEquals( "reconnectOnDisconnect",
                    true,
                    policy.reconnectOnDisconnect() );
   }

   public void testUnderThreshold()
      throws Exception
   {
      final LimitingReconnectPolicy policy = new LimitingReconnectPolicy( 3, 3000 );
      final long now = System.currentTimeMillis();
      assertEquals( "attemptConnection",
                    true,
                    policy.attemptConnection( now, 2 ) );
   }

   public void testOnThresholdNoDelay()
      throws Exception
   {
      final LimitingReconnectPolicy policy = new LimitingReconnectPolicy( 3, 3000 );
      final long now = System.currentTimeMillis();
      assertEquals( "attemptConnection",
                    false,
                    policy.attemptConnection( now, 3 ) );
   }

   public void testOnThresholdWithDelay()
      throws Exception
   {
      final LimitingReconnectPolicy policy = new LimitingReconnectPolicy( 3, 3000 );
      final long now = System.currentTimeMillis();
      assertEquals( "attemptConnection",
                    true,
                    policy.attemptConnection( now - 5000, 3 ) );
   }

   public void testOverThresholdNoDelay()
      throws Exception
   {
      final LimitingReconnectPolicy policy = new LimitingReconnectPolicy( 3, 3000 );
      final long now = System.currentTimeMillis();
      assertEquals( "attemptConnection",
                    false,
                    policy.attemptConnection( now, 3 ) );
   }

   public void testOverThresholdWithDelay()
      throws Exception
   {
      final LimitingReconnectPolicy policy = new LimitingReconnectPolicy( 3, 3000 );
      final long now = System.currentTimeMillis();
      assertEquals( "attemptConnection",
                    true,
                    policy.attemptConnection( now - 5000, 3 ) );
   }
}
