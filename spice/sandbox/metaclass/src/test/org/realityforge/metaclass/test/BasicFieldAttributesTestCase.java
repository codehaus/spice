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
import org.realityforge.metaclass.Attributes;
import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.FieldDescriptor;

public class BasicFieldAttributesTestCase
    extends AbstractFeatureTestCase
    implements BasicClassTestDataConstants
{
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
     * Set up before test.
     */
    protected void setUp()
    {
        super.setUp();
        try
        {
            _fieldDescriptors = getClassDescriptor().getFields();
        }
        catch( final Exception e )
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
                Attributes.getAttributes( aClass.getDeclaredField( FIELD_NAMES[ 1 ] ) );
            for( int i = 0; i < FIELD_ATTRIBUTES.length; i++ )
            {
                final Attribute[] expectedAttributes = FIELD_ATTRIBUTES[ i ];
                checkAttributesMatchExpected( expectedAttributes, attributes,
                                              "getAttributes" );
            }
        }
        catch( Exception e )
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
                Attributes.getAttributes( aClass.getDeclaredField( FIELD_NAMES[ 0 ] ),
                                          FIELD_0_TAG_0_NAME );
            checkAttributesMatchExpected( FIELD_0_ATTRIBUTES_NAMED_TAG_0, attributes,
                                          "getNamedAttributes" );
        }
        catch( Exception e )
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
                Attributes.getAttribute( aClass.getDeclaredField( FIELD_NAMES[ 0 ] ),
                                         FIELD_0_TAG_0_NAME );
            if( !MetaClassTestUtility.areAttributesEqual( FIELD_0_TAG_0,
                                                          attribute ) )
            {
                fail( "getNamedValueAttribute: Attributes not equal:\n" +
                      FIELD_0_TAG_0 + " != " + attribute );
            }
        }
        catch( Exception e )
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
                Attributes.getAttribute( aClass.getDeclaredField( FIELD_NAMES[ 1 ] ),
                                         FIELD_1_TAG_0_NAME );
            if( !MetaClassTestUtility.areAttributesEqual( FIELD_1_TAG_0,
                                                          attribute ) )
            {
                fail( "getNamedParametersAttribute: Attributes not equal:\n" +
                      FIELD_1_TAG_0 + " != " + attribute );
            }
        }
        catch( Exception e )
        {
            fail( e.getMessage() );
        }
    }
}
