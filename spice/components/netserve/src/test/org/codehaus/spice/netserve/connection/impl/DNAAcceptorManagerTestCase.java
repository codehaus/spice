package org.codehaus.spice.netserve.connection.impl;

import junit.framework.TestCase;
import org.jcontainer.dna.impl.DefaultConfiguration;
import org.jcontainer.dna.impl.ConsoleLogger;

public class DNAAcceptorManagerTestCase
   extends TestCase
{
   public void testDNAAcceptorManager()
      throws Exception
   {
      final DNAAcceptorManager manager = new DNAAcceptorManager();
      final DefaultConfiguration root = new DefaultConfiguration( "root", "", "" );
      final DefaultConfiguration configuration = new DefaultConfiguration( "shutdownTimeout", "", "" );
      configuration.setValue( "22" );
      root.addChild( configuration );

      final ConsoleLogger logger = new ConsoleLogger( ConsoleLogger.LEVEL_NONE );
      manager.enableLogging( logger );
      final AcceptorMonitor monitor = manager.getMonitor();
      assertTrue( "getMonitor() instanceof DNAAcceptorMonitor", monitor instanceof DNAAcceptorMonitor );
      final DNAAcceptorMonitor dnaMonitor = (DNAAcceptorMonitor) monitor;
      assertEquals( "logger", logger, dnaMonitor.getLogger() );
      manager.configure( root );
      assertEquals( "getShutdownTimeout()", 22, manager.getShutdownTimeout() );
      manager.initialize();
      manager.dispose();
   }
}
