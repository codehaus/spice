package org.jcomponent.netserve.connection.impl;

import java.net.ServerSocket;
import java.io.IOException;

import com.mockobjects.dynamic.C;
import com.mockobjects.dynamic.Mock;
import junit.framework.TestCase;
import org.jcontainer.dna.Logger;

public class DNAAcceptorMonitorTestCase
   extends TestCase
{
   public void testDNAAcceptorMonitor()
      throws Exception
   {
      final Mock mockLogger = new Mock( Logger.class );

      final String name = "MyName";
      final ServerSocket serverSocket = new ServerSocket( 2023 );
      final IOException ioe = new IOException();

      final String acceptorCreatedMessage =
         "Creating Acceptor " + name + " on " +
         serverSocket.getInetAddress().getHostAddress() + ":" +
         serverSocket.getLocalPort() + ".";

      final String acceptorClosingMessage =
         "Closing Acceptor " + name + ".";

      final String serverSocketListening =
         "About to call accept() on ServerSocket '" + name + "'.";

      final String errorAcceptingConnectionMessage =
         "Error Accepting connection on " + name;

      final String errorClosingServerSocketMessage =
         "Error Closing Server Socket " + name;

      mockLogger.expect( "info", C.args( C.eq( acceptorCreatedMessage ) ) );
      mockLogger.expect( "info", C.args( C.eq( acceptorClosingMessage ) ) );
      mockLogger.expectAndReturn( "isDebugEnabled", C.NO_ARGS, false );
      mockLogger.expectAndReturn( "isDebugEnabled", C.NO_ARGS, true );
      mockLogger.expect( "debug", C.args( C.eq( serverSocketListening ) ) );
      mockLogger.expect( "warn", C.args( C.eq( errorAcceptingConnectionMessage ), C.eq( ioe ) ) );
      mockLogger.expect( "warn", C.args( C.eq( errorClosingServerSocketMessage ), C.eq( ioe ) ) );
      final Logger logger = (Logger) mockLogger.proxy();

      final AcceptorMonitor monitor = new DNAAcceptorMonitor( logger );
      monitor.acceptorCreated( name, serverSocket );
      monitor.acceptorClosing( name, serverSocket );
      monitor.serverSocketListening( name, serverSocket );
      monitor.serverSocketListening( name, serverSocket );
      monitor.errorAcceptingConnection( name, ioe );
      monitor.errorClosingServerSocket( name, ioe );

      mockLogger.verify();
   }

   public void testDNAAcceptorMonitorPassedNullIntoCtor()
      throws Exception
   {
      try
      {
         new DNAAcceptorMonitor( null );
      }
      catch ( final NullPointerException npe )
      {
         assertEquals( "npe.getMessage()", "logger", npe.getMessage() );
         return;
      }
      fail( "Expected to fail due to npe in ctor" );
   }
}
