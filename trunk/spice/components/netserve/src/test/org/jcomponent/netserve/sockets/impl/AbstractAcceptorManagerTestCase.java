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
 * @version $Revision: 1.3 $ $Date: 2003-10-09 07:35:24 $
 */
public abstract class AbstractAcceptorManagerTestCase
    extends TestCase
{
        public void testDisconnectNonExistent()
        throws Exception
    {
        final SocketAcceptorManager manager = createAcceptorManager();
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

    protected abstract SocketAcceptorManager createAcceptorManager() throws Exception;

    protected abstract void shutdownAcceptorManager( SocketAcceptorManager manager ) throws Exception;
}
