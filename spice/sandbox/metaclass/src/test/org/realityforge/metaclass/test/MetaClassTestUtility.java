/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.test;

import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.model.MethodDescriptor;
import org.realityforge.metaclass.model.FieldDescriptor;
import org.realityforge.metaclass.model.ParameterDescriptor;
import org.realityforge.metaclass.model.Attribute;

import java.util.Vector;

/**
 *
 * @author <a href="mailto:doug at doug@stocksoftware.com.au">Doug Hagan</a>
 * @version $Revision: 1.2 $ $Date: 2003-06-05 23:46:47 $
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
        return two.getName().equals( one.getName() ) &&
            two.getModifiers() == one.getModifiers() &&
            areContentsEqual( two.getAttributes(), one.getAttributes() ) &&
            areContentsEqual( two.getFields(), one.getFields() ) &&
            areContentsEqual( two.getMethods(), one.getMethods() );
    }

    public static boolean areMethodDescriptorsEqual( final MethodDescriptor one,
                                                     final MethodDescriptor two )
    {
        return two.getName().equals( one.getName() ) &&
            two.getModifiers() == one.getModifiers() &&
            areContentsEqual( two.getAttributes(), one.getAttributes() ) &&
            two.getReturnType().equals( one.getReturnType() ) &&
            areContentsEqual( two.getParameters(), one.getParameters() );
    }

    public static boolean areFieldDescriptorsEqual( final FieldDescriptor one,
                                                    final FieldDescriptor two )
    {
        return two.getName().equals( one.getName() ) &&
            two.getModifiers() == one.getModifiers() &&
            areContentsEqual( two.getAttributes(), one.getAttributes() ) &&
            two.getType().equals( one.getType() );
    }

    public static boolean areParameterDescriptorsEqual( final ParameterDescriptor one,
                                                        final ParameterDescriptor two )
    {
        return two.getName().equals( one.getName() ) &&
            two.getType().equals( one.getType() );
    }

    public static boolean areAttributesEqual( final Attribute one, final Attribute two )
    {
        return two.getName().equals( one.getName() );
    }
}
