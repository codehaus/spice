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
import org.realityforge.metaclass.model.FieldDescriptor;

import java.util.Properties;

public class BasicFieldAttributesTestCase
    extends AbstractFeatureTestCase
{
    private static final String CLASS_NAME = "org.realityforge.metaclass.test.data.BasicClass";
    private static final String[] NAMES =
        {
            "A_CONSTANT_STRING",
            "_aPublicInt",
            "_aProtectedDouble",
            "_aPrivateString"
        };
    private static final String VALUE_FIELD_NAME = NAMES[ 0 ];
    private static final String VALUE_ATTRIBUTE_NAME = "haha";
    private static final String PARAMETERS_FIELD_NAME = NAMES[ 1 ];
    private static final String PARAMETERS_ATTRIBUTE_NAME = "hoho";
    private static final int NUM_FIELDS = 4;

    private Attribute[][] _expectedAttributes;
    private Attribute[] _expectedNamedAttributes;
    private Attribute _expectedNamedValueAttribute;
    private Attribute _expectedNamedParametersAttribute;

    private FieldDescriptor[] _fieldDescriptors;

    public BasicFieldAttributesTestCase()
    {
        super( "BasicFieldAttributes", CLASS_NAME );
    }

    public static Test suite()
    {
        return new TestSuite( BasicFieldTestCase.class );
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
            _fieldDescriptors = getClassDescriptor().getFields();

            _expectedNamedValueAttribute = new Attribute( "haha", "this is javadoc for a field" );

            final Properties parameters = new Properties();
            parameters.put( "parameters", "true" );
            _expectedNamedParametersAttribute = new Attribute( "hoho", parameters );

            _expectedNamedAttributes = new Attribute[]
            {
                _expectedNamedValueAttribute
            };

            _expectedAttributes = new Attribute[ NUM_FIELDS ][];
            _expectedAttributes[ 0 ] = new Attribute[]
            {
                _expectedNamedValueAttribute
            };
            _expectedAttributes[ 1 ] = new Attribute[]
            {
                _expectedNamedParametersAttribute
            };
            _expectedAttributes[ 2 ] = new Attribute[]{};
            _expectedAttributes[ 3 ] = new Attribute[]{};
        }
        catch ( final Exception e )
        {
            e.printStackTrace();
            fail( e.getMessage() );
        }
    }

    /**
     * Clean up after test.
     */
    protected void tearDown()
    {
        super.tearDown();
        _fieldDescriptors = null;
    }

    /**
     * This test must be run first.
     */
    public void testNotNull()
    {
        assertNotNull( _fieldDescriptors );
    }

    public void testGetAttributes()
    {
        try
        {
            final Class aClass = Class.forName( CLASS_NAME );
            final Attribute[] attributes =
                Attributes.getAttributes( aClass.getDeclaredField( PARAMETERS_FIELD_NAME ) );
            for ( int i = 0; i < _expectedAttributes.length; i++ )
            {
                final Attribute[] expectedAttributes = _expectedAttributes[ i ];
                checkAttributesMatchExpected( expectedAttributes, attributes,
                                              "getAttributes" );
            }
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
            final Class aClass = Class.forName( CLASS_NAME );
            final Attribute[] attributes =
                Attributes.getAttributes( aClass.getDeclaredField( PARAMETERS_FIELD_NAME ),
                                          VALUE_ATTRIBUTE_NAME );
            checkAttributesMatchExpected( _expectedNamedAttributes, attributes,
                                          "getNamedAttributes" );
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }
    }

    public void testGetNamedValueAttribute()
    {
        try
        {
            final Class aClass = Class.forName( CLASS_NAME );
            final Attribute attribute =
                Attributes.getAttribute( aClass.getDeclaredField( VALUE_FIELD_NAME ),
                                         VALUE_ATTRIBUTE_NAME );
            if ( !MetaClassTestUtility.areAttributesEqual( _expectedNamedValueAttribute,
                                                           attribute ) )
            {
                fail( "getNamedValueAttribute: Attributes not equal:\n" +
                      _expectedNamedValueAttribute + " != " + attribute );
            }
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }
    }

    public void testGetNamedParametersAttribute()
    {
        try
        {
            final Class aClass = Class.forName( CLASS_NAME );
            final Attribute attribute =
                Attributes.getAttribute( aClass.getDeclaredField( PARAMETERS_FIELD_NAME ),
                                         PARAMETERS_ATTRIBUTE_NAME );
            if ( !MetaClassTestUtility.areAttributesEqual( _expectedNamedParametersAttribute,
                                                           attribute ) )
            {
                fail( "getNamedParametersAttribute: Attributes not equal:\n" +
                      _expectedNamedParametersAttribute + " != " + attribute );
            }
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }
    }
}
