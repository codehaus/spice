package org.codehaus.spice.netserve.connection.impl;

import junit.framework.TestCase;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.apache.avalon.framework.logger.ConsoleLogger;

public class AvalonAcceptorManagerTestCase
   extends TestCase
{
   public void testAvalonAcceptorManager()
      throws Exception
   {
      final AvalonAcceptorManager manager = new AvalonAcceptorManager();
      final DefaultConfiguration root = new DefaultConfiguration( "root", "" );
      final DefaultConfiguration configuration = new DefaultConfiguration( "shutdownTimeout", "" );
      configuration.setValue( "22" );
      root.addChild( configuration );

      final ConsoleLogger logger = new ConsoleLogger( ConsoleLogger.LEVEL_DISABLED );
      manager.enableLogging( logger );
      final AcceptorMonitor monitor = manager.getMonitor();
      assertTrue( "getMonitor() instanceof AvalonAcceptorMonitor", monitor instanceof AvalonAcceptorMonitor );
      final AvalonAcceptorMonitor dnaMonitor = (AvalonAcceptorMonitor) monitor;
      assertEquals( "logger", logger, dnaMonitor.getLogger() );
      manager.configure( root );
      assertEquals( "getShutdownTimeout()", 22, manager.getShutdownTimeout() );
      manager.initialize();
      manager.dispose();
   }
}
