/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.introspector;

import java.util.Map;
import java.util.WeakHashMap;
import org.realityforge.metaclass.model.ClassDescriptor;

/**
 * This class is responsible for loading and caching the {@link org.realityforge.metaclass.model.ClassDescriptor}
 * objects for corresponding java classes. It serves a similar purpose as the
 * {@link java.beans.Introspector} class does for Java Beans.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-04 00:47:48 $
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
     * Flush all of the Introspector's internal caches.  This method is
     * not normally required.  It is normally only needed by advanced
     * tools that update existing "Class" objects in-place and need
     * to make the Introspector re-analyze existing Class objects.
     *
     * <p>Note that the caller must have been granted the
     * "metaclass.clearCompleteCache" {@link java.lang.RuntimePermission} or
     * else a security exception will be thrown.</p>
     *
     * @throws java.lang.SecurityException if the caller does not have
     *                           permission to clear cache
     */
    public static synchronized void clearCompleteCache()
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
     * "metaclass.setAccessor" {@link java.lang.RuntimePermission} or
     * else a security exception will be thrown.</p>
     *
     * @param accessor the MetaClassAccessor
     * @throws java.lang.SecurityException if the caller does not have
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
     * Return a {@link org.realityforge.metaclass.model.ClassDescriptor} for specified class.
     *
     * @param clazz the class to {@link org.realityforge.metaclass.model.ClassDescriptor} for
     * @return the newly created {@link org.realityforge.metaclass.model.ClassDescriptor}
     * @throws org.realityforge.metaclass.introspector.MetaClassException if unable to create {@link org.realityforge.metaclass.model.ClassDescriptor}
     */
    public static ClassDescriptor getClassDescriptor( final Class clazz )
        throws MetaClassException
    {
        return getClassDescriptor( clazz.getName(), clazz.getClassLoader() );
    }

    /**
     * Return a {@link org.realityforge.metaclass.model.ClassDescriptor} for specified class.
     *
     * @param classname the classname to get {@link org.realityforge.metaclass.model.ClassDescriptor} for
     * @param classLoader the classLoader to use
     * @return the newly created {@link org.realityforge.metaclass.model.ClassDescriptor}
     * @throws org.realityforge.metaclass.introspector.MetaClassException if unable to create {@link org.realityforge.metaclass.model.ClassDescriptor}
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
    private static synchronized Map getClassLoaderCache( final ClassLoader classLoader )
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
