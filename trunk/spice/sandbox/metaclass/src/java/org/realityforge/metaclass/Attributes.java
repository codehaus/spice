/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.model.FieldDescriptor;
import org.realityforge.metaclass.model.MethodDescriptor;

/**
 * Utility class to make it easy to access attributes for
 * classes and methods. The utility class makes it possible
 * to access attributes for methods and fields by using one
 * method call such as;
 * <pre>
 *    //Get all Class attributes for 'MyClass'
 *    Attribute[] attributes =
 *           Attributes.getAttributes( MyClass.class );
 *
 *    //Get all Class attributes for 'MyClass'
 *    //that have name 'dna.service'
 *    Attribute[] attributes =
 *           Attributes.getAttributes( MyClass.class, "dna.service" );
 *
 *    //Get attribute named 'dna.component' for 'MyClass'
 *    //Note: that this may return null
 *    Attribute attribute =
 *           Attributes.getAttribute( MyClass.class, "dna.component" );
 * </pre>
 *
 * <p>Note that none of the methods in this class throw an
 * exception. If an error occurs retrieving attributes for
 * a particular artefact (such as being unable to load
 * ClassDescriptor for class) then either an empty array
 * or a null will be returned depending on the method.</p>
 *
 * @version $Revision: 1.3 $ $Date: 2003-08-15 08:40:00 $
 */
public class Attributes
{
    /**
     * Return the Attributes with specified name
     * from specified attributes.
     *
     * @param attributes the attributes
     * @param name the name of attribute to collect
     * @return the attribute from set with specified name
     */
    public static Attribute getAttributeByName( final Attribute[] attributes,
                                                final String name )
    {
        for( int i = 0; i < attributes.length; i++ )
        {
            final Attribute attribute = attributes[ i ];
            if( attribute.getName().equals( name ) )
            {
                return attribute;
            }
        }
        return null;
    }

    /**
     * Return the set of Attributes with specified name
     * from specified attributes.
     *
     * @param attributes the attributes
     * @param name the name of attributes to collect
     * @return the attributes from set with specified name
     */
    public static Attribute[] getAttributesByName( final Attribute[] attributes,
                                                   final String name )
    {
        final ArrayList results = new ArrayList();
        for( int i = 0; i < attributes.length; i++ )
        {
            final Attribute attribute = attributes[ i ];
            final String attributeName = attribute.getName();
            if( attributeName.equals( name ) )
            {
                results.add( attribute );
            }
        }
        return (Attribute[])results.toArray( new Attribute[ results.size() ] );
    }

    /**
     * Return the attributes for specified Class.
     *
     * @param clazz the class
     * @return the 'Class' attributes
     */
    public static Attribute[] getAttributes( final Class clazz )
    {
        try
        {
            final ClassDescriptor descriptor = getClassInfo( clazz );
            return descriptor.getAttributes();
        }
        catch( final Exception e )
        {
            return Attribute.EMPTY_SET;
        }
    }

    /**
     * Return the attributes for specified Class
     * that have specified name.
     *
     * @param clazz the class
     * @param name the attribute name
     * @return the attributes
     */
    public static Attribute[] getAttributes( final Class clazz, final String name )
    {
        try
        {
            final ClassDescriptor descriptor = getClassInfo( clazz );
            return getAttributesByName( descriptor.getAttributes(),
                                        name );
        }
        catch( final Exception e )
        {
            return Attribute.EMPTY_SET;
        }
    }

    /**
     * Return the attribute for specified Class
     * that has the specified name. If there are multiple
     * attributes with the same name then the first
     * attribute will be returned.

     * @param clazz the class
     * @param name the attribute name
     * @return the attribute or null if no such attribute
     */
    public static Attribute getAttribute( final Class clazz,
                                          final String name )
    {
        try
        {
            final ClassDescriptor descriptor = getClassInfo( clazz );
            return getAttributeByName( descriptor.getAttributes(), name );
        }
        catch( final Exception e )
        {
            return null;
        }
    }

    /**
     * Return the attributes for specified Class.
     *
     * @param field the field
     * @return the 'Field' attributes
     */
    public static Attribute[] getAttributes( final Field field )
    {
        try
        {
            final Class clazz = field.getDeclaringClass();
            final FieldDescriptor descriptor =
                getField( clazz, field.getName() );
            return descriptor.getAttributes();
        }
        catch( final Exception e )
        {
            return Attribute.EMPTY_SET;
        }
    }

    /**
     * Return the attributes for specified Field
     * that have specified name.
     *
     * @param field the field
     * @param name the attribute name
     * @return the 'Field' attributes
     */
    public static Attribute[] getAttributes( final Field field,
                                             final String name )
    {
        try
        {
            final Class clazz = field.getDeclaringClass();
            final FieldDescriptor descriptor =
                getField( clazz, field.getName() );
            return getAttributesByName( descriptor.getAttributes(), name );
        }
        catch( final Exception e )
        {
            return Attribute.EMPTY_SET;
        }
    }

    /**
     * Return the attribute for specified Field
     * that has the specified name. If there are multiple
     * attributes with the same name then the first
     * attribute will be returned.

     * @param field the field
     * @param name the attribute name
     * @return the attribute or null if no such attribute
     */
    public static Attribute getAttribute( final Field field,
                                          final String name )
    {
        try
        {
            final Class clazz = field.getDeclaringClass();
            final FieldDescriptor descriptor = getField( clazz, field.getName() );
            return getAttributeByName( descriptor.getAttributes(), name );
        }
        catch( Exception e )
        {
            return null;
        }
    }

    /**
     * Return the attributes for specified Method.
     *
     * @param method the method
     * @return the 'Method' attributes
     */
    public static Attribute[] getAttributes( final Method method )
    {
        try
        {
            final Class clazz = method.getDeclaringClass();
            final MethodDescriptor descriptor = getMethod( clazz, method.getName() );
            return descriptor.getAttributes();
        }
        catch( final Exception e )
        {
            return Attribute.EMPTY_SET;
        }
    }

    /**
     * Return the attributes for specified Method
     * that have specified name.
     *
     * @param method the Method
     * @param name the attribute name
     * @return the attributes
     */
    public static Attribute[] getAttributes( final Method method, final String name )
    {
        try
        {
            final Class clazz = method.getDeclaringClass();
            final MethodDescriptor descriptor = getMethod( clazz, method.getName() );
            return getAttributesByName( descriptor.getAttributes(), name );
        }
        catch( Exception e )
        {
            return Attribute.EMPTY_SET;
        }
    }

    /**
     * Return the attribute for specified Method
     * that has the specified name. If there are multiple
     * attributes with the same name then the first
     * attribute will be returned.

     * @param method the method
     * @param name the attribute name
     * @return the attribute or null if no such attribute
     */
    public static Attribute getAttribute( final Method method, final String name )
    {
        try
        {
            final Class clazz = method.getDeclaringClass();
            final MethodDescriptor descriptor = getMethod( clazz, method.getName() );
            return getAttributeByName( descriptor.getAttributes(), name );
        }
        catch( Exception e )
        {
            return null;
        }
    }

    /**
     * Return the attributes for specified Constructor.
     *
     * @param constructor the constructor
     * @return the 'Constructor' attributes
     */
    public static Attribute[] getAttributes( final Constructor constructor )
    {
        try
        {
            final Class clazz = constructor.getDeclaringClass();
            final MethodDescriptor descriptor = getMethod( clazz, constructor.getName() );
            return descriptor.getAttributes();
        }
        catch( Exception e )
        {
            return Attribute.EMPTY_SET;
        }
    }

    /**
     * Return the attributes for specified Constructor
     * that have specified name.
     *
     * @param constructor the Constructor
     * @param name the attribute name
     * @return the attributes
     */
    public static Attribute[] getAttributes( final Constructor constructor,
                                             final String name )
    {
        try
        {
            final Class clazz = constructor.getDeclaringClass();
            final MethodDescriptor descriptor = getMethod( clazz, constructor.getName() );
            return getAttributesByName( descriptor.getAttributes(), name );
        }
        catch( Exception e )
        {
            return Attribute.EMPTY_SET;
        }
    }

    /**
     * Return the attribute for specified Constructor
     * that has the specified name. If there are multiple
     * attributes with the same name then the first
     * attribute will be returned.

     * @param constructor the constructor
     * @param name the attribute name
     * @return the attribute or null if no such attribute
     */
    public static Attribute getAttribute( final Constructor constructor, final String name )
    {
        try
        {
            final Class clazz = constructor.getDeclaringClass();
            final MethodDescriptor descriptor = getMethod( clazz, constructor.getName() );
            return getAttributeByName( descriptor.getAttributes(), name );
        }
        catch( Exception e )
        {
            return null;
        }
    }

    /**
     * Get the FieldDescriptor with specified name
     * for specified Class.
     *
     * @param clazz the class
     * @param name the name of field
     * @return the FieldDescriptor or null if no such field
     */
    private static FieldDescriptor getField( final Class clazz,
                                             final String name )
        throws Exception
    {
        final FieldDescriptor[] fields =
            getClassInfo( clazz ).getFields();
        for( int i = 0; i < fields.length; i++ )
        {
            final FieldDescriptor field = fields[ i ];
            if( field.getName().equals( name ) )
            {
                return field;
            }
        }
        throw new Exception();
    }

    /**
     * Get the MethodDescriptor with specified name
     * for specified Class.
     *
     * @param clazz the class
     * @param name the name of field
     * @return the MethodDescriptor or null if no such field
     */
    private static MethodDescriptor getMethod( final Class clazz, final String name )
        throws Exception
    {
        final MethodDescriptor[] methods =
            getClassInfo( clazz ).getMethods();
        for( int i = 0; i < methods.length; i++ )
        {
            final MethodDescriptor method = methods[ i ];
            if( method.getName().equals( name ) )
            {
                return method;
            }
        }
        throw new Exception();
    }

    /**
     * Utility method to get ClassDescriptor for specified class.
     *
     * @param clazz the class
     * @return the ClassDescriptor
     * @throws Exception if unable to get ClassDescriptor
     */
    private static ClassDescriptor getClassInfo( final Class clazz )
        throws Exception
    {
        return MetaClassIntrospector.getClassDescriptor( clazz );
    }
}
