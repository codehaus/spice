/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.model;

import java.io.Serializable;
import org.realityforge.metaclass.Utility;

/**
 * This class contains the meta information about a Class. It contains
 * the name of the class, access modifiers, attributes, the classes fields and
 * the classes methods.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @author <a href="mailto:doug at doug@stocksoftware.com.au">Doug Hagan</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-16 10:40:47 $
 */
public class ClassDescriptor
    extends FeatureDescriptor
    implements Serializable
{
    /**
     * The current version of MetaClass object.
     */
    public static final int VERSION = 100;

    /**
     * The name of class.
     */
    private final String _name;

    /**
     * The fields of the class.
     */
    private final FieldDescriptor[] _fields;

    /**
     * The methods in the class.
     */
    private final MethodDescriptor[] _methods;

    public ClassDescriptor( final String classname,
                            final int modifiers,
                            final Attribute[] attributes,
                            final FieldDescriptor[] fields,
                            final MethodDescriptor[] methods )
    {
        super( attributes, modifiers );

        if( null == classname )
        {
            throw new NullPointerException( "classname" );
        }
        if( null == fields )
        {
            throw new NullPointerException( "fields" );
        }
        if( null == methods )
        {
            throw new NullPointerException( "methods" );
        }

        _name = classname;
        _fields = fields;
        _methods = methods;
    }

    /**
     * Return the name of the class.
     *
     * @return the name of the class.
     */
    public String getName()
    {
        return _name;
    }

    public FieldDescriptor[] getFields()
    {
        return _fields;
    }

    public MethodDescriptor[] getMethods()
    {
        return _methods;
    }

    /**
     * Return a string representation of the class.
     * @return a string representation of the class.
     */
    public String toString()
    {
        final StringBuffer result = new StringBuffer();
        result.append( "CLASS: " + getModifiers() + " " + getName() + "\n" );
        result.append( attributesToString() );
        for( int i = 0; i < _fields.length; i++ )
        {
            final FieldDescriptor fieldDescriptor = _fields[ i ];
            result.append( fieldDescriptor + "\n" );
        }
        for( int i = 0; i < _methods.length; i++ )
        {
            final MethodDescriptor methodDescriptor = _methods[ i ];
            result.append( methodDescriptor + "\n" );
        }
        return result.toString();
    }

    public boolean equals( final Object o )
    {
        if( !( o instanceof ClassDescriptor ) )
        {
            return false;
        }

        final ClassDescriptor other = (ClassDescriptor)o;
        return getName().equals( other.getName() ) &&
            getModifiers() == other.getModifiers() &&
            Utility.areContentsEqual( getAttributes(), other.getAttributes() ) &&
            Utility.areContentsEqual( getFields(), other.getFields() ) &&
            Utility.areContentsEqual( getMethods(), other.getMethods() );
    }
}
