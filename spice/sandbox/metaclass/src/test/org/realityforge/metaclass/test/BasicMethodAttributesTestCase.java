/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.test;

import java.util.ArrayList;
import org.realityforge.metaclass.Attributes;
import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.MethodDescriptor;
import org.realityforge.metaclass.model.ParameterDescriptor;

public class BasicMethodAttributesTestCase
    extends AbstractFeatureTestCase
{
    private MethodDescriptor[] _methodDescriptors;

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

    protected void tearDown()
    {
        super.tearDown();
        _methodDescriptors = null;
    }

    public void testNotNull()
    {
        assertNotNull( _methodDescriptors );
    }

    public void testGetAttributes()
    {
        try
        {
            final Class aClass = Class.forName( CLASS_NAME );
            for( int i = 0; i < METHOD_ATTRIBUTES.length; i++ )
            {
                final Attribute[] expectedAttributes = METHOD_ATTRIBUTES[ i ];
                final ParameterDescriptor[] expectedParameters = METHOD_PARAMETERS[ i ];

                final ArrayList parameterTypes = new ArrayList();
                for( int j = 0; j < expectedParameters.length; j++ )
                {
                    final ParameterDescriptor expectedParameter = expectedParameters[ j ];
                    if( null != expectedParameter )
                    {
                        parameterTypes.add( Class.forName( expectedParameter.getType() ) );
                    }
                }

                final int parametersCount = parameterTypes.size();
                final Class[] parameterTypeClasses = new Class[ parametersCount ];
                for( int j = 0; j < parametersCount; j++ )
                {
                    final Class parameterType = (Class)parameterTypes.get( j );
                    parameterTypeClasses[ j ] = parameterType;
                }

                final String methodName = METHOD_NAMES[ i ];
                final Attribute[] attributes =
                    MetaClassTestUtility.getAttributesForMethodOrConstructor( CLASS_NAME,
                                                                              methodName,
                                                                              aClass,
                                                                              parameterTypeClasses );
                checkAttributesMatchExpected( expectedAttributes, attributes,
                                              "getAttributes" );
            }
        }
        catch( Exception e )
        {
            e.printStackTrace();
            fail( e.getMessage() );
        }
    }

    public void testGetNamedAttributes()
    {
        try
        {
            final Class aClass = Class.forName( CLASS_NAME );
            final Attribute[] attributes =
                MetaClassTestUtility.getAttributesForMethodOrConstructor( METHOD_NAMES[ 1 ],
                                                                          aClass,
                                                                          new Class[]{},
                                                                          METHOD_1_TAG_0_NAME );
            checkAttributesMatchExpected( METHOD_1_ATTRIBUTES_NAMED_TAG_0, attributes,
                                          "getNamedAttributes" );
        }
        catch( Exception e )
        {
            e.printStackTrace();
            fail( e.getMessage() );
        }
    }

    public void testGetNamedValueAttribute()
    {
        try
        {
            final Class aClass = Class.forName( CLASS_NAME );
            final Attribute attribute =
                MetaClassTestUtility.getAttributeForMethodOrConstructor( METHOD_NAMES[ 1 ],
                                                                         aClass,
                                                                         new Class[]{},
                                                                         METHOD_1_TAG_0_NAME );
            if( !MetaClassTestUtility.areAttributesEqual( METHOD_1_TAG_0,
                                                          attribute ) )
            {
                fail( "getNamedValueAttribute: Attributes not equal:\n" +
                      METHOD_1_TAG_0 + " != " + attribute );
            }
        }
        catch( Exception e )
        {
            e.printStackTrace();
            fail( e.getMessage() );
        }
    }

    public void testGetNamedParametersAttribute()
    {
        try
        {
            final Class aClass = Class.forName( CLASS_NAME );
            final Attribute attribute =
                Attributes.getAttribute( aClass.getDeclaredMethod( METHOD_NAMES[ 3 ],
                                                                   new Class[]{} ),
                                         METHOD_3_TAG_0_NAME );
            if( !MetaClassTestUtility.areAttributesEqual( METHOD_3_TAG_0,
                                                          attribute ) )
            {
                fail( "getNamedParametersAttribute: Attributes not equal:\n" +
                      METHOD_3_TAG_0 + " != " + attribute );
            }
        }
        catch( Exception e )
        {
            e.printStackTrace();
            fail( e.getMessage() );
        }
    }
}
