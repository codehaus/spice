/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets.impl;

import junit.framework.TestCase;
import java.io.IOException;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-10-23 03:44:28 $
 */
public class NIOAcceptorEntryTestCase
    extends TestCase
{
    public void testCreation()
        throws Exception
    {
        final AcceptorConfig config = createConfig();
        final MockSelectionKey key = new MockSelectionKey( null );
        final NIOAcceptorEntry entry = new NIOAcceptorEntry( config, key );
        assertEquals( "config", config, entry.getConfig() );
        assertEquals( "key", key, entry.getKey() );
    }

    public void testNullConfigInCtor()
        throws Exception
    {
        try
        {
            new NIOAcceptorEntry( null, new MockSelectionKey( null ) );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.message", "config", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to NPE for config" );
    }

    public void testNullKeyInCtor()
        throws Exception
    {
        try
        {
            new NIOAcceptorEntry( createConfig(), null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.message", "key", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to NPE for key" );
    }

    private AcceptorConfig createConfig() throws IOException
    {
        return new AcceptorConfig( "name",
                                        new MockServerSocket(),
                                        new MockSocketConnectionHandler() );
    }
}
