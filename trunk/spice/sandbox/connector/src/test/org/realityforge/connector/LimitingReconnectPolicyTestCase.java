package org.realityforge.connector;

import junit.framework.TestCase;

public class LimitingReconnectPolicyTestCase
   extends TestCase
{
   public void testAlwaysReconnectPolicy()
      throws Exception
   {
      final AlwaysReconnectPolicy policy = AlwaysReconnectPolicy.POLICY;
      assertEquals( "attemptConnection",
                    true,
                    policy.attemptConnection( 0, 0 ) );
      assertEquals( "disconnectOnError",
                    true,
                    policy.disconnectOnError( new Throwable() ) );
      assertEquals( "reconnectOnDisconnect",
                    true,
                    policy.reconnectOnDisconnect() );
   }
}
