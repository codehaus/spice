/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.alchemist;

import junit.framework.TestCase;

public class LoggerAlchemistTestCase
    extends TestCase
{
    private MockLogger m_mockLogger;

    public void testDNALoggerWithNullLogger()
        throws Exception
    {
        try
        {
            LoggerAlchemist.toDNALogger( null );
        }
        catch( NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "logger", npe.getMessage() );
        }
    }

    public void testAvalonLoggerWithNullLogger()
        throws Exception
    {
        try
        {
            LoggerAlchemist.toAvalonLogger( null );
        }
        catch( NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "logger", npe.getMessage() );
        }
    }

}