/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass;

import java.util.Map;
import java.util.WeakHashMap;
import org.realityforge.metaclass.io.DefaultMetaClassAccessor;
import org.realityforge.metaclass.io.MetaClassAccessor;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.model.PackageDescriptor;

/**
 * This class is responsible for loading and caching the {@link ClassDescriptor}
 * objects for corresponding java classes. It serves a similar purpose as the
 * {@link java.beans.Introspector} class does for Java Beans.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.9 $ $Date: 2003-08-23 04:46:37 $
 */
public final class MetaClassIntrospector
{
    /**
     * Permission needed to clear complete cache.
     */
    private static final RuntimePermission CLEAR_CACHE_PERMISSION =
        new RuntimePermission( "metaclass.clearCompleteCache" );

    /**
     * Permission needed to set the accessor.
     */
    private static final RuntimePermission SET_ACCESSOR_PERMISSION =
        new RuntimePermission( "metaclass.setAccessor" );

    /**
     * Class used to access the MetaData.
     */
    private static MetaClassAccessor c_accessor = new DefaultMetaClassAccessor();

    /**
     * The cache in which info objects are stored.
     * This cache stores maps for ClassLoaders which in
     * turn stores info for particular classes in
     * classloader.
     */
    private static final Map c_cache = new WeakHashMap();

    /**
     * Private constructor so that class can not be instantiated.
     */
    private MetaClassIntrospector()
    {
    }

    /**
     * Flush all of the Introspector's internal caches.  This method is
     * not normally required.  It is normally only needed by advanced
     * tools that update existing "Class" objects in-place and need
     * to make the Introspector re-analyze existing Class objects.
     *
     * <p>Note that the caller must have been granted the
     * "metaclass.clearCompleteCache" {@link RuntimePermission} or
     * else a security exception will be thrown.</p>
     *
     * @throws SecurityException if the caller does not have
     *                           permission to clear cache
     */
    public synchronized static void clearCompleteCache()
    {
        final SecurityManager sm = System.getSecurityManager();
        if( null != sm )
        {
            sm.checkPermission( CLEAR_CACHE_PERMISSION );
        }
        c_cache.clear();
    }

    /**
     * Set the MetaClassAccessor to use to locate
     * ClassDescriptor objects.
     *
     * <p>Note that the caller must have been granted the
     * "metaclass.setAccessor" {@link RuntimePermission} or
     * else a security exception will be thrown.</p>
     *
     * @param accessor the MetaClassAccessor
     * @throws SecurityException if the caller does not have
     *                           permission to clear cache
     */
    public static void setAccessor( final MetaClassAccessor accessor )
    {
        if( null == accessor )
        {
            throw new NullPointerException( "accessor" );
        }
        final SecurityManager sm = System.getSecurityManager();
        if( null != sm )
        {
            sm.checkPermission( SET_ACCESSOR_PERMISSION );
        }
        c_accessor = accessor;
    }

    /**
     * Return a {@link PackageDescriptor} for package that specified class is in.
     *
     * @param clazz the class that package derived from
     * @return the newly created {@link PackageDescriptor}
     * @throws MetaClassException if unable to create {@link PackageDescriptor}
     */
    public static PackageDescriptor getPackageDescriptor( final Class clazz )
        throws MetaClassException
    {
        final String name = clazz.getName();
        final int index = name.lastIndexOf( "." );
        final String packageName;
        if( -1 == index )
        {
            packageName = "";
        }
        else
        {
            packageName = name.substring( 0, index );
        }
        return getPackageDescriptor( packageName, clazz.getClassLoader() );
    }

    /**
     * Return a {@link PackageDescriptor} for specified package.
     *
     * @param packageName the name of the package
     * @param classLoader the classLoader to use
     * @return the newly created {@link PackageDescriptor}
     * @throws MetaClassException if unable to create {@link PackageDescriptor}
     */
    public static PackageDescriptor getPackageDescriptor( final String packageName,
                                                          final ClassLoader classLoader )
        throws MetaClassException
    {
        final Map cache = getClassLoaderCache( classLoader );
        PackageDescriptor info = (PackageDescriptor)cache.get( packageName );
        if( null != info )
        {
            return info;
        }
        else
        {
            info = c_accessor.getPackageDescriptor( packageName, classLoader );
            cache.put( packageName, info );
            return info;
        }
    }

    /**
     * Return a {@link ClassDescriptor} for specified class.
     *
     * @param clazz the class to {@link ClassDescriptor} for
     * @return the newly created {@link ClassDescriptor}
     * @throws MetaClassException if unable to create {@link ClassDescriptor}
     */
    public static ClassDescriptor getClassDescriptor( final Class clazz )
        throws MetaClassException
    {
        return getClassDescriptor( clazz.getName(), clazz.getClassLoader() );
    }

    /**
     * Return a {@link ClassDescriptor} for specified class.
     *
     * @param classname the classname to get {@link ClassDescriptor} for
     * @param classLoader the classLoader to use
     * @return the newly created {@link ClassDescriptor}
     * @throws MetaClassException if unable to create {@link ClassDescriptor}
     */
    public static ClassDescriptor getClassDescriptor( final String classname,
                                                      final ClassLoader classLoader )
        throws MetaClassException
    {
        final Map cache = getClassLoaderCache( classLoader );
        ClassDescriptor info = (ClassDescriptor)cache.get( classname );
        if( null != info )
        {
            return info;
        }
        else
        {
            info = c_accessor.getClassDescriptor( classname, classLoader );
            cache.put( classname, info );
            return info;
        }
    }

    /**
     * Get Cache for specified ClassLoader.
     *
     * @param classLoader the ClassLoader to get cache for
     * @return the Map/Cache for ClassLoader
     */
    private synchronized static Map getClassLoaderCache( final ClassLoader classLoader )
    {
        Map map = (Map)c_cache.get( classLoader );
        if( null == map )
        {
            map = new WeakHashMap();
            c_cache.put( classLoader, map );
        }
        return map;
    }
}
