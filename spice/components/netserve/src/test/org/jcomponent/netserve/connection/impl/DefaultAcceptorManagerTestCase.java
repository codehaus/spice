/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.impl;

import org.jcomponent.netserve.connection.SocketAcceptorManager;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-27 05:26:54 $
 */
public class DefaultAcceptorManagerTestCase
   extends AbstractAcceptorManagerTestCase
{
   public void testConnectAndDisconnectWithTimeoutNotSet()
      throws Exception
   {
      final DefaultAcceptorManager manager = new DefaultAcceptorManager();
      manager.setMonitor( NullAcceptorMonitor.MONITOR );
      manager.setSoTimeout( 10 );
      final String name = "name";
      assertEquals( "isConnected pre connect", false, manager.isConnected( name ) );
      final ExceptOnAcceptServerSocket socket = new ExceptOnAcceptServerSocket( true );
      assertEquals( "socket.getSoTimeout pre connect", 0, socket.getSoTimeout() );
      manager.connect( name,
                       socket,
                       new MockSocketConnectionHandler() );
      assertEquals( "socket.getSoTimeout post connect", 10, socket.getSoTimeout() );
      assertEquals( "isConnected pre disconnect", true, manager.isConnected( name ) );
      manager.disconnect( name );
      assertEquals( "isConnected post disconnect", false, manager.isConnected( name ) );
   }

   public void testShutdownAcceptors()
      throws Exception
   {
      final DefaultAcceptorManager manager = new DefaultAcceptorManager();
      manager.setMonitor( NullAcceptorMonitor.MONITOR );
      manager.setSoTimeout( 10 );
      final String name = "name";
      assertEquals( "isConnected pre connect", false, manager.isConnected( name ) );
      manager.connect( name,
                       new ExceptOnAcceptServerSocket( true ),
                       new MockSocketConnectionHandler() );
      assertEquals( "isConnected pre shutdownAcceptors", true, manager.isConnected( name ) );
      manager.shutdownAcceptors();
      assertEquals( "isConnected post shutdownAcceptors", false, manager.isConnected( name ) );
   }

   public void testConnectAndDisconnectWithTimeoutSet()
      throws Exception
   {
      final DefaultAcceptorManager manager = new DefaultAcceptorManager();
      manager.setMonitor( NullAcceptorMonitor.MONITOR );
      manager.setSoTimeout( 10 );
      final String name = "name";
      assertEquals( "isConnected pre connect", false, manager.isConnected( name ) );
      final ExceptOnAcceptServerSocket socket = new ExceptOnAcceptServerSocket( true );
      socket.setSoTimeout( 123 );
      assertEquals( "socket.getSoTimeout pre connect", 123, socket.getSoTimeout() );
      manager.connect( name,
                       socket,
                       new MockSocketConnectionHandler() );
      assertEquals( "socket.getSoTimeout post connect", 123, socket.getSoTimeout() );
      assertEquals( "isConnected pre disconnect", true, manager.isConnected( name ) );
      manager.disconnect( name );
      assertEquals( "isConnected post disconnect", false, manager.isConnected( name ) );
   }

   public void testDuplicateConnect()
      throws Exception
   {
      final SocketAcceptorManager manager = createAcceptorManager();
      final String name = "name";
      assertEquals( "isConnected pre connect", false, manager.isConnected( name ) );
      final ExceptOnAcceptServerSocket socket = new ExceptOnAcceptServerSocket( true );
      manager.connect( name,
                       socket,
                       new MockSocketConnectionHandler() );
      assertEquals( "isConnected pre disconnect", true, manager.isConnected( name ) );
      try
      {
         manager.connect( name,
                          new ExceptOnAcceptServerSocket( true ),
                          new MockSocketConnectionHandler() );
      }
      catch ( final IllegalArgumentException iae )
      {
         return;
      }
      finally
      {
         socket.close();
         shutdownAcceptorManager( manager );
         assertEquals( "isConnected post disconnect", false, manager.isConnected( name ) );
      }
      fail( "Expected to fail due to duplicate connect" );
   }

   protected SocketAcceptorManager createAcceptorManager()
   {
      return new DefaultAcceptorManager();
   }

   protected void shutdownAcceptorManager( final SocketAcceptorManager manager )
   {
      ( (DefaultAcceptorManager) manager ).shutdownAcceptors();
   }
}
