/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netserve.connection.impl;

import java.net.ServerSocket;

import junit.framework.TestCase;

/**
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-03-21 23:42:59 $
 */
public class AcceptorConfigTestCase
   extends TestCase
{
   public void testCreation()
      throws Exception
   {
      final String name = "name";
      final ServerSocket serverSocket = new ServerSocket();
      final MockSocketConnectionHandler handler = new MockSocketConnectionHandler();
      final AcceptorConfig config =
         new AcceptorConfig( name, serverSocket, handler );
      assertEquals( "name", name, config.getName() );
      assertEquals( "serverSocket", serverSocket, config.getServerSocket() );
      assertEquals( "handler", handler, config.getHandler() );
   }

   public void testNullNameInCtor()
      throws Exception
   {
      try
      {
         new AcceptorConfig( null,
                             new ServerSocket(),
                             new MockSocketConnectionHandler() );
      }
      catch ( final NullPointerException npe )
      {
         assertEquals( "npe.message", "name", npe.getMessage() );
         return;
      }
      fail( "Expected to fail due to NPE for name" );
   }

   public void testNullServerSocketInCtor()
      throws Exception
   {
      try
      {
         new AcceptorConfig( "name",
                             null,
                             new MockSocketConnectionHandler() );
      }
      catch ( final NullPointerException npe )
      {
         assertEquals( "npe.message", "serverSocket", npe.getMessage() );
         return;
      }
      fail( "Expected to fail due to NPE for serverSocket" );
   }

   public void testNullHandlerInCtor()
      throws Exception
   {
      try
      {
         new AcceptorConfig( "name",
                             new ServerSocket(),
                             null );
      }
      catch ( NullPointerException npe )
      {
         assertEquals( "npe.message", "handler", npe.getMessage() );
         return;
      }
      fail( "Expected to fail due to NPE for handler" );
   }
}
