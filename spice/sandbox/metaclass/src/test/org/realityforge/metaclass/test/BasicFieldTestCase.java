/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.test;

import java.lang.reflect.Modifier;
import java.util.Properties;
import java.util.Vector;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.FieldDescriptor;

public class BasicFieldTestCase
    extends AbstractFeatureTestCase
{
    private static final String CLASS_NAME = "org.realityforge.metaclass.test.data.BasicClass";
    private static final int NUM_FIELDS = 4;

    private static final String[] NAMES =
        {
            "A_CONSTANT_STRING",
            "_aPublicInt",
            "_aProtectedDouble",
            "_aPrivateString"
        };
    private static final String[] TYPES =
        {
            "java.lang.String",
            "int",
            "double",
            "java.lang.String"
        };
    private static final int[] MODIFIERS =
        {
            Modifier.PUBLIC + Modifier.STATIC + Modifier.FINAL,
            Modifier.PUBLIC,
            Modifier.PROTECTED,
            Modifier.PRIVATE
        };
    private Vector[] _expectedAttributes;

    private FieldDescriptor[] _fieldDescriptors;

    public BasicFieldTestCase()
    {
        super( "BasicField", CLASS_NAME );
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

            _expectedAttributes = new Vector[ NUM_FIELDS ];
            _expectedAttributes[ 0 ] = new Vector();
            _expectedAttributes[ 0 ].add( new Attribute( "haha", "this is javadoc for a field." ) );
            final Properties parameters = new Properties();
            parameters.put( "parameters", "true" );
            _expectedAttributes[ 1 ] = new Vector();
            _expectedAttributes[ 1 ].add( new Attribute( "hoho", parameters ) );
            _expectedAttributes[ 2 ] = new Vector();
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
            assertEquals( NAMES[ i ], fieldDescriptor.getName() );
        }
    }

    public void testType()
    {
        for( int i = 0; i < _fieldDescriptors.length; i++ )
        {
            final FieldDescriptor fieldDescriptor = _fieldDescriptors[ i ];
            assertNotNull( fieldDescriptor );
            assertEquals( TYPES[ i ], fieldDescriptor.getType() );
        }
    }

    public void testModifiers()
    {
        for( int i = 0; i < _fieldDescriptors.length; i++ )
        {
            final FieldDescriptor fieldDescriptor = _fieldDescriptors[ i ];
            assertNotNull( fieldDescriptor );
            assertEquals( MODIFIERS[ i ], fieldDescriptor.getModifiers() );
        }
    }

    public void testGetAttributes()
    {
        for( int i = 0; i < _fieldDescriptors.length; i++ )
        {
            final FieldDescriptor fieldDescriptor = _fieldDescriptors[ i ];
            assertNotNull( fieldDescriptor );

            final Attribute[] attributes = fieldDescriptor.getAttributes();
            final Vector expectedAttributes = _expectedAttributes[ i ];
            if( i < _expectedAttributes.length )
            {
                checkAttributesMatchExpected( expectedAttributes, attributes,
                                              "Field: getAttributes" + i );
            }
        }
    }
}
