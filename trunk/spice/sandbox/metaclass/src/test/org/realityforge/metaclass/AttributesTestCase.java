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
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.model.FieldDescriptor;
import org.realityforge.metaclass.model.MethodDescriptor;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-08-23 05:13:39 $
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

    public void testGetAttributesForClass()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( name );
        final Attribute attribute2 = new Attribute( name );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};

        final ClassDescriptor descriptor =
            new ClassDescriptor( AttributesTestCase.class.getName(),
                                 0,
                                 attributes,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        MetaClassIntrospector.setAccessor( new MockAccessor( descriptor, null ) );
        MetaClassIntrospector.clearCompleteCache();
        final Attribute[] results =
            Attributes.getAttributes( AttributesTestCase.class );
        assertEquals( "attributes.length", 2, results.length );
        assertEquals( "attributes[ 0 ]", attribute1, results[ 0 ] );
        assertEquals( "attributes[ 1 ]", attribute2, results[ 1 ] );
    }

    public void testGetAttributesForClassWithName()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( name );
        final Attribute attribute2 = new Attribute( "bleh" );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};

        final ClassDescriptor descriptor =
            new ClassDescriptor( AttributesTestCase.class.getName(),
                                 0,
                                 attributes,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        MetaClassIntrospector.setAccessor( new MockAccessor( descriptor, null ) );
        MetaClassIntrospector.clearCompleteCache();

        final Attribute[] results =
            Attributes.getAttributes( AttributesTestCase.class, name );
        assertEquals( "attributes.length", 1, results.length );
        assertEquals( "attributes[ 0 ]", attribute1, results[ 0 ] );
    }

    public void testGetAttributeForClassWithName()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( name );
        final Attribute attribute2 = new Attribute( "bleh" );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};

        final ClassDescriptor descriptor =
            new ClassDescriptor( AttributesTestCase.class.getName(),
                                 0,
                                 attributes,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        MetaClassIntrospector.setAccessor( new MockAccessor( descriptor, null ) );
        MetaClassIntrospector.clearCompleteCache();
        
        final Attribute result =
            Attributes.getAttribute( AttributesTestCase.class, name );
        assertEquals( "attribute", attribute1, result );
    }
}
