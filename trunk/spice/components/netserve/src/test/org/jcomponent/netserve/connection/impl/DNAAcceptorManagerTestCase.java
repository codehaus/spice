package org.jcomponent.netserve.connection.impl;

import junit.framework.TestCase;
import org.jcontainer.dna.impl.DefaultConfiguration;

public class DNAAcceptorManagerTestCase
   extends TestCase
{
   public void testDNAAcceptorManager()
      throws Exception
   {
      final DNAAcceptorManager manager = new DNAAcceptorManager();
      final DefaultConfiguration root = new DefaultConfiguration("root","","");
      final DefaultConfiguration configuration = new DefaultConfiguration("shutdownTimeout","","");
      configuration.setValue( "22" );
      root.addChild( configuration );
      manager.configure( root );
      assertEquals( "getShutdownTimeout()", 22, manager.getShutdownTimeout() );
      manager.initialize();
      manager.dispose();
   }
}
