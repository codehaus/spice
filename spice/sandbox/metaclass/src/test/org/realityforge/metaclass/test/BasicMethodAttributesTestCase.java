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
import org.realityforge.metaclass.model.MethodDescriptor;
import org.realityforge.metaclass.model.ParameterDescriptor;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;

public class BasicMethodAttributesTestCase
    extends AbstractFeatureTestCase
{
    private static final String CLASS_NAME = "org.realityforge.metaclass.test.data.BasicClass";

    private static final String[] NAMES =
        {
            "BasicClass",
            "getPrivateString",
            "setPrivateString",
            "aPrivateMethod"
        };

    private static final ParameterDescriptor[][] EXPECTED_PARAMETERS =
        {
            new ParameterDescriptor[ 0 ],
            new ParameterDescriptor[ 0 ],
            {
                new ParameterDescriptor( "aPrivateString", "java.lang.String" )
            },
            new ParameterDescriptor[ 0 ]
        };

    private static final String VALUE_METHOD_NAME = NAMES[ 1 ];
    private static final String VALUE_ATTRIBUTE_NAME = "return";
    private static final String PARAMETERS_METHOD_NAME = NAMES[ 3 ];
    private static final String PARAMETERS_ATTRIBUTE_NAME = "stuff";

    private static final int NUM_METHODS = 4;

    private Vector[] _expectedAttributes;
    private Vector _expectedNamedAttributes;
    private Attribute _expectedNamedValueAttribute;
    private Attribute _expectedNamedParametersAttribute;

    private MethodDescriptor[] _methodDescriptors;

    public BasicMethodAttributesTestCase()
    {
        super( "BasicMethodAttributes", CLASS_NAME );
    }

    public static Test suite()
    {
        return new TestSuite( BasicMethodAttributesTestCase.class );
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
            _methodDescriptors = getClassDescriptor().getMethods();

            _expectedNamedValueAttribute = new Attribute( "return", "the private string" );

            final Properties parameters = new Properties();
            parameters.put( "1", "2" );
            _expectedNamedParametersAttribute = new Attribute( "stuff", parameters );

            _expectedNamedAttributes = new Vector();
            _expectedNamedAttributes.add( _expectedNamedValueAttribute );

            _expectedAttributes = new Vector[ NUM_METHODS ];
            _expectedAttributes[ 0 ] = new Vector();
            _expectedAttributes[ 1 ] = new Vector();
            _expectedAttributes[ 1 ].add( _expectedNamedValueAttribute );
            _expectedAttributes[ 2 ] = new Vector();
            _expectedAttributes[ 2 ].add( new Attribute( "param", "aPrivateString" ) );
            _expectedAttributes[ 3 ] = new Vector();
            _expectedAttributes[ 3 ].add( _expectedNamedParametersAttribute );
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
        _methodDescriptors = null;
    }

    /**
     * This test must be run first.
     */
    public void testNotNull()
    {
        assertNotNull( _methodDescriptors );
    }

    public void testGetAttributes()
    {
        try
        {
            final Class aClass = Class.forName( CLASS_NAME );
            for ( int i = 0; i < _expectedAttributes.length; i++ )
            {
                final Vector expectedAttributes = _expectedAttributes[ i ];
                final ParameterDescriptor[] expectedParameters = EXPECTED_PARAMETERS[ i ];

                final ArrayList parameterTypes = new ArrayList();
                for ( int j = 0; j < expectedParameters.length; j++ )
                {
                    final ParameterDescriptor expectedParameter = expectedParameters[ j ];
                    if ( null != expectedParameter )
                    {
                        parameterTypes.add( Class.forName( expectedParameter.getType() ) );
                    }
                }

                final int parametersCount = parameterTypes.size();
                final Class[] parameterTypeClasses = new Class[ parametersCount ];
                for ( int j = 0; j < parametersCount; j++ )
                {
                    final Class parameterType = (Class) parameterTypes.get( j );
                    parameterTypeClasses[ j ] = parameterType;
                }

                final String methodName = NAMES[ i ];
                final Attribute[] attributes =
                    MetaClassTestUtility.getAttributesForMethodOrConstructor( CLASS_NAME,
                                                                              methodName,
                                                                              aClass,
                                                                              parameterTypeClasses );
                checkAttributesMatchExpected( expectedAttributes, attributes,
                                              "getAttributes" );
            }
        }
        catch ( Exception e )
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
                MetaClassTestUtility.getAttributesForMethodOrConstructor( VALUE_METHOD_NAME,
                                                                          aClass,
                                                                          new Class[]{},
                                                                          VALUE_ATTRIBUTE_NAME );
            checkAttributesMatchExpected( _expectedNamedAttributes, attributes,
                                          "getNamedAttributes" );
        }
        catch ( Exception e )
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
                MetaClassTestUtility.getAttributeForMethodOrConstructor( VALUE_METHOD_NAME,
                                                                         aClass,
                                                                         new Class[]{},
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
                Attributes.getAttribute( aClass.getDeclaredMethod( PARAMETERS_METHOD_NAME,
                                                                   new Class[]{} ),
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
            e.printStackTrace();
            fail( e.getMessage() );
        }
    }
}
