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
 * @version $Revision: 1.1 $ $Date: 2003-08-18 07:18:22 $
 */
public class PackageDescriptor
    implements Serializable
{
    /**
     * The name of the package.
     */
    private final String _name;

    /**
     * The arbitrary set of Attributes associated with Package.
     */
    private final Attribute[] _attributes;


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

        _name = name;
        _attributes = attributes;
    }

    /**
     * Return the name of the package.
     *
     * @return the name of the package.
     */
    public String getName()
    {
        return _name;
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
}
