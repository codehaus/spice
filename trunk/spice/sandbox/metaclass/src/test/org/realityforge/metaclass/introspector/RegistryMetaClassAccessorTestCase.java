/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.introspector;

import junit.framework.TestCase;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.FieldDescriptor;
import org.realityforge.metaclass.model.MethodDescriptor;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-26 11:32:20 $
 */
public class RegistryMetaClassAccessorTestCase
    extends TestCase
{
    public void testGetNonExistent()
        throws Exception
    {
        final RegistryMetaClassAccessor accessor = new RegistryMetaClassAccessor();
        try
        {
            accessor.getClassDescriptor( "NoExist", getClass().getClassLoader() );
        }
        catch( final MetaClassException mce )
        {
            assertEquals( "mce.getMessage()",
                          "Missing descriptor for NoExist",
                          mce.getMessage() );
            return;
        }
        fail( "Expected to fail to get non-existent descriptor" );
    }

    public void testGetExistent()
        throws Exception
    {
        final RegistryMetaClassAccessor accessor = new RegistryMetaClassAccessor();
        final ClassDescriptor descriptor =
            new ClassDescriptor( "MyClass",
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final ClassLoader classLoader = getClass().getClassLoader();
        accessor.registerDescriptor( descriptor, classLoader );
        final ClassDescriptor result = accessor.getClassDescriptor( "MyClass", classLoader );
        assertEquals( "descriptor", descriptor, result );
    }

    public void testRegisterMultipleInSameClassLoader()
        throws Exception
    {
        final RegistryMetaClassAccessor accessor = new RegistryMetaClassAccessor();
        final ClassDescriptor descriptor1 =
            new ClassDescriptor( "MyClass",
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final ClassDescriptor descriptor2 =
            new ClassDescriptor( "MyClass2",
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final ClassLoader classLoader = getClass().getClassLoader();
        accessor.registerDescriptor( descriptor1, classLoader );
        accessor.registerDescriptor( descriptor2, classLoader );
        final ClassDescriptor result1 =
            accessor.getClassDescriptor( "MyClass", classLoader );
        assertEquals( "descriptor1", descriptor1, result1 );
        final ClassDescriptor result2 =
            accessor.getClassDescriptor( "MyClass2", classLoader );
        assertEquals( "descriptor2", descriptor2, result2 );
    }

    public void testNullDescriptorPassedToRegisterDescriptor()
        throws Exception
    {
        final RegistryMetaClassAccessor accessor = new RegistryMetaClassAccessor();
        try
        {
            accessor.registerDescriptor( null, getClass().getClassLoader() );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "descriptor", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null Descriptor passed into RegisterDescriptor" );
    }

    public void testNullClassLoaderPassedToRegisterDescriptor()
        throws Exception
    {
        final ClassDescriptor descriptor =
            new ClassDescriptor( "MyClass2",
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final RegistryMetaClassAccessor accessor = new RegistryMetaClassAccessor();
        try
        {
            accessor.registerDescriptor( descriptor, null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "classLoader", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null classLoader passed into RegisterDescriptor" );
    }
}
