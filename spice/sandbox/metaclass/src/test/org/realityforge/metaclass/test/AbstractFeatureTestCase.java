/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.test;

import junit.framework.TestCase;
import org.realityforge.metaclass.MetaClassIntrospector;
import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.ClassDescriptor;

/**
 * This class requires super.setUp() to be called by subclasses in their setUp()
 * in order to set up the Attributes info.
 * This also allows the subclass to populate _attributes with expected attributes.
 */
public abstract class AbstractFeatureTestCase
    extends TestCase
{
    private String _className;
    private ClassDescriptor _classDescriptor;

    public AbstractFeatureTestCase( final String name, final String className )
    {
        super( name );
        _className = className;
    }

    /**
     * Set up before test.
     */
    protected void setUp()
    {
        try
        {
            final Class clazz = Class.forName( _className );
            _classDescriptor = MetaClassIntrospector.getClassDescriptor( clazz );
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
        _classDescriptor = null;
    }

    public ClassDescriptor getClassDescriptor()
    {
        return _classDescriptor;
    }

    protected String getClassName()
    {
        return _className;
    }

    protected void setClassDescriptor( final ClassDescriptor classDescriptor )
    {
        _classDescriptor = classDescriptor;
    }

    /**
     * This test must be run first.
     */
    public void testClassNotNull()
    {
        assertNotNull( _classDescriptor );
    }

    protected void checkAttributesMatchExpected( final Attribute[] expectedAttributes,
                                                 final Attribute[] attributes,
                                                 final String testName )
    {
        if( !areContentsEqual( expectedAttributes, attributes ) )
        {
            final StringBuffer failMessage = new StringBuffer();
            failMessage.append( "Test: " + testName + "\n" );
            failMessage.append( "Attributes are not equal:\n" );

            failMessage.append( "Expected:\n" );
            if( null == expectedAttributes )
            {
                failMessage.append( "null\n" );
            }
            else
            {
                failMessage.append( expectedAttributes.length + " elements\n" );
                for( int i = 0; i < expectedAttributes.length; i++ )
                {
                    final Attribute attribute = expectedAttributes[ i ];
                    failMessage.append( i + " = " + attribute + "\n" );
                }
            }

            failMessage.append( "Actual:\n" );
            if( null == attributes )
            {
                failMessage.append( "null\n" );
            }
            else
            {
                failMessage.append( attributes.length + " elements\n" );
                for( int i = 0; i < attributes.length; i++ )
                {
                    final Attribute attribute = attributes[ i ];
                    failMessage.append( i + " = " + attribute + "\n" );
                }
            }

            fail( failMessage.toString() );
        }
    }

    /**
     * Compares contents of an array with contents of an array.
     * Returns true if collections are of equal size
     * and each member of other is equal to the same member in the same position in original.
     * @param original
     * @param original
     * @return result
     */
    protected static boolean areContentsEqual( final Object[] original,
                                               final Object[] other )
    {
        if( null == original || null == other )
        {
            return ( null == original && null == other );
        }

        boolean valid = true;
        if( null != original && null != other )
        {
            if( original.length != other.length )
            {
                valid = false;
            }
            else
            {
                for( int i = 0; i < original.length; i++ )
                {
                    final Object originalElement = original[ i ];
                    final Object otherElement = other[ i ];
                    if( !MetaClassTestUtility.areDescriptorsEqual( originalElement,
                                                                   otherElement ) )
                    {
                        valid = false;
                        break;
                    }
                }
            }
        }
        return valid;
    }

}
