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
 * @version $Revision: 1.1 $ $Date: 2003-08-23 04:59:42 $
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
}
