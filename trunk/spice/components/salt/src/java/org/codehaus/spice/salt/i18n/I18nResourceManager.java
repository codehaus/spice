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
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Manager for I18nResources objects.
 * The manager caches the I18nResources object based on the ClassLoader
 * and Locale that the I18nResources is loaded from.
 *
 * @author Peter Donald
 */
public class I18nResourceManager
{
    /** Postfix appended to base names to get names of Resource files. */
    private static final String POSTFIX = "Resources";

    /** Permission needed to clear complete cache. */
    private static final RuntimePermission CLEAR_CACHE_PERMISSION = new RuntimePermission( "i18n.clearCompleteCache" );

    /**
     * This maps ClassLoader -> "Locale Map".
     * The "Locale Map" is a map that maps Locale -> "Resource Map"
     * The "Resource Map" is a map that maps baseName -> I18nResource
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
    public static final I18nResources getBaseResources( final String basename,
                                                        final ClassLoader classLoader )
    {
        return getBaseResources( basename, Locale.getDefault(), classLoader );
    }

    /**
     * Retrieve resource with specified basename in specified locale.
     *
     * @param basename the basename
     * @return the Resources
     */
    public static final I18nResources getBaseResources( final String basename, final Locale locale,
                                                        final ClassLoader classLoader )
    {
        I18nResources resources = getCachedResource( basename, locale, classLoader );
        if( null == resources )
        {
            resources = new I18nResources( basename, locale, classLoader );
            putCachedResource( basename, locale, classLoader, resources );
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
    public static final I18nResources getResources( final String resource,
                                                    final ClassLoader classLoader )
    {
        return getResources( resource, Locale.getDefault(), classLoader );
    }

    /**
     * Retrieve resource for specified name. The baseName is determined by name
     * postfixed with ".Resources".
     *
     * @param resource the base location
     * @return the Resources
     */
    public static final I18nResources getResources( final String resource, final Locale locale,
                                                    final ClassLoader classLoader )
    {
        return getBaseResources( resource + POSTFIX, locale, classLoader );
    }

    /**
     * Retrieve resource for specified Classes package. The baseName is
     * determined by name of classes package postfixed with ".Resources".
     *
     * @param clazz the Class
     * @return the Resources
     */
    public static final I18nResources getPackageResources( final Class clazz )
    {
        return getPackageResources( clazz, Locale.getDefault() );
    }

    /**
     * Retrieve resource for specified Classes package. The baseName is
     * determined by name of classes package postfixed with ".Resources".
     *
     * @param clazz the Class
     * @return the Resources
     */
    public static final I18nResources getPackageResources( final Class clazz, final Locale locale )
    {
        final String name = clazz.getName();
        final int index = name.lastIndexOf( "." );
        final String packageName;
        if( -1 != index )
        {
            packageName = name.substring( 0, index ) + "." + POSTFIX;
        }
        else
        {
            packageName = POSTFIX;
        }

        return getBaseResources( packageName, locale, clazz.getClassLoader() );
    }

    /**
     * Retrieve resource for specified Class. The baseName is determined by name
     * of Class postfixed with ".Resources".
     *
     * @param clazz the Class
     * @return the Resources
     */
    public static final I18nResources getClassResources( final Class clazz )
    {
        return getClassResources( clazz, Locale.getDefault() );
    }

    /**
     * Retrieve resource for specified Class. The baseName is determined by name
     * of Class postfixed with ".Resources".
     *
     * @param clazz the Class
     * @return the Resources
     */
    public static final I18nResources getClassResources( final Class clazz, final Locale locale )
    {
        final String resource = clazz.getName() + POSTFIX;
        return getBaseResources( resource, locale, clazz.getClassLoader() );
    }

    /**
     * Cache specified resource in weak reference.
     *
     * @param baseName the resource key
     * @param resources the resources object
     */
    private static final synchronized void putCachedResource( final String baseName,
                                                              final Locale locale,
                                                              final ClassLoader classLoader,
                                                              final I18nResources resources )
    {

        final Map map = getLocaleMap( classLoader, locale );
        map.put( baseName, new WeakReference( resources ) );
    }

    /**
     * Retrieve cached resource.
     *
     * @param baseName the resource key
     * @return resources the resources object
     */
    private static final synchronized I18nResources getCachedResource( final String baseName,
                                                                       final Locale locale,
                                                                       final ClassLoader classLoader )
    {
        final Map map = getLocaleMap( classLoader, locale );
        final WeakReference weakReference = (WeakReference) map.get( baseName );
        if( null == weakReference )
        {
            return null;
        }
        else
        {
            return (I18nResources) weakReference.get();
        }
    }

    /**
     * Return the map associated with specified ClassLoader.
     * Will create Map if it does not exist.
     *
     * @param classLoader the ClassLoader
     * @return the Map.
     */
    private static final Map getLocaleMap( final ClassLoader classLoader, final Locale locale )
    {
        final Map loaderMap = getClassLoaderMap( classLoader );
        Map map = (Map) loaderMap.get( locale );
        if( null == map )
        {
            map = new HashMap();
            loaderMap.put( locale, map );
        }
        return map;
    }

    /**
     * Return the map associated with specified ClassLoader.
     * Will create Map if it does not exist.
     *
     * @param classLoader the ClassLoader
     * @return the Map.
     */
    private static final Map getClassLoaderMap( final ClassLoader classLoader )
    {
        Map map = (Map) c_resources.get( classLoader );
        if( null == map )
        {
            map = new WeakHashMap();
            c_resources.put( classLoader, map );
        }
        return map;
    }
}
