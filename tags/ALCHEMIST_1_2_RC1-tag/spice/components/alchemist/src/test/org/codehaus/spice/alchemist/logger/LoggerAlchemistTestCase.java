/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.alchemist.logger;

import org.codehaus.dna.LogEnabled;
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
            fail("Expected NPE");
        }
        catch( NullPointerException e )
        {
            assertEquals( "NPE message", "logger", e.getMessage() );
        }
    }

    public void testAvalonLoggerWithNullLogger()
        throws Exception
    {
        try
        {
            LoggerAlchemist.toAvalonLogger( null );
            fail("Expected NPE");
        }
        catch( NullPointerException e )
        {
            assertEquals( "NPE message", "logger", e.getMessage() );
        }
    }
    
    public void testIsAvalonLogEnabled()
    {
        MockAvalonLogEnabled avalonLogEnabled = new MockAvalonLogEnabled();
        MockDNALogEnabled dnaLogEnabled = new MockDNALogEnabled();
        assertTrue("Avalon LogEnabled", LoggerAlchemist.isAvalonLogEnabled(avalonLogEnabled));
        assertTrue("!Avalon LogEnabled", !LoggerAlchemist.isAvalonLogEnabled(dnaLogEnabled));
    }

    public void testToAvalonLogEnabled()
    {
        MockAvalonLogEnabled avalonLogEnabled = new MockAvalonLogEnabled();
        MockDNALogEnabled dnaLogEnabled = new MockDNALogEnabled();
        assertTrue("Avalon LogEnabled", 
                (LoggerAlchemist.toAvalonLogEnabled(avalonLogEnabled) 
                 instanceof org.apache.avalon.framework.logger.LogEnabled ));
        try {
            LoggerAlchemist.toAvalonLogEnabled( null );
            fail("Expected IllegalArgumentException");
        } catch ( IllegalArgumentException e ) {
            final String message = "Object is not Avalon LogEnabled";
            assertEquals( "IAE message", message, e.getMessage() );
        }
        try {
            LoggerAlchemist.toAvalonLogEnabled( dnaLogEnabled );
            fail("Expected IllegalArgumentException");
        } catch ( IllegalArgumentException e ) {
            final String message = dnaLogEnabled.getClass().getName()+ " is not Avalon LogEnabled";
            assertEquals( "IAE message", message, e.getMessage() );
        }
    }
    
    public void testIsDNALogEnabled()
    {
        MockAvalonLogEnabled avalonLogEnabled = new MockAvalonLogEnabled();
        MockDNALogEnabled dnaLogEnabled = new MockDNALogEnabled();
        assertTrue("DNA LogEnabled", LoggerAlchemist.isDNALogEnabled(dnaLogEnabled));
        assertTrue("!DNA LogEnabled", !LoggerAlchemist.isDNALogEnabled(avalonLogEnabled));
    }

    public void testToDNALogEnabled()
    {
        MockAvalonLogEnabled avalonLogEnabled = new MockAvalonLogEnabled();
        MockDNALogEnabled dnaLogEnabled = new MockDNALogEnabled();
        assertTrue("DNA LogEnabled", 
                (LoggerAlchemist.toDNALogEnabled(dnaLogEnabled) instanceof LogEnabled ));
        try {
            LoggerAlchemist.toDNALogEnabled( null );
            fail("Expected IllegalArgumentException");
        } catch ( IllegalArgumentException e ) {
            final String message = "Object is not DNA LogEnabled";
            assertEquals( "IAE message", message, e.getMessage() );
        }
        try {
            LoggerAlchemist.toDNALogEnabled( avalonLogEnabled );
            fail("Expected IllegalArgumentException");
        } catch ( IllegalArgumentException e ) {
            final String message = avalonLogEnabled.getClass().getName()+ " is not DNA LogEnabled";
            assertEquals( "IAE message", message, e.getMessage() );
        }
    }
}