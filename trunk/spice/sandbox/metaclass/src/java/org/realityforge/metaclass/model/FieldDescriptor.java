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
 * A descriptor that describes a Field. It contains
 * information about;
 * <ul>
 *   <li>name: the name of the field</li>
 *   <li>type: the type of the field</li>
 *   <li>modifiers: the access modifiers for the field</li>
 * </ul>
 *
 * <p>Also associated with each field is a set of arbitrary
 * Attributes that can be used to store extra information
 * about method.</p>
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @author <a href="mailto:doug at doug@stocksoftware.com.au">Doug Hagan</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-16 10:40:47 $
 */
public final class FieldDescriptor
    extends FeatureDescriptor
    implements Serializable
{
    /**
     * The name of the field.
     */
    private final String _name;

    /**
     * The type of the field.
     */
    private final String _type;

    /**
     * Create a descriptor for a field.
     *
     * @param name the name of the field
     * @param type the return type of the field
     * @param modifiers the access modifiers for field
     * @param attributes any attributes associated with method
     */
    public FieldDescriptor( final String name,
                            final String type,
                            final int modifiers,
                            final Attribute[] attributes )
    {
        super( attributes, modifiers );
        if( null == name )
        {
            throw new NullPointerException( "name" );
        }
        if( null == type )
        {
            throw new NullPointerException( "type" );
        }

        _name = name;
        _type = type;
    }

    /**
     * Return the name of the field.
     *
     * @return the name of the field.
     */
    public String getName()
    {
        return _name;
    }

    /**
     * Return the type of the field.
     *
     * @return the type of the field.
     */
    public String getType()
    {
        return _type;
    }

    /**
     * Return a string representation of the field.
     * @return a string representation of the field.
     */
    public String toString()
    {
        return "FIELD: " + getName() + ":" + getModifiers() + " (" + getType() + ")" + "\n" +
            attributesToString();
    }

    public boolean equals( final Object o )
    {
        if( !( o instanceof FieldDescriptor ) )
        {
            return false;
        }

        final FieldDescriptor other = (FieldDescriptor)o;
        return getName().equals( other.getName() ) &&
            getModifiers() == other.getModifiers() &&
            Utility.areContentsEqual( getAttributes(), other.getAttributes() ) &&
            getType().equals( other.getType() );
    }
}
