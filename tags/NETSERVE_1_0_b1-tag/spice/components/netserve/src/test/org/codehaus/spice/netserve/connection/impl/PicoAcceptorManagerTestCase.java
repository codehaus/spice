package org.codehaus.spice.netserve.connection.impl;

import junit.framework.TestCase;

public class PicoAcceptorManagerTestCase
   extends TestCase
{
   public void testPicoAcceptorManagerWithMonitorAndShutdownSet()
      throws Exception
   {
      final NullAcceptorMonitor monitor = new NullAcceptorMonitor();
      final PicoAcceptorManager manager = new PicoAcceptorManager( 22, monitor );
      assertEquals( "shutdownTimeout", 22, manager.getShutdownTimeout() );
      assertEquals( "monitor", monitor, manager.getMonitor() );
   }

   public void testPicoAcceptorManagerWithShutdownSet()
      throws Exception
   {
      final PicoAcceptorManager manager = new PicoAcceptorManager( 22 );
      assertEquals( "shutdownTimeout", 22, manager.getShutdownTimeout() );
      assertEquals( "monitor", NullAcceptorMonitor.MONITOR, manager.getMonitor() );
   }

   public void testPicoAcceptorManagerWithMonitorSet()
      throws Exception
   {
      final NullAcceptorMonitor monitor = new NullAcceptorMonitor();
      final PicoAcceptorManager manager = new PicoAcceptorManager( monitor );
      assertEquals( "shutdownTimeout", 0, manager.getShutdownTimeout() );
      assertEquals( "monitor", monitor, manager.getMonitor() );
   }

   public void testPicoAcceptorManagerWithDefaultCtor()
      throws Exception
   {
      final PicoAcceptorManager manager = new PicoAcceptorManager();
      assertEquals( "shutdownTimeout", 0, manager.getShutdownTimeout() );
      assertEquals( "monitor", NullAcceptorMonitor.MONITOR, manager.getMonitor() );
   }
}
