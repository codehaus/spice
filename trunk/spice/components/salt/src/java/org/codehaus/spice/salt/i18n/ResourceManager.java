/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.salt.i18n;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Manager for resources.
 *
 * @author Peter Donald
 * @author Eung-ju Park
 */
public class ResourceManager
{
    /**
     * Postfix appended to base names to get names of Resource files.
     */
    private static final String POSTFIX = "Resources";

    /** Permission needed to clear complete cache. */
    private static final RuntimePermission CLEAR_CACHE_PERMISSION = new RuntimePermission( "i18n.clearCompleteCache" );

    /**
     * Map of ClassLoaders onto Secondary Map. Secondary map will map basename
     * to a Resources object.
     */
    private static final Map c_resources = new WeakHashMap();

    /**
     * Clear the cache of all resources currently loaded into the system. This
     * method is useful if you need to dump the complete cache and because part
     * of the application is reloading and thus the resources may need to be
     * reloaded.
     *
     * <p>Note that the caller must have been granted the
     * "i18n.clearCompleteCache" {@link RuntimePermission} or else a security
     * exception will be thrown.</p>
     *
     * @throws SecurityException if the caller does not have permission to clear
     * cache
     */
    public static final synchronized void clearResourceCache()
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
     * @param basename the basename
     * @return the Resources
     */
    public static final Resources getBaseResources( final String basename,
                                                    final ClassLoader classLoader )
    {
        Resources resources = getCachedResource( basename, classLoader );
        if( null == resources )
        {
            resources = new Resources( basename, classLoader );
            putCachedResource( basename, classLoader, resources );
        }

        return resources;
    }

    /**
     * Retrieve resource for specified name. The baseName is determined by name
     * postfixed with ".Resources".
     *
     * @param resource the base location
     * @return the Resources
     */
    public static final Resources getResources( final String resource,
                                                final ClassLoader classLoader )
    {
        return getBaseResources( resource + POSTFIX, classLoader );
    }

    /**
     * Retrieve resource for specified Classes package. The baseName is
     * determined by name of classes package postfixed with ".Resources".
     *
     * @param clazz the Class
     * @return the Resources
     */
    public static final Resources getPackageResources( final Class clazz )
    {
        final String name = clazz.getName();
        final int index = name.lastIndexOf( "." );
        String pkgName;
        if( -1 != index )
        {
            pkgName = name.substring( 0, index ) + "." + POSTFIX;
        }
        else
        {
            pkgName = POSTFIX;
        }

        return getBaseResources( pkgName, clazz.getClassLoader() );
    }

    /**
     * Retrieve resource for specified Class. The baseName is determined by name
     * of Class postfixed with ".Resources".
     *
     * @param clazz the Class
     * @return the Resources
     */
    public static final Resources getClassResources( final Class clazz )
    {
        final String resource = clazz.getName() + POSTFIX;
        return getBaseResources( resource, clazz.getClassLoader() );
    }

    /**
     * Cache specified resource in weak reference.
     *
     * @param baseName the resource key
     * @param resources the resources object
     */
    private static final synchronized void putCachedResource( final String baseName,
                                                              final ClassLoader classLoader,
                                                              final Resources resources )
    {
        Map map = (Map) c_resources.get( classLoader );
        if( null == map )
        {
            map = new HashMap();
            c_resources.put( classLoader, map );
        }
        map.put( baseName, new WeakReference( resources ) );
    }

    /**
     * Retrieve cached resource.
     *
     * @param baseName the resource key
     * @return resources the resources object
     */
    private static final synchronized Resources getCachedResource( final String baseName,
                                                                   final ClassLoader classLoader )
    {
        Map map = (Map) c_resources.get( classLoader );
        if( null == map )
        {
            return null;
        }
        final WeakReference weakReference = (WeakReference) map.get( baseName );
        if( null == weakReference )
        {
            return null;
        }
        else
        {
            return (Resources) weakReference.get();
        }
    }
}
