/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.test;

import java.lang.reflect.Modifier;
import java.util.Vector;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.realityforge.metaclass.Utility;
import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.MethodDescriptor;
import org.realityforge.metaclass.model.ParameterDescriptor;

public class BasicMethodTestCase
    extends AbstractFeatureTestCase
{
    private static final String CLASS_NAME = "org.realityforge.metaclass.test.data.BasicClass";
    private static final int NUM_METHODS = 4;

    private static final String[] NAMES =
        {
            "BasicClass",
            "getPrivateString",
            "setPrivateString",
            "aPrivateMethod"
        };
    private static final int[] EXPECTED_MODIFIERS =
        {
            Modifier.PUBLIC,
            Modifier.PUBLIC,
            Modifier.PROTECTED,
            Modifier.PRIVATE
        };
    private static final String[] EXPECTED_RETURN_TYPES =
        {
            "",
            "java.lang.String",
            "void",
            "void"
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
    private Vector[] _expectedAttributes;

    private MethodDescriptor[] _methodDescriptors;

    public BasicMethodTestCase( final String name )
    {
        super( name, CLASS_NAME );
    }

    public static Test suite()
    {
        return new TestSuite( BasicMethodTestCase.class );
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

            _expectedAttributes = new Vector[ NUM_METHODS ];
            _expectedAttributes[ 0 ] = new Vector();
            _expectedAttributes[ 1 ] = new Vector();
            _expectedAttributes[ 1 ].add( new Attribute( "return", "the private string" ) );
            _expectedAttributes[ 2 ] = new Vector();
            _expectedAttributes[ 2 ].add( new Attribute( "param", "aPrivateString" ) );
            _expectedAttributes[ 3 ] = new Vector();
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
            assertEquals( NAMES[ i ], methodDescriptor.getName() );

        }
    }

    public void testModifiers()
    {
        for( int i = 0; i < _methodDescriptors.length; i++ )
        {
            final MethodDescriptor methodDescriptor = _methodDescriptors[ i ];
            assertNotNull( methodDescriptor );
            assertEquals( EXPECTED_MODIFIERS[ i ], methodDescriptor.getModifiers() );
        }
    }

    public void testReturnType()
    {
        for( int i = 0; i < _methodDescriptors.length; i++ )
        {
            final MethodDescriptor methodDescriptor = _methodDescriptors[ i ];
            assertNotNull( methodDescriptor );
            assertEquals( EXPECTED_RETURN_TYPES[ i ], methodDescriptor.getReturnType() );
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
            assertTrue( Utility.areContentsEqual( EXPECTED_PARAMETERS[ i ],
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
            if( i < _expectedAttributes.length )
            {
                checkAttributesMatchExpected( _expectedAttributes[ i ], attributes,
                                              "Method: getAttributes" + i );
            }
        }
    }
}
