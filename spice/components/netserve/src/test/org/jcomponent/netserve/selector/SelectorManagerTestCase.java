package org.jcomponent.netserve.selector;

import junit.framework.TestCase;

public class SelectorManagerTestCase
   extends TestCase
{
   public void testSetMonitorUsingNonNull()
      throws Exception
   {
      final SelectorManager manager = new SelectorManager();
      final NullSelectorMonitor monitor = new NullSelectorMonitor();
      manager.setMonitor( monitor );
      assertEquals( "monitor", monitor, manager.getMonitor() );
   }

   public void testSetMonitorUsingNull()
      throws Exception
   {
      final SelectorManager manager = new SelectorManager();
      try
      {
         manager.setMonitor( null );
      }
      catch ( final NullPointerException npe )
      {
         assertEquals( "npe.getMessage()", "monitor", npe.getMessage() );
         return;
      }
      fail( "expected to fail with npe" );
   }

   public void testSetHandlerUsingNonNull()
      throws Exception
   {
      final SelectorManager manager = new SelectorManager();
      final NullSelectorEventHandler handler = new NullSelectorEventHandler();
      manager.setHandler( handler );
      assertEquals( "handler", handler, manager.getHandler() );
   }

   public void testSetHandlerUsingNull()
      throws Exception
   {
      final SelectorManager manager = new SelectorManager();
      try
      {
         manager.setHandler( null );
      }
      catch ( final NullPointerException npe )
      {
         assertEquals( "npe.getMessage()", "handler", npe.getMessage() );
         return;
      }
      fail( "expected to fail with npe" );
   }

   public void testStartupAndShutdownSelectorManager()
      throws Exception
   {
      final SelectorManager manager = new SelectorManager();
      manager.setTimeout( 400 );
      assertEquals( "isRunning pre start", false, manager.isRunning() );
      assertNullSelector( manager );
      manager.startup();
      assertEquals( "isRunning post start", true, manager.isRunning() );
      assertNotNull( "getSelector post start", manager.getSelector() );
      Thread.sleep( 150 );
      manager.shutdown();
      assertNullSelector( manager );
      assertEquals( "isRunning post shutdown", false, manager.isRunning() );
   }

   public void testShutdownSelectorManagerWithoutStatrup()
      throws Exception
   {
      final SelectorManager manager = new SelectorManager();
      manager.setTimeout( 400 );
      assertEquals( "isRunning pre shutdown", false, manager.isRunning() );
      assertNullSelector( manager );
      manager.shutdown();
      assertNullSelector( manager );
      assertEquals( "isRunning post shutdown", false, manager.isRunning() );
   }

   private void assertNullSelector( final SelectorManager manager )
   {
      try
      {
         manager.getSelector();
         fail( "Expected NPE in getSelector" );
      }
      catch ( final NullPointerException npe )
      {
         assertEquals( "getMessage()", "selector", npe.getMessage() );
      }
   }
}
