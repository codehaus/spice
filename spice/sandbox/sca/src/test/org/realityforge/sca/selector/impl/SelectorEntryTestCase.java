package org.realityforge.sca.selector.impl;

import junit.framework.TestCase;
import org.realityforge.sca.selector.impl.SelectorEntry;
import org.realityforge.sca.selector.impl.HelloSelectorEventHandler;

public class SelectorEntryTestCase
   extends TestCase
{
   public void testSelectorEntry()
      throws Exception
   {
      final HelloSelectorEventHandler handler = new HelloSelectorEventHandler();
      final Object userData = new Object();
      final SelectorEntry entry =
         new SelectorEntry( handler, userData );
      assertEquals( "handler", handler, entry.getHandler() );
      assertEquals( "userData", userData, entry.getUserData() );
   }

   public void testNullHandlerInSelectorEntryCtor()
      throws Exception
   {
      try
      {
         new SelectorEntry( null, null );
      }
      catch ( final NullPointerException npe )
      {
         assertEquals( "npe.getMessage()", "handler", npe.getMessage() );
         return;
      }
      fail( "Expected to fail due to passing null handler into ctor" );
   }
}
