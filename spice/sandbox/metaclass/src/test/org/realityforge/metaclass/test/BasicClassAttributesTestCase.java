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

/**
 * Use Class to get ClassDescriptor from BasicClass.
 */
public class BasicClassAttributesTestCase
    extends AbstractFeatureTestCase
    implements BasicClassTestDataConstants
{
    public BasicClassAttributesTestCase()
    {
        super( "BasicClassAttributes", CLASS_NAME );
    }

    public static Test suite()
    {
        return new TestSuite( BasicClassAttributesTestCase.class );
    }

    /**
     * Runs the test case.
     */
    public static void main( String args[] )
    {
        TestRunner.run( suite() );
    }

    public void testGetAttributes()
    {
        try
        {
            final Attribute[] attributes = Attributes.getAttributes( Class.forName( CLASS_NAME ) );
            checkAttributesMatchExpected( CLASS_ATTRIBUTES, attributes,
                                          "getAttributes" );
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
            final Attribute[] attributes = Attributes.getAttributes( Class.forName( CLASS_NAME ),
                                                                     CLASS_TAG_0_NAME );
            checkAttributesMatchExpected( CLASS_ATTRIBUTES_NAMED_TAG_0, attributes,
                                          "getNamedAttributes" );
        }
        catch( Exception e )
        {
            fail( e.getMessage() );
        }
    }

    public void testGetNamedAttribute()
    {
        try
        {
            final Attribute attribute = Attributes.getAttribute( Class.forName( CLASS_NAME ),
                                                                 CLASS_TAG_0_NAME );
            if( !MetaClassTestUtility.areAttributesEqual( CLASS_TAG_0, attribute ) )
            {
                fail( "getNamedAttribute: Attributes not equal:\n" +
                      CLASS_TAG_0 + " != " + attribute );
            }
        }
        catch( Exception e )
        {
            fail( e.getMessage() );
        }
    }
}
