/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets.impl;

import junit.framework.TestCase;
import org.jcomponent.netserve.sockets.SocketAcceptorManager;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-09 07:01:51 $
 */
public abstract class AbstractAcceptorManagerTestCase
    extends TestCase
{
    public void testConnectWithNullName()
        throws Exception
    {
        final SocketAcceptorManager manager = createAcceptorManager();
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
        final SocketAcceptorManager manager = createAcceptorManager();
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
        final SocketAcceptorManager manager = createAcceptorManager();
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
        final SocketAcceptorManager manager = createAcceptorManager();
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
            shutdownAcceptorManager( manager );
            assertEquals( "isConnected post disconnect", false, manager.isConnected( name ) );
        }
        fail( "Expected to fail due to duplicate connect" );
    }

    protected abstract SocketAcceptorManager createAcceptorManager() throws Exception;

    protected abstract void shutdownAcceptorManager( SocketAcceptorManager manager ) throws Exception;
}