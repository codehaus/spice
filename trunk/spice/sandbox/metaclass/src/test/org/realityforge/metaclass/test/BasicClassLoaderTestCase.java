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
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.MetaClassIntrospector;

/**
 * Use className and ClassLoader to get ClassDescriptor from BasicClass.
 */
public class BasicClassLoaderTestCase
    extends AbstractFeatureTestCase
{
    private static final String CLASS_NAME = "org.realityforge.metaclass.test.data.BasicClass";

    private static final int EXPECTED_MODIFIER = Modifier.PUBLIC;
    private Vector _expectedAttributes;

    public BasicClassLoaderTestCase()
    {
        super( "BasicClassLoader", CLASS_NAME );
    }

    public static Test suite()
    {
        return new TestSuite( BasicClassLoaderTestCase.class );
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
        try
        {
            final String className = getClassName();
            final ClassLoader classLoader = Class.forName( className ).getClassLoader();
            setClassDescriptor( MetaClassIntrospector.getClassInfo( className, classLoader ) );

            _expectedAttributes = new Vector();
            _expectedAttributes.add( new Attribute( "test-attribute1", "true" ) );
            _expectedAttributes.add( new Attribute( "test-attribute2", "thisIsATestString" ) );
            final Properties parameters = new Properties();
            parameters.put( "satan", "17.5" );
            _expectedAttributes.add( new Attribute( "test-attribute3", parameters ) );
        }
        catch( final Exception e )
        {
            e.printStackTrace();
            fail( e.getMessage() );
        }
    }

    public void testGetName()
    {
        assertEquals( CLASS_NAME,
                      getClassDescriptor().getName() );
    }

    public void testGetModifiers()
    {
        assertEquals( EXPECTED_MODIFIER,
                      getClassDescriptor().getModifiers() );
    }

    public void testGetAttributes()
    {
        final Attribute[] attributes = getClassDescriptor().getAttributes();
        checkAttributesMatchExpected( _expectedAttributes, attributes,
                                      "Class: getAttributes" );
    }
}
