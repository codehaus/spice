/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.salt.i18n;

import java.util.HashMap;
import java.util.Map;

/**
 * Manager for resources.
 *
 * @author <a href="mailto:peter at apache.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-05-28 12:29:59 $
 */
public class ResourceManager
{
    /**
     * Permission needed to clear complete cache.
     */
    private static final RuntimePermission CLEAR_CACHE_PERMISSION =
        new RuntimePermission( "i18n.clearCompleteCache" );

    /**
     * Map of names onto Resources.
     */
    private final static Map c_resources = new HashMap();

    /**
     * Clear the cache of all resources currently loaded into the
     * system. This method is useful if you need to dump the complete
     * cache and because part of the application is reloading and
     * thus the resources may need to be reloaded.
     *
     * <p>Note that the caller must have been granted the
     * "i18n.clearCompleteCache" {@link RuntimePermission} or
     * else a security exception will be thrown.</p>
     *
     * @throws SecurityException if the caller does not have
     *                           permission to clear cache
     */
    public synchronized static final void clearResourceCache()
        throws SecurityException
    {
        final SecurityManager sm = System.getSecurityManager();
        if( null != sm )
        {
            sm.checkPermission( CLEAR_CACHE_PERMISSION );
        }

        c_resources.clear();
    }

    /**
     * Retrieve resource with specified basename.
     *
     * @param baseName the basename
     * @return the Resources
     */
    public final static Resources getBaseResources( final String baseName )
    {
        //TODO: Make these weak references????
        Resources packet = (Resources)c_resources.get( baseName );

        if( null == packet )
        {
            packet = new Resources( baseName );
            c_resources.put( baseName, packet );
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
