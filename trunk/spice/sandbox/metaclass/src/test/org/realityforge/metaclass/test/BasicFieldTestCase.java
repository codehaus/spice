/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.test;

import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.FieldDescriptor;

public class BasicFieldTestCase
    extends AbstractFeatureTestCase
    implements BasicClassTestDataConstants
{
    private FieldDescriptor[] _fieldDescriptors;

    public BasicFieldTestCase()
    {
        super( "BasicField" );
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

    public void testName()
    {
        for( int i = 0; i < _fieldDescriptors.length; i++ )
        {
            final FieldDescriptor fieldDescriptor = _fieldDescriptors[ i ];
            assertNotNull( fieldDescriptor );
            assertEquals( FIELD_NAMES[ i ], fieldDescriptor.getName() );
        }
    }

    public void testType()
    {
        for( int i = 0; i < _fieldDescriptors.length; i++ )
        {
            final FieldDescriptor fieldDescriptor = _fieldDescriptors[ i ];
            assertNotNull( fieldDescriptor );
            assertEquals( FIELD_TYPES[ i ], fieldDescriptor.getType() );
        }
    }

    public void testModifiers()
    {
        for( int i = 0; i < _fieldDescriptors.length; i++ )
        {
            final FieldDescriptor fieldDescriptor = _fieldDescriptors[ i ];
            assertNotNull( fieldDescriptor );
            assertEquals( FIELD_MODIFIERS[ i ], fieldDescriptor.getModifiers() );
        }
    }

    public void testGetAttributes()
    {
        for( int i = 0; i < _fieldDescriptors.length; i++ )
        {
            final FieldDescriptor fieldDescriptor = _fieldDescriptors[ i ];
            assertNotNull( fieldDescriptor );

            final Attribute[] attributes = fieldDescriptor.getAttributes();
            final Attribute[] expectedAttributes = FIELD_ATTRIBUTES[ i ];
            if( i < FIELD_ATTRIBUTES.length )
            {
                checkAttributesMatchExpected( expectedAttributes, attributes,
                                              "Field: getAttributes" + i );
            }
        }
    }
}
