/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass;

import junit.framework.TestCase;
import org.realityforge.metaclass.model.Attribute;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-08-23 05:04:23 $
 */
public class AttributesTestCase
    extends TestCase
{
    public void testGetAttributeByNameWithZeroAttributes()
    {
        final Attribute attribute =
            Attributes.getAttributeByName( Attribute.EMPTY_SET, "name" );
        assertNull( "attribute", attribute );
    }

    public void testGetAttributeByNameWithNoMatchingAttributes()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( "foo" );
        final Attribute attribute2 = new Attribute( "baz" );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};
        final Attribute attribute =
            Attributes.getAttributeByName( attributes, name );
        assertNull( "attribute", attribute );
    }

    public void testGetAttributeByNameWithOneMatchingAttribute()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( name );
        final Attribute attribute2 = new Attribute( "baz" );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};
        final Attribute attribute =
            Attributes.getAttributeByName( attributes, name );
        assertNotNull( "attribute", attribute );
        assertEquals( "attribute", attribute1, attribute );
    }

    public void testGetAttributeByNameWithOneMatchingAttributes()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( name );
        final Attribute attribute2 = new Attribute( name );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};
        final Attribute attribute =
            Attributes.getAttributeByName( attributes, name );
        assertNotNull( "attribute", attribute );
        assertEquals( "attribute", attribute1, attribute );
    }

    public void testGetAttributesByNameWithZeroAttributes()
    {
        final String name = "name";
        final Attribute[] results =
            Attributes.getAttributesByName( Attribute.EMPTY_SET, name );
        assertEquals( "attributes.length", 0, results.length );
    }

    public void testGetAttributesByNameWithNoMatchingAttributes()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( "foo" );
        final Attribute attribute2 = new Attribute( "baz" );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};
        final Attribute[] results =
            Attributes.getAttributesByName( attributes, name );
        assertEquals( "attributes.length", 0, results.length );
    }

    public void testGetAttributesByNameWithOneMatchingAttribute()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( name );
        final Attribute attribute2 = new Attribute( "baz" );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};
        final Attribute[] results =
            Attributes.getAttributesByName( attributes, name );
        assertEquals( "attributes.length", 1, results.length );
        assertEquals( "attribute[ 0 ]", attribute1, results[ 0 ] );
    }

    public void testGetAttributesByNameWithOneMatchingAttributes()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( name );
        final Attribute attribute2 = new Attribute( name );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};
        final Attribute[] results =
            Attributes.getAttributesByName( attributes, name );
        assertEquals( "attributes.length", 2, results.length );
        assertEquals( "attributes[ 0 ]", attribute1, results[ 0 ] );
        assertEquals( "attributes[ 1 ]", attribute2, results[ 1 ] );
    }
}
