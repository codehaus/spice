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
 * @version $Revision: 1.2 $ $Date: 2003-10-09 01:50:14 $
 */
public class ConnectionAcceptorTestCase
    extends TestCase
{
    public void testNullNameInCtor()
        throws Exception
    {
        try
        {
            new ConnectionAcceptor( null,
                                    new MockServerSocket(),
                                    new MockSocketConnectionHandler(),
                                    new NullAcceptorMonitor() );
        }
        catch( final NullPointerException npe )
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
            new ConnectionAcceptor( "name",
                                    null,
                                    new MockSocketConnectionHandler(),
                                    new NullAcceptorMonitor() );
        }
        catch( final NullPointerException npe )
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
            new ConnectionAcceptor( "name",
                                    new MockServerSocket(),
                                    null,
                                    new NullAcceptorMonitor() );
        }
        catch( NullPointerException npe )
        {
            assertEquals( "npe.message", "handler", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to NPE for handler" );
    }

    public void testNullMonitorInCtor()
        throws Exception
    {
        try
        {
            new ConnectionAcceptor( "name",
                                    new MockServerSocket(),
                                    new MockSocketConnectionHandler(),
                                    null );
        }
        catch( NullPointerException npe )
        {
            assertEquals( "npe.message", "monitor", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to NPE for monitor" );
    }

    public void testCloseWhenNotRunning()
        throws Exception
    {
        final ConnectionAcceptor acceptor =
            new ConnectionAcceptor( "name",
                                    new MockServerSocket(),
                                    new MockSocketConnectionHandler(),
                                    new NullAcceptorMonitor() );
        assertFalse( "isRunning() pre-close()", acceptor.isRunning() );
        acceptor.close();
        assertFalse( "isRunning() post-close()", acceptor.isRunning() );
    }
}
