/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.impl;

import java.net.ServerSocket;

import junit.framework.TestCase;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-27 05:26:54 $
 */
public class ConnectionAcceptorTestCase
   extends TestCase
{
   public void testNullConfigInCtor()
      throws Exception
   {
      try
      {
         new ConnectionAcceptor( null,
                                 new NullAcceptorMonitor() );
      }
      catch ( final NullPointerException npe )
      {
         assertEquals( "npe.message", "config", npe.getMessage() );
         return;
      }
      fail( "Expected to fail due to NPE for config" );
   }

   public void testNullMonitorInCtor()
      throws Exception
   {
      try
      {
         new ConnectionAcceptor( new AcceptorConfig( "name",
                                                     new ServerSocket(),
                                                     new MockSocketConnectionHandler() ),
                                 null );
      }
      catch ( NullPointerException npe )
      {
         assertEquals( "npe.message", "monitor", npe.getMessage() );
         return;
      }
      fail( "Expected to fail due to NPE for monitor" );
   }

   public void testShutdownServerSocketCausesError()
      throws Exception
   {
      final RecordingAcceptorMonitor monitor = new RecordingAcceptorMonitor();
      final ConnectionAcceptor acceptor =
         new ConnectionAcceptor( new AcceptorConfig( "name",
                                                     new ExceptOnCloseServerSocket(),
                                                     new MockSocketConnectionHandler() ),
                                 monitor );
      assertEquals( "errorClosingServerSocket pre-shutdownServerSocket()",
                    null,
                    monitor.getErrorClosingServerSocket() );
      acceptor.shutdownServerSocket();
      assertEquals( "errorClosingServerSocket post-shutdownServerSocket()",
                    ExceptOnCloseServerSocket.EXCEPTION,
                    monitor.getErrorClosingServerSocket() );
   }

   public void testShutdownServerSocket()
      throws Exception
   {
      final RecordingAcceptorMonitor monitor = new RecordingAcceptorMonitor();
      final ConnectionAcceptor acceptor =
         new ConnectionAcceptor( new AcceptorConfig( "name",
                                                     new ServerSocket(),
                                                     new MockSocketConnectionHandler() ),
                                 monitor );
      assertEquals( "errorClosingServerSocket pre-shutdownServerSocket()",
                    null,
                    monitor.getErrorClosingServerSocket() );
      acceptor.shutdownServerSocket();
      assertEquals( "errorClosingServerSocket post-shutdownServerSocket()",
                    null,
                    monitor.getErrorClosingServerSocket() );
   }

   public void testExceptionOnAccept()
      throws Exception
   {
      final RecordingAcceptorMonitor monitor = new RecordingAcceptorMonitor();
      final ConnectionAcceptor acceptor =
         new ConnectionAcceptor( new AcceptorConfig( "name",
                                                     new ExceptOnAcceptServerSocket( false ),
                                                     new MockSocketConnectionHandler() ),
                                 monitor );
      assertEquals( "getErrorAcceptingConnection pre-shutdownServerSocket()",
                    null,
                    monitor.getErrorAcceptingConnection() );
      final Thread thread = startAcceptor( acceptor );
      waitUntilStarted( acceptor );
      waitUntilListening( monitor );

      assertEquals( "getErrorAcceptingConnection post-shutdownServerSocket()",
                    ExceptOnAcceptServerSocket.ERROR_EXCEPTION,
                    monitor.getErrorAcceptingConnection() );

      acceptor.close( 0 );
      thread.join();
   }

   public void testInteruptOnAccept()
      throws Exception
   {
      final RecordingAcceptorMonitor monitor = new RecordingAcceptorMonitor();
      final ConnectionAcceptor acceptor =
         new ConnectionAcceptor( new AcceptorConfig( "name",
                                                     new ExceptOnAcceptServerSocket( true ),
                                                     new MockSocketConnectionHandler() ),
                                 monitor );
      final Thread thread = startAcceptor( acceptor );
      waitUntilStarted( acceptor );
      waitUntilListening( monitor );

      try
      {
         Thread.sleep( 30 );
      }
      catch ( final InterruptedException e )
      {
      }
      assertTrue( "1 < monitor.getListenCount", 1 < monitor.getListenCount() );

      acceptor.close( 0 );
      thread.join();
   }

   public void _testNormalHandlerAccept()
      throws Exception
   {
      final RecordingAcceptorMonitor monitor = new RecordingAcceptorMonitor();
      final BlockingServerSocket serverSocket = new BlockingServerSocket();
      final MockSocketConnectionHandler handler = new MockSocketConnectionHandler();
      final ConnectionAcceptor acceptor =
         new ConnectionAcceptor( new AcceptorConfig( "name",
                                                     serverSocket,
                                                     handler ),
                                 monitor );
      final Thread thread = startAcceptor( acceptor );
      waitUntilStarted( acceptor );
      waitUntilListening( monitor );
      serverSocket.unlock();
      try
      {
         Thread.sleep( 30 );
      }
      catch ( final InterruptedException e )
      {
      }
      assertEquals( "handler.getSocket()",
                    BlockingServerSocket.SOCKET,
                    handler.getSocket() );
      acceptor.close( 50 );
      serverSocket.unlock();
      thread.join();
   }

   public void testAcceptAfterClose()
      throws Exception
   {
      final RecordingAcceptorMonitor monitor = new RecordingAcceptorMonitor();
      final BlockingServerSocket serverSocket = new BlockingServerSocket();
      final MockSocketConnectionHandler handler = new MockSocketConnectionHandler();
      final ConnectionAcceptor acceptor =
         new ConnectionAcceptor( new AcceptorConfig( "name",
                                                     serverSocket,
                                                     handler ),
                                 monitor );
      final Thread thread = startAcceptor( acceptor );
      waitUntilStarted( acceptor );
      waitUntilListening( monitor );
      acceptor.close( 50 );
      serverSocket.unlock();
      waitUntilListening( monitor );
      assertEquals( "handler.getSocket()",
                    null,
                    handler.getSocket() );
      serverSocket.unlock();
      thread.join();
   }

   private void waitUntilListening( final RecordingAcceptorMonitor monitor )
   {
      while ( 0 == monitor.getListenCount() )
      {
         try
         {
            Thread.sleep( 30 );
         }
         catch ( final InterruptedException e )
         {
         }
      }
   }

   private Thread startAcceptor( final ConnectionAcceptor acceptor )
   {
      final Thread thread = new Thread( acceptor );
      thread.start();
      return thread;
   }

   private void waitUntilStarted( final ConnectionAcceptor acceptor )
   {
      while ( !acceptor.isRunning() )
      {
         try
         {
            Thread.sleep( 30 );
         }
         catch ( final InterruptedException e )
         {
         }
      }
   }
}
