/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.realityforge.metaclass.Attributes;
import org.realityforge.metaclass.model.Attribute;

import java.util.Properties;

/**
 * Use Class to get ClassDescriptor from BasicClass.
 */
public class BasicClassAttributesTestCase
    extends AbstractFeatureTestCase
{
    private static final String CLASS_NAME = "org.realityforge.metaclass.test.data.BasicClass";
    private static final String ATTRIBUTE_NAME_1 = "test-attribute1";

    private Attribute[] _expectedAttributes;
    private Attribute[] _expectedNamedAttributes;
    private Attribute _expectedNamedAttribute;

    public BasicClassAttributesTestCase()
    {
        super( "BasicClassAttributes", CLASS_NAME );
    }

    public static Test suite()
    {
        return new TestSuite( BasicClassAttributesTestCase.class );
    }

    /**
     * Runs the test case.
     */
    public static void main( String args[] )
    {
        TestRunner.run( suite() );
    }

    /**
     * Set up before test.
     */
    protected void setUp()
    {
        super.setUp();
        try
        {
            _expectedNamedAttribute = new Attribute( ATTRIBUTE_NAME_1, "true" );

            final Properties attributeParameters3 = new Properties();
            attributeParameters3.put( "satan", "17.5" );

            _expectedAttributes = new Attribute[]
            {
                _expectedNamedAttribute,
                new Attribute( "test-attribute2", "thisIsATestString" ),
                new Attribute( "test-attribute3", attributeParameters3 )
            };

            _expectedNamedAttributes = new Attribute[]
            {
                _expectedNamedAttribute
            };
        }
        catch ( final Exception e )
        {
            e.printStackTrace();
            fail( e.getMessage() );
        }
    }

    public void testGetAttributes()
    {
        try
        {
            final Attribute[] attributes = Attributes.getAttributes( Class.forName( CLASS_NAME ) );
            checkAttributesMatchExpected( _expectedAttributes, attributes,
                                          "getAttributes" );
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }
    }

    public void testGetNamedAttributes()
    {
        try
        {
            final Attribute[] attributes = Attributes.getAttributes( Class.forName( CLASS_NAME ),
                                                                     ATTRIBUTE_NAME_1 );
            checkAttributesMatchExpected( _expectedNamedAttributes, attributes,
                                          "getNamedAttributes" );
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }
    }

    public void testGetNamedAttribute()
    {
        try
        {
            final Attribute attribute = Attributes.getAttribute( Class.forName( CLASS_NAME ),
                                                                 ATTRIBUTE_NAME_1 );
            if ( !MetaClassTestUtility.areAttributesEqual( _expectedNamedAttribute, attribute ) )
            {
                fail( "getNamedAttribute: Attributes not equal:\n" +
                      _expectedNamedAttribute + " != " + attribute );
            }
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }
    }
}
