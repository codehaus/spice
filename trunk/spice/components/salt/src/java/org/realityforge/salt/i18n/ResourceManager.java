/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.salt.i18n;

import java.util.HashMap;

/**
 * Manager for resources.
 *
 * @author <a href="mailto:peter at apache.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-05-28 12:26:53 $
 */
public class ResourceManager
{
    private final static HashMap  m_resources   = new HashMap();

    /**
     * Retrieve resource with specified basename.
     *
     * @param baseName the basename
     * @return the Resources
     */
    public final static Resources getBaseResources( final String baseName )
    {
        //TODO: Make these weak references????
        Resources packet = (Resources)m_resources.get( baseName );

        if( null == packet )
        {
            packet = new Resources( baseName );
            m_resources.put( baseName, packet );
        }

        return packet;
    }

    /**
     * Retrieve resource for specified name.
     * The baseName is determined by name postfixed with ".Resources".
     *
     * @param resource the base location
     * @return the Resources
     */
    public final static Resources getResources( final String resource )
    {
        return getBaseResources( resource + ".Resources" );
    }

    /**
     * Retrieve resource for specified Classes package.
     * The baseName is determined by name of classes package
     * postfixed with ".Resources".
     *
     * @param clazz the Class
     * @return the Resources
     */
    public final static Resources getPackageResources( final Class clazz )
    {
        final String resource = clazz.getPackage().getName() + ".Resources";
        return getBaseResources( resource );
    }

    /**
     * Retrieve resource for specified Class.
     * The baseName is determined by name of Class
     * postfixed with ".Resources".
     *
     * @param clazz the Class
     * @return the Resources
     */
    public final static Resources getClassResources( final Class clazz )
    {
        final String resource = clazz.getName() + ".Resources";
        return getBaseResources( resource );
    }
}
