/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is the Abstract class for all feature descriptors.
 * Every descriptor has the capability of adding Attributes
 * of some kind. These Attributes can then be interpreted by
 * the container. The meaning of the specific Attributes will
 * be defined by future specification documents.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @author <a href="mailto:doug at doug@stocksoftware.com.au">Doug Hagan</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-16 10:40:47 $
 */
public abstract class FeatureDescriptor
    implements Serializable
{
    /**
     * The arbitrary set of Attributes associated with Component.
     */
    private final Attribute[] _attributes;

    /**
     * The modifiers for this particular feature as defined
     * in {@link java.lang.reflect.Modifier}.
     */
    private final int _modifiers;

    /**
     * Create a FeatureDescriptor with specific set of attributes.
     *
     * @param attributes the attributes
     */
    protected FeatureDescriptor( final Attribute[] attributes,
                                 final int modifiers )
    {
        if( null == attributes )
        {
            throw new NullPointerException( "attributes" );
        }

        _attributes = attributes;
        _modifiers = modifiers;
    }

    /**
     * Return the attributes associated with descriptor.
     *
     * @return the attributes associated with descriptor.
     */
    public Attribute[] getAttributes()
    {
        return _attributes;
    }

    /**
     * Return the attribute with specified name.
     *
     * @return the attribute with specified name.
     */
    public Attribute getAttributeByName( final String name )
    {
        for( int i = 0; i < _attributes.length; i++ )
        {
            final Attribute attribute = _attributes[ i ];
            if( attribute.getName().equals( name ) )
            {
                return attribute;
            }
        }
        return null;
    }

    /**
     * Return the attributes with specified name.
     *
     * @return the attributes with specified name.
     */
    public Attribute[] getAttributesByName( final String name )
    {
        final ArrayList results = new ArrayList();

        for( int i = 0; i < _attributes.length; i++ )
        {
            final Attribute attribute = _attributes[ i ];
            if( attribute.getName().equals( name ) )
            {
                results.add( attribute );
            }
        }
        return (Attribute[])results.toArray( new Attribute[ results.size() ] );
    }

    /**
     * Return the modifiers for feature decoded by
     * {@link java.lang.reflect.Modifier}.
     *
     * @return the modifiers for feature decoded by
     *         {@link java.lang.reflect.Modifier}.
     */
    public int getModifiers()
    {
        return _modifiers;
    }

    /**
     * Helper method to convert attributes into a
     * string representation.
     *
     * @return attributes converted into a string representation
     */
    protected final String attributesToString()
    {
        if( 0 == _attributes.length )
        {
            return "";
        }
        else
        {
            return String.valueOf( Arrays.asList( _attributes ) );
        }
    }
}
