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
import org.realityforge.metaclass.model.Attribute;

/**
 * Use className and ClassLoader to get ClassDescriptor from BasicClass.
 */
public class BasicClassLoaderTestCase
    extends AbstractFeatureTestCase
    implements BasicClassTestDataConstants
{
    public BasicClassLoaderTestCase()
    {
        super( "BasicClassLoader", CLASS_NAME );
    }

    public static Test suite()
    {
        return new TestSuite( BasicClassLoaderTestCase.class );
    }

    public void testGetName()
    {
        assertEquals( CLASS_NAME,
                      getClassDescriptor().getName() );
    }

    public void testGetModifiers()
    {
        assertEquals( CLASS_MODIFIER,
                      getClassDescriptor().getModifiers() );
    }

    public void testGetAttributes()
    {
        final Attribute[] attributes = getClassDescriptor().getAttributes();
        checkAttributesMatchExpected( CLASS_ATTRIBUTES, attributes,
                                      "Class: getAttributes" );
    }
}
