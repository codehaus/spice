/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.sca.connector;

import junit.framework.TestCase;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003-12-05 05:39:33 $
 */
public class AlwaysReconnectPolicyTestCase
    extends TestCase
{
    public void testLimitingReconnectPolicy()
        throws Exception
    {
        final LimitingReconnectPolicy policy = new LimitingReconnectPolicy( 3,
                                                                            3000 );
        final long now = System.currentTimeMillis();
        assertEquals( "attemptConnection",
                      true,
                      policy.attemptConnection( now, 0 ) );
        assertEquals( "disconnectOnError",
                      true,
                      policy.disconnectOnError( new Throwable() ) );
        assertEquals( "reconnectOnDisconnect",
                      true,
                      policy.reconnectOnDisconnect() );
    }

    public void testUnderThreshold()
        throws Exception
    {
        final LimitingReconnectPolicy policy = new LimitingReconnectPolicy( 3,
                                                                            3000 );
        final long now = System.currentTimeMillis();
        assertEquals( "attemptConnection",
                      true,
                      policy.attemptConnection( now, 2 ) );
    }

    public void testOnThresholdNoDelay()
        throws Exception
    {
        final LimitingReconnectPolicy policy = new LimitingReconnectPolicy( 3,
                                                                            3000 );
        final long now = System.currentTimeMillis();
        assertEquals( "attemptConnection",
                      false,
                      policy.attemptConnection( now, 3 ) );
    }

    public void testOnThresholdWithDelay()
        throws Exception
    {
        final LimitingReconnectPolicy policy = new LimitingReconnectPolicy( 3,
                                                                            3000 );
        final long now = System.currentTimeMillis();
        assertEquals( "attemptConnection",
                      true,
                      policy.attemptConnection( now - 5000, 3 ) );
    }

    public void testOverThresholdNoDelay()
        throws Exception
    {
        final LimitingReconnectPolicy policy = new LimitingReconnectPolicy( 3,
                                                                            3000 );
        final long now = System.currentTimeMillis();
        assertEquals( "attemptConnection",
                      false,
                      policy.attemptConnection( now, 4 ) );
    }

    public void testOverThresholdWithDelay()
        throws Exception
    {
        final LimitingReconnectPolicy policy = new LimitingReconnectPolicy( 3,
                                                                            3000 );
        final long now = System.currentTimeMillis();
        assertEquals( "attemptConnection",
                      true,
                      policy.attemptConnection( now - 5000, 4 ) );
    }
}
