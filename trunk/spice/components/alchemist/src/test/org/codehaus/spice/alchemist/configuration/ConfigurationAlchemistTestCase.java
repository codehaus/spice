/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.alchemist.configuration;

import junit.framework.TestCase;
import org.codehaus.dna.Configurable;
import org.codehaus.dna.Configuration;
import org.codehaus.dna.impl.DefaultConfiguration;

public class ConfigurationAlchemistTestCase
    extends TestCase
{
    public void testAvalonConfigurationWithNullConfiguration()
        throws Exception
    {
        try
        {
            ConfigurationAlchemist.toAvalonConfiguration( null );
            fail("Expected NPE");
        }
        catch( NullPointerException e )
        {
            assertEquals( "NPE message", "configuration", e.getMessage() );
        }
    }

    public void testDNAConfigurationWithNullConfiguration()
    	throws Exception
    {
        try
        {
            ConfigurationAlchemist.toDNAConfiguration( null );
            fail("Expected NPE");
        }
        catch( NullPointerException e )
        {
            assertEquals( "NPE message", "configuration", e.getMessage() );
        }
    }

    public void testAvalonConfiguration()
    	throws Exception
   	{
        org.apache.avalon.framework.configuration.Configuration configuration =
            ConfigurationAlchemist.toAvalonConfiguration( new DefaultConfiguration( "name", "location", "") );
        assertEquals( "configuration name", "name", configuration.getName() );
        assertEquals( "configuration location", "location", configuration.getLocation() );
        assertEquals( "configuration namespace", "", configuration.getNamespace() );
   	}
    
    public void testDNAConfiguration()
    	throws Exception
	{
        Configuration configuration = ConfigurationAlchemist.toDNAConfiguration( 
                new org.apache.avalon.framework.configuration.DefaultConfiguration( "name", "location") );
        assertEquals( "configuration name", "name", configuration.getName() );
        assertEquals( "configuration location", "location", configuration.getLocation() );
        assertEquals( "configuration path", "", configuration.getPath() );
	}
    
    public void testIsAvalonConfigurable()
    {
        MockAvalonConfigurable avalonConfigurable = new MockAvalonConfigurable();
        MockDNAConfigurable dnaConfigurable = new MockDNAConfigurable();
        assertTrue("Avalon Configurable", ConfigurationAlchemist.isAvalonConfigurable(avalonConfigurable));
        assertTrue("!Avalon Configurable", !ConfigurationAlchemist.isAvalonConfigurable(dnaConfigurable));
    }

    public void testToAvalonConfigurable()
    {
        MockAvalonConfigurable avalonConfigurable = new MockAvalonConfigurable();
        MockDNAConfigurable dnaConfigurable = new MockDNAConfigurable();
        assertTrue("Avalon Configurable", 
                (ConfigurationAlchemist.toAvalonConfigurable(avalonConfigurable) 
                 instanceof org.apache.avalon.framework.configuration.Configurable ));
        try {
            ConfigurationAlchemist.toAvalonConfigurable( null );
            fail("Expected IllegalArgumentException");
        } catch ( IllegalArgumentException e ) {
            final String message = "Object is not Avalon Configurable";
            assertEquals( "IAE message", message, e.getMessage() );
        }
        try {
            ConfigurationAlchemist.toAvalonConfigurable( dnaConfigurable );
            fail("Expected IllegalArgumentException");
        } catch ( IllegalArgumentException e ) {
            final String message = dnaConfigurable.getClass().getName()+ " is not Avalon Configurable";
            assertEquals( "IAE message", message, e.getMessage() );
        }
    }
    
    public void testIsDNAConfigurable()
    {
        MockAvalonConfigurable avalonConfigurable = new MockAvalonConfigurable();
        MockDNAConfigurable dnaConfigurable = new MockDNAConfigurable();
        assertTrue("DNA Configurable", ConfigurationAlchemist.isDNAConfigurable(dnaConfigurable));
        assertTrue("!DNA Configurable", !ConfigurationAlchemist.isDNAConfigurable(avalonConfigurable));
    }

    public void testToDNAConfigurable()
    {
        MockAvalonConfigurable avalonConfigurable = new MockAvalonConfigurable();
        MockDNAConfigurable dnaConfigurable = new MockDNAConfigurable();
        assertTrue("DNA Configurable", 
                (ConfigurationAlchemist.toDNAConfigurable(dnaConfigurable) instanceof Configurable ));
        try {
            ConfigurationAlchemist.toDNAConfigurable( null );
            fail("Expected IllegalArgumentException");
        } catch ( IllegalArgumentException e ) {
            final String message = "Object is not DNA Configurable";
            assertEquals( "IAE message", message, e.getMessage() );
        }
        try {
            ConfigurationAlchemist.toDNAConfigurable( avalonConfigurable );
            fail("Expected IllegalArgumentException");
        } catch ( IllegalArgumentException e ) {
            final String message = avalonConfigurable.getClass().getName()+ " is not DNA Configurable";
            assertEquals( "IAE message", message, e.getMessage() );
        }
    }
}