package org.realityforge.connector;

import com.mockobjects.dynamic.C;
import com.mockobjects.dynamic.Mock;
import junit.framework.TestCase;

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

   public void testGetConnectionPreSet()
      throws Exception
   {
      final Connector connector = new Connector();
      try
      {
         connector.getConnection();
      }
      catch ( final NullPointerException npe )
      {
         assertEquals( "npe.message", "connection", npe.getMessage() );
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

   public void testTransmissionOccured()
      throws Exception
   {
      final Connector connector = new Connector();
      final Object message = new Object();
      assertEquals( "time", 0, connector.getLastTxTime() );
      assertEquals( "message", null, connector.getLastTxMessage() );
      connector.transmissionOccured( message );
      final long now = System.currentTimeMillis();
      assertEquals( "time", now, connector.getLastTxTime(), 500.0 );
      assertEquals( "message", message, connector.getLastTxMessage() );
   }

   public void testReceiveOccured()
      throws Exception
   {
      final Connector connector = new Connector();
      final Object message = new Object();
      assertEquals( "time", 0, connector.getLastRxTime() );
      assertEquals( "message", null, connector.getLastRxMessage() );
      connector.receiveOccured( message );
      final long now = System.currentTimeMillis();
      assertEquals( "time", now, connector.getLastRxTime(), 500.0 );
      assertEquals( "message", message, connector.getLastRxMessage() );
   }

   public void testCommOccured()
      throws Exception
   {
      final Connector connector = new Connector();
      final Object message = new Object();
      assertEquals( "rx time", 0, connector.getLastRxTime() );
      assertEquals( "rx message", null, connector.getLastRxMessage() );
      assertEquals( "tx time", 0, connector.getLastTxTime() );
      assertEquals( "tx message", null, connector.getLastTxMessage() );
      connector.commOccured( message );
      final long now = System.currentTimeMillis();
      assertEquals( "time", now, connector.getLastRxTime(), 500.0 );
      assertEquals( "tx vs rx time", connector.getLastTxTime(), connector.getLastRxTime() );
      assertEquals( "rx message", message, connector.getLastRxMessage() );
      assertEquals( "tx message", message, connector.getLastTxMessage() );
   }

   public void testSuccessfulConnectOnDisconnected()
      throws Exception
   {
      final Mock connectorMock = new Mock( ConnectorConnection.class );
      connectorMock.expect( "doConnect", C.NO_ARGS );

      final ConnectorConnection connection = (ConnectorConnection) connectorMock.proxy();

      final Connector connector = new Connector();
      connector.setConnection( connection );
      connector.setActive( true );
      connector.setMonitor( DebugMonitor.MONITOR );

      assertEquals( "isConnected pre connect", false, connector.isConnected() );
      connector.connect();
      final long now = System.currentTimeMillis();
      assertEquals( "getLastConnectionTime", now, connector.getLastConnectionTime(), 500.0 );
      assertEquals( "isConnected post connect", true, connector.isConnected() );
      assertEquals( "getConnectionAttempts", 0, connector.getConnectionAttempts() );
      assertEquals( "getConnectionError", null, connector.getConnectionError() );

      connectorMock.verify();
   }

   public void testSuccessfulConnectOnConnected()
      throws Exception
   {
      final Mock connectorMock = new Mock( ConnectorConnection.class );
      connectorMock.expect( "doDisconnect", C.NO_ARGS );
      connectorMock.expect( "doConnect", C.NO_ARGS );

      final ConnectorConnection connection = (ConnectorConnection) connectorMock.proxy();

      final Connector connector = new Connector();
      connector.setConnection( connection );
      connector.setActive( true );
      connector.setMonitor( DebugMonitor.MONITOR );

      connector.setConnected( true );
      connector.connect();
      final long now = System.currentTimeMillis();

      assertEquals( "getLastConnectionTime", now, connector.getLastConnectionTime(), 500.0 );
      assertEquals( "isConnected post connect", true, connector.isConnected() );
      assertEquals( "getConnectionAttempts", 0, connector.getConnectionAttempts() );
      assertEquals( "getConnectionError", null, connector.getConnectionError() );

      connectorMock.verify();
   }

   public void testSuccessfulDisconnectOnConnected()
      throws Exception
   {
      final Mock connectorMock = new Mock( ConnectorConnection.class );
      connectorMock.expect( "doDisconnect", C.NO_ARGS );

      final ConnectorConnection connection = (ConnectorConnection) connectorMock.proxy();

      final Connector connector = new Connector();
      connector.setConnection( connection );
      connector.setActive( true );

      connector.setConnected( true );
      connector.disconnect();
      assertEquals( "isConnected", false, connector.isConnected() );

      connectorMock.verify();
   }

   public void testSuccessfulDisconnectOnDisconnected()
      throws Exception
   {
      final Mock connectorMock = new Mock( ConnectorConnection.class );

      final ConnectorConnection connection = (ConnectorConnection) connectorMock.proxy();

      final Connector connector = new Connector();
      connector.setConnection( connection );

      connector.setConnected( false );
      connector.disconnect();
      assertEquals( "isConnected", false, connector.isConnected() );

      connectorMock.verify();
   }

   public void testFailedDisconnect()
      throws Exception
   {
      final Mock connectorMock = new Mock( ConnectorConnection.class );
      final Exception exception = new Exception();
      connectorMock.expectAndThrow( "doDisconnect", C.NO_ARGS, exception );

      final Mock monitorMock = new Mock( ConnectorMonitor.class );
      monitorMock.expect( "attemptingDisconnection", C.NO_ARGS );
      monitorMock.expect( "errorDisconnecting", C.args( C.eq( exception ) ) );
      final ConnectorMonitor monitor = (ConnectorMonitor) monitorMock.proxy();

      final ConnectorConnection connection = (ConnectorConnection) connectorMock.proxy();

      final Connector connector = new Connector();
      connector.setConnection( connection );
      connector.setMonitor( monitor );

      connector.setConnected( true );
      connector.disconnect();
      assertEquals( "isConnected", false, connector.isConnected() );

      connectorMock.verify();
      monitorMock.verify();
   }

   public void testConnectOnInActive()
      throws Exception
   {
      final Mock connectorMock = new Mock( ConnectorConnection.class );

      final ConnectorConnection connection = (ConnectorConnection) connectorMock.proxy();

      final Connector connector = new Connector();
      connector.setConnection( connection );
      connector.setActive( false );
      connector.connect();
      assertEquals( "isConnected", false, connector.isConnected() );

      connectorMock.verify();
   }

   public void testFailedConnect()
      throws Exception
   {
      final Mock connectorMock = new Mock( ConnectorConnection.class );
      final Exception exception = new Exception();
      connectorMock.expectAndThrow( "doConnect", C.NO_ARGS, exception );

      final Mock monitorMock = new Mock( ConnectorMonitor.class );
      monitorMock.expect( "attemptingConnection", C.NO_ARGS );
      monitorMock.expect( "errorConnecting", C.args( C.eq( exception ) ) );
      monitorMock.expect( "skippingConnectionAttempt", C.NO_ARGS );
      final ConnectorMonitor monitor = (ConnectorMonitor) monitorMock.proxy();

      final Mock policyMock = new Mock( ReconnectionPolicy.class );
      policyMock.expectAndReturn( "attemptConnection", C.args( C.eq( 0L ), C.eq( 0 ) ), true );
      policyMock.expectAndReturn( "attemptConnection", C.args( C.IS_ANYTHING, C.eq( 1 ) ), false );
      final ReconnectionPolicy policy = (ReconnectionPolicy) policyMock.proxy();

      final ConnectorConnection connection = (ConnectorConnection) connectorMock.proxy();

      final Connector connector = new Connector();
      connector.setConnection( connection );
      connector.setMonitor( monitor );
      connector.setPolicy( policy );

      connector.setActive( true );
      connector.connect();
      assertEquals( "isConnected", false, connector.isConnected() );

      connectorMock.verify();
      monitorMock.verify();
      policyMock.verify();
   }

   public void testVerifyConnectedOnConnected()
      throws Exception
   {
      final Mock connectorMock = new Mock( ConnectorConnection.class );

      final ConnectorConnection connection = (ConnectorConnection) connectorMock.proxy();

      final Connector connector = new Connector();
      connector.setConnection( connection );

      connector.setActive( true );
      connector.setConnected( true );
      connector.verifyConnected();
      assertEquals( "isConnected", true, connector.isConnected() );

      connectorMock.verify();
   }

   public void testVerifyConnectedOnDisconnected()
      throws Exception
   {
      final Mock connectorMock = new Mock( ConnectorConnection.class );
      connectorMock.expect( "doConnect", C.NO_ARGS );

      final ConnectorConnection connection = (ConnectorConnection) connectorMock.proxy();

      final Connector connector = new Connector();
      connector.setConnection( connection );

      connector.setActive( true );
      connector.verifyConnected();
      assertEquals( "isConnected", true, connector.isConnected() );

      connectorMock.verify();
   }

   public void testDoValidateConnection()
      throws Exception
   {
      final Mock connectorMock = new Mock( ConnectorConnection.class );
      final Exception exception = new Exception();
      connectorMock.expectAndThrow( "doValidateConnection", C.NO_ARGS, exception );
      connectorMock.expect( "doConnect", C.NO_ARGS );
      final ConnectorConnection connection = (ConnectorConnection) connectorMock.proxy();

      final Mock monitorMock = new Mock( ConnectorMonitor.class );
      monitorMock.expect( "errorValidatingConnection", C.args( C.eq( exception ) ) );
      monitorMock.expect( "attemptingConnection" );
      monitorMock.expect( "connectionEstablished" );
      final ConnectorMonitor monitor = (ConnectorMonitor) monitorMock.proxy();

      final Mock policyMock = new Mock( ReconnectionPolicy.class );
      policyMock.expectAndReturn( "reconnectOnDisconnect", C.NO_ARGS, true );
      policyMock.expectAndReturn( "attemptConnection", C.anyArgs( 2 ), true );

      final ReconnectionPolicy policy = (ReconnectionPolicy) policyMock.proxy();

      final Connector connector = new Connector();
      connector.setConnection( connection );
      connector.setMonitor( monitor );
      connector.setPolicy( policy );

      connector.setActive( true );
      connector.doValidateConnection();

      assertEquals( "isConnected", true, connector.isConnected() );
      assertEquals( "getConnectionError",
                    null,
                    connector.getConnectionError() );

      connectorMock.verify();
      policyMock.verify();
      monitorMock.verify();
   }

   public void testDoValidateConnectionWithNoReconnect()
      throws Exception
   {
      final Mock connectorMock = new Mock( ConnectorConnection.class );
      final Exception exception = new Exception();
      connectorMock.expectAndThrow( "doValidateConnection", C.NO_ARGS, exception );
      final ConnectorConnection connection = (ConnectorConnection) connectorMock.proxy();

      final Mock monitorMock = new Mock( ConnectorMonitor.class );
      monitorMock.expect( "errorValidatingConnection", C.args( C.eq( exception ) ) );
      final ConnectorMonitor monitor = (ConnectorMonitor) monitorMock.proxy();

      final Mock policyMock = new Mock( ReconnectionPolicy.class );
      policyMock.expectAndReturn( "reconnectOnDisconnect", C.NO_ARGS, false );

      final ReconnectionPolicy policy = (ReconnectionPolicy) policyMock.proxy();

      final Connector connector = new Connector();
      connector.setConnection( connection );
      connector.setMonitor( monitor );
      connector.setPolicy( policy );

      connector.setActive( true );
      connector.doValidateConnection();

      assertEquals( "isConnected", false, connector.isConnected() );
      assertEquals( "getConnectionError",
                    exception.toString(),
                    connector.getConnectionError() );

      connectorMock.verify();
      policyMock.verify();
      monitorMock.verify();
   }

   public void testValidateConnectionOnConnected()
      throws Exception
   {
      final Mock connectorMock = new Mock( ConnectorConnection.class );
      connectorMock.expect( "doValidateConnection", C.NO_ARGS );
      final ConnectorConnection connection = (ConnectorConnection) connectorMock.proxy();

      final Mock monitorMock = new Mock( ConnectorMonitor.class );
      monitorMock.expect( "attemptingValidation", C.NO_ARGS );
      final ConnectorMonitor monitor = (ConnectorMonitor) monitorMock.proxy();

      final Connector connector = new Connector();
      connector.setConnection( connection );
      connector.setMonitor( monitor );

      connector.setActive( true );
      connector.setConnected( true );
      connector.validateConnection();

      assertEquals( "isConnected", true, connector.isConnected() );

      connectorMock.verify();
      monitorMock.verify();
   }


   public void testValidateConnectionOnNonVerified()
      throws Exception
   {
      final Mock connectorMock = new Mock( ConnectorConnection.class );
      final ConnectorConnection connection = (ConnectorConnection) connectorMock.proxy();

      final Mock monitorMock = new Mock( ConnectorMonitor.class );
      monitorMock.expect( "attemptingValidation", C.NO_ARGS );
      final ConnectorMonitor monitor = (ConnectorMonitor) monitorMock.proxy();

      final Mock policyMock = new Mock( ReconnectionPolicy.class );
      final ReconnectionPolicy policy = (ReconnectionPolicy) policyMock.proxy();

      final Connector connector = new Connector();
      connector.setConnection( connection );
      connector.setMonitor( monitor );
      connector.setPolicy( policy );

      connector.setActive( false );
      final boolean result = connector.validateConnection();

      assertEquals( "result", false, result );
      assertEquals( "isConnected", false, connector.isConnected() );

      connectorMock.verify();
      monitorMock.verify();
      policyMock.verify();
   }
}
