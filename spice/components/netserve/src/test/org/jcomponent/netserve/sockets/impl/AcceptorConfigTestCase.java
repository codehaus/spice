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
 * @version $Revision: 1.1 $ $Date: 2003-10-09 05:51:20 $
 */
public class AcceptorConfigTestCase
    extends TestCase
{
    public void testCreation()
        throws Exception
    {
        final String name = "name";
        final MockServerSocket serverSocket = new MockServerSocket();
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
                                new MockServerSocket(),
                                new MockSocketConnectionHandler() );
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
            new AcceptorConfig( "name",
                                null,
                                new MockSocketConnectionHandler() );
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
            new AcceptorConfig( "name",
                                new MockServerSocket(),
                                null );
        }
        catch( NullPointerException npe )
        {
            assertEquals( "npe.message", "handler", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to NPE for handler" );
    }
}
