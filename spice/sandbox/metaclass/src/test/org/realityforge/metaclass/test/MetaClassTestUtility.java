/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.test;

import org.realityforge.metaclass.Attributes;
import org.realityforge.metaclass.ClassDescriptorUtility;
import org.realityforge.metaclass.InvalidMetaClassException;
import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.model.FieldDescriptor;
import org.realityforge.metaclass.model.MethodDescriptor;
import org.realityforge.metaclass.model.ParameterDescriptor;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

/**
 *
 * @author <a href="mailto:doug at doug@stocksoftware.com.au">Doug Hagan</a>
 * @version $Revision: 1.4 $ $Date: 2003-06-25 03:54:57 $
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
        if ( original.length != other.length )
        {
            return false;
        }
        boolean result = true;
        for ( int i = 0; i < original.length &&
            result == true; i++ )
        {
            final Object originalElement = original[ i ];
            final Object otherElement = other[ i ];

            if ( null == originalElement )
            {
                if ( null != otherElement )
                {
                    result = false;
                }
            }
            else
            {
                if ( !areDescriptorsEqual( originalElement, otherElement ) )
                {
                    result = false;
                }
            }
        }
        return result;
    }

    /**
     * Compares contents of an array with contents of a vector.
     * Returns true if collections are of equal size
     * and each member of original is contained by other.
     * @param original
     * @param other
     * @return result
     */
    public static boolean areContentsEqual( final Object[] original,
                                            final Vector other )
    {
        if ( original.length != other.size() )
        {
            return false;
        }

        for ( int i = 0; i < original.length; i++ )
        {
            final Object originalElement = original[ i ];
            for ( int j = 0; j < other.size(); j++ )
            {
                final Object otherElement = other.elementAt( j );
                if ( areDescriptorsEqual( originalElement, otherElement ) )
                {
                    return true;
                }
            }
            if ( !other.contains( originalElement ) )
            {
                return false;
            }
        }
        return true;
    }

    public static boolean areDescriptorsEqual( final Object o, final Object p )
    {
        // if at least one is null they are only equal if both are null
        if ( null == o || null == p )
        {
            return ( null == o && null == p );
        }

        if ( o instanceof ClassDescriptor && p instanceof ClassDescriptor )
        {
            return areClassDescriptorsEqual( (ClassDescriptor) o, (ClassDescriptor) p );
        }
        else if ( o instanceof MethodDescriptor && p instanceof MethodDescriptor )
        {
            return areMethodDescriptorsEqual( (MethodDescriptor) o, (MethodDescriptor) p );
        }
        else if ( o instanceof FieldDescriptor && p instanceof FieldDescriptor )
        {
            return areFieldDescriptorsEqual( (FieldDescriptor) o, (FieldDescriptor) p );
        }
        else if ( o instanceof ParameterDescriptor && p instanceof ParameterDescriptor )
        {
            return areParameterDescriptorsEqual( (ParameterDescriptor) o, (ParameterDescriptor) p );
        }
        else if ( o instanceof Attribute && p instanceof Attribute )
        {
            return areAttributesEqual( (Attribute) o, (Attribute) p );
        }

        if ( o.getClass().getName().equals( p.getClass().getName() ) )
        {
            return o.equals( p );
        }

        return false;
    }

    public static boolean areClassDescriptorsEqual( final ClassDescriptor one,
                                                    final ClassDescriptor two )
    {
        if ( null == one || null == two )
        {
            return ( null == one && null == two );
        }

        final String name2 = two.getName();
        final String name1 = one.getName();
        if ( null == name1 || null == name2 )
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
        if ( null == one || null == two )
        {
            return ( null == one && null == two );
        }

        final String name2 = two.getName();
        final String name1 = one.getName();
        if ( null == name1 || null == name2 )
        {
            return ( null == name1 && null == name2 );
        }

        final String returnType2 = two.getReturnType();
        final String returnType1 = one.getReturnType();
        if ( null == returnType1 || null == returnType2 )
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
        if ( null == one || null == two )
        {
            return ( null == one && null == two );
        }

        final String name2 = two.getName();
        final String name1 = one.getName();
        if ( null == name1 || null == name2 )
        {
            return ( null == name1 && null == name2 );
        }

        final String type2 = two.getType();
        final String type1 = one.getType();
        if ( null == type1 || null == type2 )
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
        if ( null == one || null == two )
        {
            return ( null == one && null == two );
        }

        final String name2 = two.getName();
        final String name1 = one.getName();
        if ( null == name1 || null == name2 )
        {
            return ( null == name1 && null == name2 );
        }

        final String type2 = two.getType();
        final String type1 = one.getType();
        if ( null == type1 || null == type2 )
        {
            return ( null == type1 && null == type2 );
        }

        return name2.equals( name1 ) &&
            type2.equals( type1 );
    }

    public static boolean areAttributesEqual( final Attribute one, final Attribute two )
    {
        if ( null == one || null == two )
        {
            return ( null == one && null == two );
        }

        boolean valid = true;

        final String nameOne = one.getName();
        final String nameTwo = two.getName();
        if ( null == nameTwo || null == nameOne )
        {
            if ( null != nameTwo || null != nameOne )
            {
                valid = false;
            }
        }

        if ( valid )
        {
            final String valueOne = one.getValue();
            final String valueTwo = two.getValue();
            if ( null == valueTwo || null == valueOne )
            {
                if ( null != valueTwo || null != valueOne )
                {
                    valid = false;
                }
            }
            else
            {
                if ( !valueOne.equals( valueTwo ) )
                {
                    valid = false;
                }
            }
        }

        if ( valid )
        {
            final Properties parametersOne = one.getParameters();
            final Properties parametersTwo = two.getParameters();
            if ( null == parametersTwo || null == parametersOne )
            {
                if ( null != parametersTwo || null != parametersOne )
                {
                    valid = false;
                }
            }
            else
            {
                final Enumeration keySetOne = parametersOne.keys();
                final Enumeration keySetTwo = parametersTwo.keys();
                while ( keySetOne.hasMoreElements() )
                {
                    final Object oOne = keySetOne.nextElement();
                    if ( keySetTwo.hasMoreElements() )
                    {
                        final Object oTwo = keySetTwo.nextElement();
                        if ( oOne instanceof String )
                        {
                            final String stringOne = (String) oOne;
                            final String stringTwo = (String) oTwo;
                            if ( !stringOne.equals( stringTwo ) )
                            {
                                valid = false;
                                break;
                            }
                        }
                    }
                    else
                    {
                        valid = false;
                        break;
                    }
                }

                final Collection valuesOne = parametersOne.values();
                final Collection valuesTwo = parametersTwo.values();
                final Iterator iteratorTwo = valuesTwo.iterator();
                for ( final Iterator iteratorOne = valuesOne.iterator(); iteratorOne.hasNext(); )
                {
                    final String stringOne = (String) iteratorOne.next();
                    final String stringTwo = (String) iteratorTwo.next();
                    if ( !stringOne.equals( stringTwo ) )
                    {
                        valid = false;
                        break;
                    }
                }
            }
        }

        return valid;
    }

    public static Attribute[] getAttributesForMethodOrConstructor( final String className,
                                                                   final String methodName,
                                                                   final Class aClass,
                                                                   final Class[] parameterTypeClasses )
        throws NoSuchMethodException, InvalidMetaClassException, IOException
    {
        Attribute[] attributes;
        if ( methodName.equals( ClassDescriptorUtility.getJustClassName( className ) ) )
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
        throws NoSuchMethodException, InvalidMetaClassException, IOException
    {
        Attribute[] attributes = null;
        if ( methodName.equals( ClassDescriptorUtility.getJustClassName( aClass.getName() ) ) )
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
        throws NoSuchMethodException, InvalidMetaClassException, IOException
    {
        Attribute attribute = null;
        if ( methodName.equals( ClassDescriptorUtility.getJustClassName( aClass.getName() ) ) )
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
}
