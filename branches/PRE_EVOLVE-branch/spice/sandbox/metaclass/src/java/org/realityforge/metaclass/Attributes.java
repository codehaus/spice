/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass;

import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.model.FieldDescriptor;
import org.realityforge.metaclass.model.MethodDescriptor;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Attributes
{
    /**
     * Get Attributes for a Class.
     * @param clazz
     * @return Attribute[] attributes
     * @throws InvalidMetaClassException
     * @throws IOException
     */
    public static Attribute[] getAttributes( final Class clazz )
        throws InvalidMetaClassException, IOException
    {
        final ClassDescriptor classDescriptor = MetaClassIntrospector.getClassInfo( clazz );
        return classDescriptor.getAttributes();
    }

    /**
     * Get all Attributes that share a name for a Class.
     * @param clazz The Class to get from
     * @param name The name of the Attribute to get
     * @return Attribute[] attributes
     * @throws InvalidMetaClassException
     * @throws IOException
     */
    public static Attribute[] getAttributes( final Class clazz, final String name )
        throws InvalidMetaClassException, IOException
    {
        final ClassDescriptor classDescriptor = MetaClassIntrospector.getClassInfo( clazz );
        return classDescriptor.getAttributesByName( name );
    }

    /**
     * Get a named Attribute for a Class.
     * Get all Attributes that share a name for a Class.
     * @param clazz The Class to get from
     * @param name The name of the Attribute to get
     * @return Attribute attribute
     * @throws InvalidMetaClassException
     * @throws IOException
     */
    public static Attribute getAttribute( final Class clazz, final String name )
        throws InvalidMetaClassException, IOException
    {
        final ClassDescriptor classDescriptor = MetaClassIntrospector.getClassInfo( clazz );
        return classDescriptor.getAttributeByName( name );
    }

    public static Attribute[] getAttributes( final Field field )
        throws InvalidMetaClassException, IOException
    {
        final Class declaringClass = field.getDeclaringClass();
        final FieldDescriptor fieldDescriptor = getField( declaringClass, field.getName() );
        return fieldDescriptor.getAttributes();
    }

    public static Attribute[] getAttributes( final Field field, final String name )
        throws InvalidMetaClassException, IOException
    {
        final Class declaringClass = field.getDeclaringClass();
        final FieldDescriptor fieldDescriptor = getField( declaringClass, field.getName() );
        return fieldDescriptor.getAttributesByName( name );
    }

    public static Attribute getAttribute( final Field field, final String name )
        throws InvalidMetaClassException, IOException
    {
        final Class declaringClass = field.getDeclaringClass();
        final FieldDescriptor fieldDescriptor = getField( declaringClass, field.getName() );
        return fieldDescriptor.getAttributeByName( name );
    }

    public static Attribute[] getAttributes( final Method method )
        throws InvalidMetaClassException, IOException
    {
        final Class declaringClass = method.getDeclaringClass();
        final MethodDescriptor methodDescriptor = getMethod( declaringClass, method.getName() );
        return methodDescriptor.getAttributes();
    }

    public static Attribute[] getAttributes( final Method method, final String name )
        throws InvalidMetaClassException, IOException
    {
        final Class declaringClass = method.getDeclaringClass();
        final MethodDescriptor methodDescriptor = getMethod( declaringClass, method.getName() );
        return methodDescriptor.getAttributesByName( name );
    }

    public static Attribute getAttribute( final Method method, final String name )
        throws InvalidMetaClassException, IOException
    {
        final Class declaringClass = method.getDeclaringClass();
        final MethodDescriptor methodDescriptor = getMethod( declaringClass, method.getName() );
        return methodDescriptor.getAttributeByName( name );
    }

    public static Attribute[] getAttributes( final Constructor constructor )
        throws InvalidMetaClassException, IOException
    {
        final Class declaringClass = constructor.getDeclaringClass();
        final MethodDescriptor methodDescriptor = getMethod( declaringClass, constructor.getName() );
        return methodDescriptor.getAttributes();
    }

    public static Attribute[] getAttributes( final Constructor constructor, final String name )
        throws InvalidMetaClassException, IOException
    {
        final Class declaringClass = constructor.getDeclaringClass();
        final MethodDescriptor methodDescriptor = getMethod( declaringClass, constructor.getName() );
        return methodDescriptor.getAttributesByName( name );
    }

    public static Attribute getAttribute( final Constructor constructor, final String name )
        throws InvalidMetaClassException, IOException
    {
        final Class declaringClass = constructor.getDeclaringClass();
        final MethodDescriptor methodDescriptor = getMethod( declaringClass, constructor.getName() );
        return methodDescriptor.getAttributeByName( name );
    }

    /**
     * Get a named FieldDescriptor for a Class.
     * @param clazz The Class to get from
     * @param name The name of the Field to get
     * @return FieldDescriptor A FieldDescriptor of the Field
     * @throws InvalidMetaClassException
     * @throws IOException
     */
    private static FieldDescriptor getField( final Class clazz, final String name )
        throws InvalidMetaClassException, IOException
    {
        final ClassDescriptor classDescriptor = MetaClassIntrospector.getClassInfo( clazz );
        final FieldDescriptor[] fields = classDescriptor.getFields();
        for ( int i = 0; i < fields.length; i++ )
        {
            final FieldDescriptor field = fields[ i ];
            if ( field.getName().equals( name ) )
            {
                return field;
            }
        }
        return null;
    }

    /**
     * Get a named MethodDescriptor for a Class.
     * @param clazz The Class to get from
     * @param name The name of the Method to get
     * @return MethodDescriptor A MethodDescriptor of the Method
     * @throws InvalidMetaClassException
     * @throws IOException
     */
    private static MethodDescriptor getMethod( final Class clazz, final String name )
        throws InvalidMetaClassException, IOException
    {
        final ClassDescriptor classDescriptor = MetaClassIntrospector.getClassInfo( clazz );
        final MethodDescriptor[] methods = classDescriptor.getMethods();
        for ( int i = 0; i < methods.length; i++ )
        {
            final MethodDescriptor method = methods[ i ];
            if ( method.getName().equals( ClassDescriptorUtility.getJustClassName( name ) ) )
            {
                return method;
            }
        }
        return null;
    }
}
