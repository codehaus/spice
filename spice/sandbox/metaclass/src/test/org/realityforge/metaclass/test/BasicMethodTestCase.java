/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.test;

import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.MethodDescriptor;
import org.realityforge.metaclass.model.ParameterDescriptor;

public class BasicMethodTestCase
    extends AbstractFeatureTestCase
{
    private MethodDescriptor[] _methodDescriptors;

    public BasicMethodTestCase()
    {
        super( "BasicMethod" );
    }

    /**
     * Set up before test.
     */
    protected void setUp()
    {
        super.setUp();
        try
        {
            _methodDescriptors = getClassDescriptor().getMethods();
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
        _methodDescriptors = null;
    }

    /**
     * This test must be run first.
     */
    public void testNotNull()
    {
        assertNotNull( _methodDescriptors );
    }

    public void testName()
    {
        for( int i = 0; i < _methodDescriptors.length; i++ )
        {
            final MethodDescriptor methodDescriptor = _methodDescriptors[ i ];
            assertNotNull( methodDescriptor );
            assertEquals( METHOD_NAMES[ i ], methodDescriptor.getName() );
        }
    }

    public void testModifiers()
    {
        for( int i = 0; i < _methodDescriptors.length; i++ )
        {
            final MethodDescriptor methodDescriptor = _methodDescriptors[ i ];
            assertNotNull( methodDescriptor );
            assertEquals( METHOD_MODIFIERS[ i ], methodDescriptor.getModifiers() );
        }
    }

    public void testReturnType()
    {
        for( int i = 0; i < _methodDescriptors.length; i++ )
        {
            final MethodDescriptor methodDescriptor = _methodDescriptors[ i ];
            assertNotNull( methodDescriptor );
            assertEquals( METHOD_RETURN_TYPES[ i ], methodDescriptor.getReturnType() );
        }
    }

    public void testParameters()
    {
        for( int i = 0; i < _methodDescriptors.length; i++ )
        {
            final MethodDescriptor methodDescriptor = _methodDescriptors[ i ];
            assertNotNull( methodDescriptor );
            final ParameterDescriptor[] parameters = methodDescriptor.getParameters();
            assertNotNull( parameters );
            assertTrue( MetaClassTestUtility.areContentsEqual( METHOD_PARAMETERS[ i ],
                                                               parameters ) );
        }
    }

    public void testGetAttributes()
    {
        for( int i = 0; i < _methodDescriptors.length; i++ )
        {
            final MethodDescriptor methodDescriptor = _methodDescriptors[ i ];
            assertNotNull( methodDescriptor );

            final Attribute[] attributes = methodDescriptor.getAttributes();
            if( i < METHOD_ATTRIBUTES.length )
            {
                checkAttributesMatchExpected( METHOD_ATTRIBUTES[ i ], attributes,
                                              "Method: getAttributes" + i );
            }
        }
    }
}
