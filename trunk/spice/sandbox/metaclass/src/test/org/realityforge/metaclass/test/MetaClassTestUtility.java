/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.realityforge.metaclass.Attributes;
import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.model.FieldDescriptor;
import org.realityforge.metaclass.model.MethodDescriptor;
import org.realityforge.metaclass.model.ParameterDescriptor;

/**
 *
 * @author <a href="mailto:doug at doug@stocksoftware.com.au">Doug Hagan</a>
 * @version $Revision: 1.8 $ $Date: 2003-08-18 08:03:50 $
 */
public final class MetaClassTestUtility
{
    /**
     * Compares contents of an array with contents of another array.
     * Return true if collections are of equal size
     * and each member of original is found in other.
     * @param original
     * @param other
     * @return result
     */
    public static boolean areContentsEqual( final Object[] original,
                                            final Object[] other )
    {
        if( original.length != other.length )
        {
            return false;
        }
        boolean result = true;
        for( int i = 0; i < original.length &&
            result == true; i++ )
        {
            final Object originalElement = original[ i ];
            final Object otherElement = other[ i ];

            if( null == originalElement )
            {
                if( null != otherElement )
                {
                    result = false;
                }
            }
            else
            {
                if( !areDescriptorsEqual( originalElement, otherElement ) )
                {
                    result = false;
                }
            }
        }
        return result;
    }

    public static boolean areDescriptorsEqual( final Object o, final Object p )
    {
        // if at least one is null they are only equal if both are null
        if( null == o || null == p )
        {
            return ( null == o && null == p );
        }

        if( o instanceof ClassDescriptor && p instanceof ClassDescriptor )
        {
            return areClassDescriptorsEqual( (ClassDescriptor)o, (ClassDescriptor)p );
        }
        else if( o instanceof MethodDescriptor && p instanceof MethodDescriptor )
        {
            return areMethodDescriptorsEqual( (MethodDescriptor)o, (MethodDescriptor)p );
        }
        else if( o instanceof FieldDescriptor && p instanceof FieldDescriptor )
        {
            return areFieldDescriptorsEqual( (FieldDescriptor)o, (FieldDescriptor)p );
        }
        else if( o instanceof ParameterDescriptor && p instanceof ParameterDescriptor )
        {
            return areParameterDescriptorsEqual( (ParameterDescriptor)o, (ParameterDescriptor)p );
        }
        else if( o instanceof Attribute && p instanceof Attribute )
        {
            return areAttributesEqual( (Attribute)o, (Attribute)p );
        }

        if( o.getClass().getName().equals( p.getClass().getName() ) )
        {
            return o.equals( p );
        }

        return false;
    }

    public static boolean areClassDescriptorsEqual( final ClassDescriptor one,
                                                    final ClassDescriptor two )
    {
        if( null == one || null == two )
        {
            return ( null == one && null == two );
        }

        final String name2 = two.getName();
        final String name1 = one.getName();
        if( null == name1 || null == name2 )
        {
            return ( null == name1 && null == name2 );
        }

        return name2.equals( name1 ) &&
            two.getModifiers() == one.getModifiers() &&
            areContentsEqual( two.getAttributes(), one.getAttributes() ) &&
            areContentsEqual( two.getFields(), one.getFields() ) &&
            areContentsEqual( two.getMethods(), one.getMethods() );
    }

    public static boolean areMethodDescriptorsEqual( final MethodDescriptor one,
                                                     final MethodDescriptor two )
    {
        if( null == one || null == two )
        {
            return ( null == one && null == two );
        }

        final String name2 = two.getName();
        final String name1 = one.getName();
        if( null == name1 || null == name2 )
        {
            return ( null == name1 && null == name2 );
        }

        final String returnType2 = two.getReturnType();
        final String returnType1 = one.getReturnType();
        if( null == returnType1 || null == returnType2 )
        {
            return ( null == returnType1 && null == returnType2 );
        }

        return name2.equals( name1 ) &&
            two.getModifiers() == one.getModifiers() &&
            areContentsEqual( two.getAttributes(), one.getAttributes() ) &&
            returnType2.equals( returnType1 ) &&
            areContentsEqual( two.getParameters(), one.getParameters() );
    }

    public static boolean areFieldDescriptorsEqual( final FieldDescriptor one,
                                                    final FieldDescriptor two )
    {
        if( null == one || null == two )
        {
            return ( null == one && null == two );
        }

        final String name2 = two.getName();
        final String name1 = one.getName();
        if( null == name1 || null == name2 )
        {
            return ( null == name1 && null == name2 );
        }

        final String type2 = two.getType();
        final String type1 = one.getType();
        if( null == type1 || null == type2 )
        {
            return ( null == type1 && null == type2 );
        }

        return name2.equals( name1 ) &&
            two.getModifiers() == one.getModifiers() &&
            areContentsEqual( two.getAttributes(), one.getAttributes() ) &&
            type2.equals( type1 );
    }

    public static boolean areParameterDescriptorsEqual( final ParameterDescriptor one,
                                                        final ParameterDescriptor two )
    {
        if( null == one || null == two )
        {
            return ( null == one && null == two );
        }

        final String name2 = two.getName();
        final String name1 = one.getName();
        if( null == name1 || null == name2 )
        {
            return ( null == name1 && null == name2 );
        }

        final String type2 = two.getType();
        final String type1 = one.getType();
        if( null == type1 || null == type2 )
        {
            return ( null == type1 && null == type2 );
        }

        return name2.equals( name1 ) &&
            type2.equals( type1 );
    }

    public static boolean areAttributesEqual( final Attribute one, final Attribute two )
    {
        if( null == one || null == two )
        {
            return ( null == one && null == two );
        }

        final String nameOne = one.getName();
        final String nameTwo = two.getName();
        if( !nameOne.equals( nameTwo ) )
        {
            return false;
        }

        final String valueOne = one.getValue();
        final String valueTwo = two.getValue();
        if( null == valueTwo || null == valueOne )
        {
            if( null != valueTwo || null != valueOne )
            {
                return false;
            }
        }
        else if( !valueOne.equals( valueTwo ) )
        {
            return false;
        }
        if( one.getParameterCount() != two.getParameterCount() )
        {
            return false;
        }
        else
        {
            final String[] names = one.getParameterNames();
            for( int i = 0; i < names.length; i++ )
            {
                final String name = names[ i ];
                final String oneValue = one.getParameter( name );
                final String twoValue = two.getParameter( name );
                if( !oneValue.equals( twoValue ) )
                {
                    return false;
                }
            }
        }

        return true;
    }

    public static Attribute[] getAttributesForMethodOrConstructor( final String className,
                                                                   final String methodName,
                                                                   final Class aClass,
                                                                   final Class[] parameterTypeClasses )
        throws NoSuchMethodException
    {
        Attribute[] attributes;
        if( methodName.equals( getJustClassName( className ) ) )
        {
            final Constructor declaredConstructor =
                aClass.getDeclaredConstructor( parameterTypeClasses );
            attributes = Attributes.getAttributes( declaredConstructor );
        }
        else
        {
            final Method declaredMethod =
                aClass.getDeclaredMethod( methodName, parameterTypeClasses );
            attributes = Attributes.getAttributes( declaredMethod );
        }
        return attributes;
    }

    public static Attribute[] getAttributesForMethodOrConstructor( final String methodName,
                                                                   final Class aClass,
                                                                   final Class[] parameterTypeClasses,
                                                                   final String attributeName )
        throws NoSuchMethodException
    {
        Attribute[] attributes = null;
        if( methodName.equals( getJustClassName( aClass.getName() ) ) )
        {
            final Constructor declaredConstructor =
                aClass.getDeclaredConstructor( parameterTypeClasses );
            attributes = Attributes.getAttributes( declaredConstructor, attributeName );
        }
        else
        {
            final Method declaredMethod =
                aClass.getDeclaredMethod( methodName, parameterTypeClasses );
            attributes = Attributes.getAttributes( declaredMethod, attributeName );
        }
        return attributes;
    }

    public static Attribute getAttributeForMethodOrConstructor( final String methodName,
                                                                final Class aClass,
                                                                final Class[] parameterTypeClasses,
                                                                final String attributeName )
        throws NoSuchMethodException
    {
        Attribute attribute = null;
        if( methodName.equals( getJustClassName( aClass.getName() ) ) )
        {
            final Constructor declaredConstructor =
                aClass.getDeclaredConstructor( parameterTypeClasses );
            attribute = Attributes.getAttribute( declaredConstructor, attributeName );
        }
        else
        {
            final Method declaredMethod =
                aClass.getDeclaredMethod( methodName, parameterTypeClasses );
            attribute = Attributes.getAttribute( declaredMethod, attributeName );
        }
        return attribute;
    }

    private static String getJustClassName( final String classname )
    {
        int lastDot = classname.lastIndexOf( "." );
        if( lastDot < classname.length() )
        {
            lastDot++;
        }
        return classname.substring( lastDot, classname.length() );
    }
}
