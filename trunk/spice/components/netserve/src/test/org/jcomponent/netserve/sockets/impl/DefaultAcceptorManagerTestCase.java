/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets.impl;

import junit.framework.TestCase;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-09 04:38:53 $
 */
public class DefaultAcceptorManagerTestCase
    extends TestCase
{
    public void testDisconnectNonExistent()
        throws Exception
    {
        final DefaultAcceptorManager manager = new DefaultAcceptorManager();
        try
        {
            manager.disconnect( "NonExistent" );
        }
        catch( IllegalArgumentException iae )
        {
            return;
        }
        fail( "Expected to fail to disconnect non existent acceptor" );
    }

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

    public void testConnectWithNullName()
        throws Exception
    {
        final DefaultAcceptorManager manager = new DefaultAcceptorManager();
        try
        {
            manager.connect( null,
                             new ExceptOnAcceptServerSocket( true ),
                             new MockSocketConnectionHandler() );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.message", "name", npe.getMessage() );
            return;
        }
        fail( "expected NPE due to null name in connect" );
    }

    public void testConnectWithNullSocket()
        throws Exception
    {
        final DefaultAcceptorManager manager = new DefaultAcceptorManager();
        try
        {
            manager.connect( "name",
                             null,
                             new MockSocketConnectionHandler() );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.message", "socket", npe.getMessage() );
            return;
        }
        fail( "expected NPE due to null socket in connect" );
    }

    public void testConnectWithNullHandler()
        throws Exception
    {
        final DefaultAcceptorManager manager = new DefaultAcceptorManager();
        try
        {
            manager.connect( "name",
                             new ExceptOnAcceptServerSocket( true ),
                             null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.message", "handler", npe.getMessage() );
            return;
        }
        fail( "expected NPE due to null handler in connect" );
    }

    public void testDuplicateConnect()
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
        assertEquals( "isConnected pre disconnect", true, manager.isConnected( name ) );
        try
        {
            manager.connect( name,
                             new ExceptOnAcceptServerSocket( true ),
                             new MockSocketConnectionHandler() );
        }
        catch( final IllegalArgumentException iae )
        {
            return;
        }
        finally
        {
            manager.shutdownAcceptors();
            assertEquals( "isConnected post disconnect", false, manager.isConnected( name ) );
        }
        fail( "Expected to fail due to duplicate connect" );
    }
}
