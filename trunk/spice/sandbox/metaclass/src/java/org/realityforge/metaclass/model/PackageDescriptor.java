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
 * This class contains the meta information about a Package. It contains
 * the name of the package and associated attributes.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-08-25 13:12:45 $
 */
public class PackageDescriptor
    implements Serializable
{
    /**
     * The name of the package.
     */
    private final String m_name;

    /**
     * The arbitrary set of Attributes associated with Package.
     */
    private final Attribute[] m_attributes;

    /**
     * Create a PackageDescriptor with specified name and attributes.
     *
     * @param name the name
     * @param attributes the attributes
     */
    public PackageDescriptor( final String name,
                              final Attribute[] attributes )
    {
        if( null == name )
        {
            throw new NullPointerException( "name" );
        }
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

        m_name = name;
        m_attributes = attributes;
    }

    /**
     * Return the name of the package.
     *
     * @return the name of the package.
     */
    public String getName()
    {
        return m_name;
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
}
