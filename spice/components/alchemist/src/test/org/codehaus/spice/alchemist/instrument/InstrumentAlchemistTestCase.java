/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.alchemist.instrument;

import junit.framework.TestCase;

public class InstrumentAlchemistTestCase extends TestCase {

    public void testDNAConfigurationWithNullConfiguration() 
    	throws Exception 
    {
        try 
        {
            InstrumentAlchemist.toDNAInstrumentManager( null );
            fail( "Expected NPE" );
        } catch ( NullPointerException e ) {
            assertEquals( "NPE message", "manager", e.getMessage() );
        }
    }
}