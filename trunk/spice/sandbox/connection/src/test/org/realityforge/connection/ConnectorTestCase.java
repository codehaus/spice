package org.realityforge.connection;

import com.mockobjects.dynamic.Mock;
import junit.framework.TestCase;
import org.realityforge.connection.policys.LimitingReconnectPolicy;

public class ConnectorTestCase
   extends TestCase
{
   public void testSetPolicy()
      throws Exception
   {
      final Connector connector = new Connector();
      final LimitingReconnectPolicy policy = new LimitingReconnectPolicy( 1, 1 );
      connector.setPolicy( policy );
      assertEquals( "policy", policy, connector.getPolicy() );
   }

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

   public void testSetMonitor()
      throws Exception
   {
      final Connector connector = new Connector();
      final NullMonitor monitor = new NullMonitor();
      connector.setMonitor( monitor );
      assertEquals( "monitor", monitor, connector.getMonitor() );
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

   public void testSetConnection()
      throws Exception
   {
      final Mock mock = new Mock( ConnectorConnection.class );
      final ConnectorConnection connection = (ConnectorConnection) mock.proxy();

      final Connector connector = new Connector();
      connector.setConnection( connection );
      assertEquals( "connection", connection, connector.getConnection() );
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
