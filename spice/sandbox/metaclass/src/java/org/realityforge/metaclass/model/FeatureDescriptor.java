/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.model;

import java.io.Serializable;

/**
 * This is the Abstract class for all feature descriptors.
 * Every descriptor has the capability of adding Attributes
 * of some kind. These Attributes can then be interpreted by
 * the container. The meaning of the specific Attributes will
 * be defined by future specification documents.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @author <a href="mailto:doug at stocksoftware.com.au">Doug Hagan</a>
 * @version $Revision: 1.7 $ $Date: 2003-10-03 13:46:05 $
 */
public abstract class FeatureDescriptor
    implements Serializable
{
    /**
     * The arbitrary set of Attributes associated with feature.
     */
    private final Attribute[] m_attributes;

    /**
     * The modifiers for this particular feature as defined
     * in {@link java.lang.reflect.Modifier}.
     */
    private final int m_modifiers;

    /**
     * Create a FeatureDescriptor with specific set of attributes.
     *
     * @param attributes the attributes
     * @param modifiers the access modifiers for feature
     */
    protected FeatureDescriptor( final Attribute[] attributes,
                                 final int modifiers )
    {
        if( null == attributes )
        {
            throw new NullPointerException( "attributes" );
        }
        for( int i = 0; i < attributes.length; i++ )
        {
            if( null == attributes[ i ] )
            {
                throw new NullPointerException( "attributes[" + i + "]" );
            }
        }

        m_attributes = attributes;
        m_modifiers = modifiers;
    }

    /**
     * Return the attributes associated with descriptor.
     *
     * @return the attributes associated with descriptor.
     */
    public Attribute[] getAttributes()
    {
        return m_attributes;
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
        return m_modifiers;
    }
}