package org.realityforge.connection;

import junit.framework.TestCase;

public class ConnectorTestCase
   extends TestCase
{
   public void testSetNullPolicy()
      throws Exception
   {
      final Connector connector = new Connector();
      try
      {
         connector.setPolicy( null );
      }
      catch ( final NullPointerException npe )
      {
         assertEquals( "npe.message", "policy", npe.getMessage() );
         return;
      }
      fail( "Expected to fail due to NPE" );
   }

   public void testSetNullMonitor()
      throws Exception
   {
      final Connector connector = new Connector();
      try
      {
         connector.setMonitor( null );
      }
      catch ( final NullPointerException npe )
      {
         assertEquals( "npe.message", "monitor", npe.getMessage() );
         return;
      }
      fail( "Expected to fail due to NPE" );
   }

   public void testSetNullConnection()
      throws Exception
   {
      final Connector connector = new Connector();
      try
      {
         connector.setConnection( null );
      }
      catch ( final NullPointerException npe )
      {
         assertEquals( "npe.message", "connection", npe.getMessage() );
         return;
      }
      fail( "Expected to fail due to NPE" );
   }
}
