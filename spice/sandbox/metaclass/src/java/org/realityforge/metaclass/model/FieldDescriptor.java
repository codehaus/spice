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
 * @version $Revision: 1.7 $ $Date: 2003-10-28 11:02:02 $
 */
public final class FieldDescriptor
    extends FeatureDescriptor
    implements Serializable
{
    /**
     * Constant for empty set of fields.
     */
    public static final FieldDescriptor[] EMPTY_SET = new FieldDescriptor[ 0 ];

    /**
     * The name of the field.
     */
    private final String m_name;

    /**
     * The type of the field.
     */
    private final String m_type;

    /**
     * Create a descriptor for a field.
     *
     * @param name the name of the field
     * @param type the return type of the field
     * @param attributes any attributes associated with method
     */
    public FieldDescriptor( final String name,
                            final String type,
                            final Attribute[] attributes )
    {
        super( attributes, attributes );
        if( null == name )
        {
            throw new NullPointerException( "name" );
        }
        if( null == type )
        {
            throw new NullPointerException( "type" );
        }

        m_name = name;
        m_type = type;
    }

    /**
     * Return the name of the field.
     *
     * @return the name of the field.
     */
    public String getName()
    {
        return m_name;
    }

    /**
     * Return the type of the field.
     *
     * @return the type of the field.
     */
    public String getType()
    {
        return m_type;
    }
}
