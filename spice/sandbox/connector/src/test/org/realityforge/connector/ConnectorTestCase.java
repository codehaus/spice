package org.realityforge.connector;

import com.mockobjects.dynamic.C;
import com.mockobjects.dynamic.Mock;
import junit.framework.TestCase;

public class ConnectorTestCase
   extends TestCase
{
   public void testSetPingPolicy()
      throws Exception
   {
      final Connector connector = new Connector();
      final NeverPingPolicy policy = new NeverPingPolicy();
      connector.setPingPolicy( policy );
      assertEquals( "PingPolicy", policy, connector.getPingPolicy() );
   }

   public void testSetNullPingPolicy()
      throws Exception
   {
      final Connector connector = new Connector();
      try
      {
         connector.setPingPolicy( null );
      }
      catch ( final NullPointerException npe )
      {
         assertEquals( "npe.message", "pingPolicy", npe.getMessage() );
         return;
      }
      fail( "Expected to fail due to NPE" );
   }

   public void testSetPolicy()
      throws Exception
   {
      final Connector connector = new Connector();
      final LimitingReconnectPolicy policy = new LimitingReconnectPolicy( 1, 1 );
      connector.setReconnectPolicy( policy );
      assertEquals( "policy", policy, connector.getReconnectPolicy() );
   }

   public void testSetNullPolicy()
      throws Exception
   {
      final Connector connector = new Connector();
      try
      {
         connector.setReconnectPolicy( null );
      }
      catch ( final NullPointerException npe )
      {
         assertEquals( "npe.message", "reconnectPolicy", npe.getMessage() );
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
      assertEqualTime( "time", now, connector.getLastTxTime() );
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
      assertEqualTime( "time", now, connector.getLastRxTime() );
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
      assertEqualTime( "time", now, connector.getLastRxTime() );
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

      assertEqualTime( "getPingTime", now, connector.getLastPingTime() );

      assertEquals( "getLastRxMessage", null, connector.getLastRxMessage() );
      assertEquals( "getLastTxMessage", null, connector.getLastTxMessage() );
      assertEqualTime( "getLastRxTime", now, connector.getLastRxTime() );
      assertEqualTime( "getLastTxTime", now, connector.getLastTxTime() );
      assertEqualTime( "getLastConnectionTime", now, connector.getLastConnectionTime() );
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

      assertEqualTime( "getPingTime", now, connector.getLastPingTime() );
      assertEqualTime( "getLastConnectionTime", now, connector.getLastConnectionTime() );
      assertEquals( "getLastRxMessage", null, connector.getLastRxMessage() );
      assertEquals( "getLastTxMessage", null, connector.getLastTxMessage() );
      assertEqualTime( "getLastRxTime", now, connector.getLastRxTime() );
      assertEqualTime( "getLastTxTime", now, connector.getLastTxTime() );
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
      connector.setReconnectPolicy( policy );

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
      connector.setReconnectPolicy( policy );

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
      connector.setReconnectPolicy( policy );

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
      connector.setReconnectPolicy( policy );

      connector.setActive( false );
      final boolean result = connector.validateConnection();

      assertEquals( "result", false, result );
      assertEquals( "isConnected", false, connector.isConnected() );

      connectorMock.verify();
      monitorMock.verify();
      policyMock.verify();
   }

   public void testPing()
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
      connector.ping();

      final long now = System.currentTimeMillis();
      assertEqualTime( "getPingTime", now, connector.getLastPingTime() );
      assertEquals( "isConnected", true, connector.isConnected() );

      connectorMock.verify();
      monitorMock.verify();
   }

   public void testCommErrorOccuredAndDisconnectAndReconnect()
      throws Exception
   {
      final Exception exception = new Exception();

      final Mock connectorMock = new Mock( ConnectorConnection.class );
      connectorMock.expect( "doDisconnect", C.NO_ARGS );
      connectorMock.expect( "doConnect", C.NO_ARGS );
      final ConnectorConnection connection = (ConnectorConnection) connectorMock.proxy();

      final Mock policyMock = new Mock( ReconnectionPolicy.class );
      policyMock.expectAndReturn( "disconnectOnError", C.args( C.eq( exception ) ), true );
      policyMock.expectAndReturn( "reconnectOnDisconnect", C.NO_ARGS, true );
      policyMock.expectAndReturn( "attemptConnection", C.anyArgs( 2 ), true );
      final ReconnectionPolicy policy = (ReconnectionPolicy) policyMock.proxy();

      final Connector connector = new Connector();
      connector.setConnection( connection );
      connector.setReconnectPolicy( policy );

      connector.setActive( true );
      connector.setConnected( true );

      connector.commErrorOccured( exception );

      assertEquals( "isConnected", true, connector.isConnected() );

      connectorMock.verify();
      policyMock.verify();
   }

   public void testCommErrorOccuredAndDisconnect()
      throws Exception
   {
      final Exception exception = new Exception();

      final Mock connectorMock = new Mock( ConnectorConnection.class );
      connectorMock.expect( "doDisconnect", C.NO_ARGS );
      final ConnectorConnection connection = (ConnectorConnection) connectorMock.proxy();

      final Mock policyMock = new Mock( ReconnectionPolicy.class );
      policyMock.expectAndReturn( "disconnectOnError", C.args( C.eq( exception ) ), true );
      policyMock.expectAndReturn( "reconnectOnDisconnect", C.NO_ARGS, false );
      final ReconnectionPolicy policy = (ReconnectionPolicy) policyMock.proxy();

      final Connector connector = new Connector();
      connector.setConnection( connection );
      connector.setReconnectPolicy( policy );

      connector.setActive( true );
      connector.setConnected( true );

      connector.commErrorOccured( exception );

      assertEquals( "isConnected", false, connector.isConnected() );

      connectorMock.verify();
      policyMock.verify();
   }

   public void testCommErrorOccuredAndNoDisconnect()
      throws Exception
   {
      final Exception exception = new Exception();

      final Mock connectorMock = new Mock( ConnectorConnection.class );
      final ConnectorConnection connection = (ConnectorConnection) connectorMock.proxy();

      final Mock policyMock = new Mock( ReconnectionPolicy.class );
      policyMock.expectAndReturn( "disconnectOnError", C.args( C.eq( exception ) ), false );
      final ReconnectionPolicy policy = (ReconnectionPolicy) policyMock.proxy();

      final Connector connector = new Connector();
      connector.setConnection( connection );
      connector.setReconnectPolicy( policy );

      connector.setActive( true );
      connector.setConnected( true );

      connector.commErrorOccured( exception );

      assertEquals( "isConnected", true, connector.isConnected() );

      policyMock.verify();
      connectorMock.verify();
   }

   public void testCheckPingThatPings()
      throws Exception
   {
      final Connector connector = new Connector();

      final Mock monitorMock = new Mock( ConnectorMonitor.class );
      monitorMock.expect( "attemptingValidation", C.NO_ARGS );
      final ConnectorMonitor monitor = (ConnectorMonitor) monitorMock.proxy();

      final Mock policyMock = new Mock( PingPolicy.class );
      policyMock.expectAndReturn( "checkPingConnection", C.args( C.eq( connector ) ), true );
      policyMock.expectAndReturn( "nextPingCheck", C.args( C.eq( connector.getLastPingTime() ) ), new Long( 52 ) );
      final PingPolicy policy = (PingPolicy) policyMock.proxy();

      connector.setMonitor( monitor );
      connector.setPingPolicy( policy );
      connector.setActive( false );

      final long result = connector.checkPing();
      assertEquals( "result", 52, result );
      final long now = System.currentTimeMillis();
      assertEqualTime( "getLastPingTime", now, connector.getLastPingTime() );

      policyMock.verify();
      monitorMock.verify();
   }

   public void testCheckPingThatNoPings()
      throws Exception
   {
      final Connector connector = new Connector();

      final Mock monitorMock = new Mock( ConnectorMonitor.class );
      final ConnectorMonitor monitor = (ConnectorMonitor) monitorMock.proxy();

      final Mock policyMock = new Mock( PingPolicy.class );
      policyMock.expectAndReturn( "checkPingConnection", C.args( C.eq( connector ) ), false );
      policyMock.expectAndReturn( "nextPingCheck", C.args( C.eq( connector.getLastPingTime() ) ), new Long( 52 ) );
      final PingPolicy policy = (PingPolicy) policyMock.proxy();

      connector.setMonitor( monitor );
      connector.setPingPolicy( policy );
      connector.setActive( false );

      final long result = connector.checkPing();
      assertEquals( "result", 52, result );
      assertEqualTime( "getLastPingTime", 0, connector.getLastPingTime() );

      policyMock.verify();
      monitorMock.verify();
   }

   private void assertEqualTime( final String description, final long expected, final long actual )
   {
      assertEquals( description, expected, actual, 500.0 );
   }
}
