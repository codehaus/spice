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

    public void testAvalonConfigurationWithChild()
    	throws Exception
   	{
        org.apache.avalon.framework.configuration.Configuration configuration =
            ConfigurationAlchemist.toAvalonConfiguration( createDNAConfigurationWithChild() );
        assertEquals( "name", "element", configuration.getName() );
        assertEquals( "location", "location", configuration.getLocation() );
        assertEquals( "namespace", "", configuration.getNamespace() );
        assertEquals( "attribute 1", "value1", configuration.getAttribute( "key1" ) );
        assertEquals( "attribute 2", "value2", configuration.getAttribute( "key2" ) );
        assertEquals( "child name", "child", configuration.getChild( "child" ).getName() );        
   	}

    public void testAvalonConfigurationWithValue()
    	throws Exception
	{
	    org.apache.avalon.framework.configuration.Configuration configuration =
	        ConfigurationAlchemist.toAvalonConfiguration( createDNAConfigurationWithValue() );
	    assertEquals( "name", "element", configuration.getName() );
	    assertEquals( "location", "location", configuration.getLocation() );
	    assertEquals( "namespace", "", configuration.getNamespace() );
	    assertEquals( "attribute 1", "value1", configuration.getAttribute( "key1" ) );
	    assertEquals( "attribute 2", "value2", configuration.getAttribute( "key2" ) );
	    assertEquals( "value", "value", configuration.getValue( null ) );
	}

    private Configuration createDNAConfigurationWithChild() {
        final DefaultConfiguration configuration =
            new DefaultConfiguration( "element", "location", "" );
        configuration.setAttribute( "key1", "value1" );
        configuration.setAttribute( "key2", "value2" );
        configuration.addChild( new DefaultConfiguration( "child", "location", "/element" ) );
        return configuration;
    }    

    private Configuration createDNAConfigurationWithValue() {
        final DefaultConfiguration configuration =
            new DefaultConfiguration( "element", "location", "" );
        configuration.setAttribute( "key1", "value1" );
        configuration.setAttribute( "key2", "value2" );
        configuration.setValue( "value" );
        return configuration;
    }    
    
    public void testDNAConfigurationWithChild()
    	throws Exception
	{
        Configuration configuration = ConfigurationAlchemist.toDNAConfiguration( 
                createAvalonConfigurationWithChild() );
        assertEquals( "name", "element", configuration.getName() );
        assertEquals( "location", "location", configuration.getLocation() );
        assertEquals( "path", "", configuration.getPath() );
        assertEquals( "attribute 1", "value1", configuration.getAttribute( "key1" ) );
        assertEquals( "attribute 2", "value2", configuration.getAttribute( "key2" ) );
        assertEquals( "child name", "child", configuration.getChild( "child" ).getName() );        
	}
    
    public void testDNAConfigurationWithValue()
    	throws Exception
	{
	    Configuration configuration = ConfigurationAlchemist.toDNAConfiguration( 
	            createAvalonConfigurationWithValue() );
	    assertEquals( "name", "element", configuration.getName() );
	    assertEquals( "location", "location", configuration.getLocation() );
	    assertEquals( "path", "", configuration.getPath() );
	    assertEquals( "attribute 1", "value1", configuration.getAttribute( "key1" ) );
	    assertEquals( "attribute 2", "value2", configuration.getAttribute( "key2" ) );
	    assertEquals( "value", "value", configuration.getValue( null ) );
	}
    
    private org.apache.avalon.framework.configuration.Configuration createAvalonConfigurationWithChild() {
        final org.apache.avalon.framework.configuration.DefaultConfiguration configuration =
            new org.apache.avalon.framework.configuration.DefaultConfiguration( "element", "location" );
        configuration.setAttribute( "key1", "value1" );
        configuration.setAttribute( "key2", "value2" );
        configuration.addChild( new org.apache.avalon.framework.configuration.DefaultConfiguration( "child", "location" ) );
        return configuration;
    }    

    private org.apache.avalon.framework.configuration.Configuration createAvalonConfigurationWithValue() {
        final org.apache.avalon.framework.configuration.DefaultConfiguration configuration =
            new org.apache.avalon.framework.configuration.DefaultConfiguration( "element", "location" );
        configuration.setAttribute( "key1", "value1" );
        configuration.setAttribute( "key2", "value2" );
        configuration.setValue( "value" );
        return configuration;
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