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
 * @version $Revision: 1.3 $ $Date: 2003-12-05 05:39:34 $
 */
public class InactivityPingPolicyTestCase
    extends TestCase
{
    public void testInactivityPingPolicyWithNeitherTxOrRxSet()
        throws Exception
    {
        final MockConnector connector = new MockConnector( 0, 0, 0 );
        final InactivityPingPolicy policy = new InactivityPingPolicy( -1,
                                                                      connector );
        assertEquals( "nextPingCheck",
                      Long.MAX_VALUE,
                      policy.nextPingCheck() );
        assertEquals( "checkPingConnection",
                      false,
                      policy.checkPingConnection() );
    }

    public void testInactivityPingPolicyWithNeitherTxSetNeedingPing()
        throws Exception
    {
        final long now = System.currentTimeMillis();
        final long lastTx = now - 100;
        final long tx = 5;
        final MockConnector connector = new MockConnector( lastTx, 0, 0 );
        final InactivityPingPolicy policy = new InactivityPingPolicy( tx,
                                                                      -1,
                                                                      connector );
        assertEquals( "nextPingCheck",
                      lastTx + tx,
                      policy.nextPingCheck() );
        assertEquals( "checkPingConnection",
                      true,
                      policy.checkPingConnection() );
    }

    public void testInactivityPingPolicyWithNeitherTxSetNotNeedingPing()
        throws Exception
    {
        final long now = System.currentTimeMillis();
        final long lastTx = now - 100;
        final long tx = 500;
        final MockConnector connector = new MockConnector( lastTx, 0, 0 );
        final InactivityPingPolicy policy = new InactivityPingPolicy( tx,
                                                                      -1,
                                                                      connector );
        assertEquals( "nextPingCheck",
                      lastTx + tx,
                      policy.nextPingCheck() );
        assertEquals( "checkPingConnection",
                      false,
                      policy.checkPingConnection() );
    }

    public void testInactivityPingPolicyWithNeitherRxSetNeedingPing()
        throws Exception
    {
        final long now = System.currentTimeMillis();
        final long lastRx = now - 100;
        final long rx = 5;
        final MockConnector connector = new MockConnector( 0, lastRx, 0 );
        final InactivityPingPolicy policy = new InactivityPingPolicy( -1,
                                                                      rx,
                                                                      connector );
        assertEquals( "nextPingCheck",
                      lastRx + rx,
                      policy.nextPingCheck() );
        assertEquals( "checkPingConnection",
                      true,
                      policy.checkPingConnection() );
    }

    public void testInactivityPingPolicyWithNeitherRxSetNotNeedingPing()
        throws Exception
    {
        final long now = System.currentTimeMillis();
        final long lastRx = now - 100;
        final long rx = 500;
        final MockConnector connector = new MockConnector( 0, lastRx, 0 );
        final InactivityPingPolicy policy = new InactivityPingPolicy( -1,
                                                                      rx,
                                                                      connector );
        assertEquals( "nextPingCheck",
                      lastRx + rx,
                      policy.nextPingCheck() );
        assertEquals( "checkPingConnection",
                      false,
                      policy.checkPingConnection() );
    }
}
