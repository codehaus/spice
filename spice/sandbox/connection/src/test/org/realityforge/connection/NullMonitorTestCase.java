package org.realityforge.connection;

import junit.framework.TestCase;

public class NullMonitorTestCase
   extends TestCase
{
   public void testNullMonitor()
      throws Exception
   {
      final NullMonitor monitor = new NullMonitor();
      monitor.attemptingConnection();
      monitor.attemptingValidation();
      monitor.connectionEstablished();
      monitor.errorConnecting( null );
      monitor.errorDisconnecting( null );
      monitor.errorValidatingConnection( null );
      monitor.skippingConnectionAttempt();
   }
}
