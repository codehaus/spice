package org.realityforge.connector;

import junit.framework.TestCase;

public class InactivityPingPolicyTestCase
   extends TestCase
{
   public void testInactivityPingPolicyWithNeitherTxOrRxSet()
      throws Exception
   {
      final MockConnector connector = new MockConnector( 0, 0, 0 );
      final InactivityPingPolicy policy = new InactivityPingPolicy( -1, -1, connector );
      assertEquals( "nextPingCheck",
                    Long.MAX_VALUE,
                    policy.nextPingCheck() );
      assertEquals( "checkPingConnection",
                    false,
                    policy.checkPingConnection() );
   }

   public void testInactivityPingPolicyWithNeitherTxSetNeedingPing()
      throws Exception
   {
      final long now = System.currentTimeMillis();
      final long lastTx = now - 100;
      final long tx = 5;
      final MockConnector connector = new MockConnector( lastTx, 0, 0 );
      final InactivityPingPolicy policy = new InactivityPingPolicy( tx, -1, connector );
      assertEquals( "nextPingCheck",
                    lastTx + tx,
                    policy.nextPingCheck() );
      assertEquals( "checkPingConnection",
                    true,
                    policy.checkPingConnection() );
   }

   public void testInactivityPingPolicyWithNeitherTxSetNotNeedingPing()
      throws Exception
   {
      final long now = System.currentTimeMillis();
      final long lastTx = now - 100;
      final long tx = 500;
      final MockConnector connector = new MockConnector( lastTx, 0, 0 );
      final InactivityPingPolicy policy = new InactivityPingPolicy( tx, -1, connector );
      assertEquals( "nextPingCheck",
                    lastTx + tx,
                    policy.nextPingCheck() );
      assertEquals( "checkPingConnection",
                    false,
                    policy.checkPingConnection() );
   }

   public void testInactivityPingPolicyWithNeitherRxSetNeedingPing()
      throws Exception
   {
      final long now = System.currentTimeMillis();
      final long lastRx = now - 100;
      final long rx = 5;
      final MockConnector connector = new MockConnector( 0, lastRx, 0 );
      final InactivityPingPolicy policy = new InactivityPingPolicy( -1, rx, connector );
      assertEquals( "nextPingCheck",
                    lastRx + rx,
                    policy.nextPingCheck() );
      assertEquals( "checkPingConnection",
                    true,
                    policy.checkPingConnection() );
   }

   public void testInactivityPingPolicyWithNeitherRxSetNotNeedingPing()
      throws Exception
   {
      final long now = System.currentTimeMillis();
      final long lastRx = now - 100;
      final long rx = 500;
      final MockConnector connector = new MockConnector( 0, lastRx, 0 );
      final InactivityPingPolicy policy = new InactivityPingPolicy( -1, rx, connector );
      assertEquals( "nextPingCheck",
                    lastRx + rx,
                    policy.nextPingCheck() );
      assertEquals( "checkPingConnection",
                    false,
                    policy.checkPingConnection() );
   }
}
