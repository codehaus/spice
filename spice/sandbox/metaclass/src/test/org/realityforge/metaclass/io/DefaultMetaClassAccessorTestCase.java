/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.io;

import junit.framework.TestCase;
import org.realityforge.metaclass.MetaClassException;
import org.realityforge.metaclass.model.PackageDescriptor;
import org.realityforge.metaclass.model.ClassDescriptor;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-08-22 04:04:33 $
 */
public class DefaultMetaClassAccessorTestCase
    extends TestCase
{
    public void testLoadAttributesForPackage()
        throws Exception
    {
        final String name = "com.biz";
        final String location = "com/biz/package-meta.binary";
        final byte[] data = new byte[]
        {
            0, 0, 0, 1, //version
            0, 7, //length of package name
            'c', 'o', 'm', '.', 'b', 'i', 'z',
            0, 0, 0, 0 //attribute count
        };
        final MockClassLoader classLoader = new MockClassLoader();
        classLoader.bindResource( location, data );
        final DefaultMetaClassAccessor accessor = new DefaultMetaClassAccessor();
        final PackageDescriptor pkg = accessor.getPackageDescriptor( name, classLoader );
        assertEquals( "pakache.name", name, pkg.getName() );
        assertEquals( "pakache.attributes.length",
                      0, pkg.getAttributes().length );
    }

    public void testLoadAttributesForDefaultPackage()
        throws Exception
    {
        final String name = "";
        final String location = "package-meta.binary";
        final byte[] data = new byte[]
        {
            0, 0, 0, 1, //version
            0, 0, //length of package name
            0, 0, 0, 0 //attribute count
        };
        final MockClassLoader classLoader = new MockClassLoader();
        classLoader.bindResource( location, data );
        final DefaultMetaClassAccessor accessor = new DefaultMetaClassAccessor();
        final PackageDescriptor pkg = accessor.getPackageDescriptor( name, classLoader );
        assertEquals( "pakache.name", name, pkg.getName() );
        assertEquals( "pakache.attributes.length",
                      0, pkg.getAttributes().length );
    }

    public void testMissingAttributesForPackage()
        throws Exception
    {
        final String name = "missing";
        final MockClassLoader classLoader = new MockClassLoader();
        final DefaultMetaClassAccessor accessor = new DefaultMetaClassAccessor();
        try
        {
            accessor.getPackageDescriptor( name, classLoader );
        }
        catch( MetaClassException e )
        {
            return;
        }
        fail( "Expected to be unable to find attributes for non existent package" );
    }

    public void testBadAttributesForPackage()
        throws Exception
    {
        final String name = "com.biz";
        final String location = "com/biz/package-meta.binary";
        final byte[] data = new byte[]
        {
            1, 2, 3, 4 //Bad version number
        };
        final MockClassLoader classLoader = new MockClassLoader();
        classLoader.bindResource( location, data );
        final DefaultMetaClassAccessor accessor = new DefaultMetaClassAccessor();
        try
        {
            accessor.getPackageDescriptor( name, classLoader );
        }
        catch( MetaClassException e )
        {
            return;
        }
        fail( "Expected to be unable to parse bad meta data" );
    }

    public void testLoadAttributesForClass()
        throws Exception
    {
        final String name = "com.biz.MyClass";
        final String location = "com/biz/MyClass-meta.binary";
        final byte[] data = new byte[]
        {
            0, 0, 0, 1, //version
            0, 15, //length of classname
            'c', 'o', 'm', '.', 'b', 'i', 'z', '.', 'M', 'y', 'C', 'l', 'a', 's', 's',
            0, 0, 0, 0, //modifers
            0, 0, 0, 0, //attribute count
            0, 0, 0, 0, //field count
            0, 0, 0, 0 //method count
        };
        final MockClassLoader classLoader = new MockClassLoader();
        classLoader.bindResource( location, data );
        final DefaultMetaClassAccessor accessor = new DefaultMetaClassAccessor();
        final ClassDescriptor clazz = accessor.getClassDescriptor( name, classLoader );
        assertEquals( "class.name", name, clazz.getName() );
        assertEquals( "class.modifiers",
                      0, clazz.getModifiers() );
        assertEquals( "class.attributes.length",
                      0, clazz.getAttributes().length );
        assertEquals( "class.methods.length",
                      0, clazz.getMethods().length );
        assertEquals( "class.fields.length",
                      0, clazz.getFields().length );
    }

    public void testMissingAttributesForClass()
        throws Exception
    {
        final String name = "missing";
        final MockClassLoader classLoader = new MockClassLoader();
        final DefaultMetaClassAccessor accessor = new DefaultMetaClassAccessor();
        try
        {
            accessor.getClassDescriptor( name, classLoader );
        }
        catch( MetaClassException e )
        {
            return;
        }
        fail( "Expected to be unable to find attributes for non existent class" );
    }

    public void testBadAttributesForClass()
        throws Exception
    {
        final String name = "com.biz.MyClass";
        final String location = "com/biz/MyClass-meta.binary";
        final byte[] data = new byte[]
        {
            1, 2, 3, 4 //Bad version number
        };
        final MockClassLoader classLoader = new MockClassLoader();
        classLoader.bindResource( location, data );
        final DefaultMetaClassAccessor accessor = new DefaultMetaClassAccessor();
        try
        {
            accessor.getClassDescriptor( name, classLoader );
        }
        catch( MetaClassException e )
        {
            return;
        }
        fail( "Expected to be unable to parse bad meta data" );
    }
}
