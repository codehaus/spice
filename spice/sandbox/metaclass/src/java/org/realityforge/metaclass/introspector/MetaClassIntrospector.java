/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.introspector;

import org.realityforge.metaclass.model.ClassDescriptor;

/**
 * This class is responsible for loading and caching the {@link ClassDescriptor}
 * objects for corresponding java classes. It serves a similar purpose as the
 * {@link java.beans.Introspector} class does for Java Beans.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.4 $ $Date: 2003-10-28 07:01:01 $
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
     * The cache in which info objects are stored.
     * This cache stores maps for ClassLoaders which in
     * turn stores info for particular classes in
     * classloader.
     */
    private static final CachingMetaClassAccessor c_cachingAccessor = new CachingMetaClassAccessor();

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
        c_cachingAccessor.clear();
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
     * @throws SecurityException if the caller does not have
     *                           permission to clear cache
     */
    public static void setAccessor( final MetaClassAccessor accessor )
    {
        final SecurityManager sm = System.getSecurityManager();
        if( null != sm )
        {
            sm.checkPermission( SET_ACCESSOR_PERMISSION );
        }
        c_cachingAccessor.setAccessor( accessor );
    }

    /**
     * @see MetaClassAccessor#getClassDescriptor
     */
    public static ClassDescriptor getClassDescriptor( final Class clazz )
        throws MetaClassException
    {
        return getClassDescriptor( clazz.getName(), clazz.getClassLoader() );
    }

    /**
     * @see MetaClassAccessor#getClassDescriptor
     */
    public static ClassDescriptor getClassDescriptor( final String classname,
                                                      final ClassLoader classLoader )
        throws MetaClassException
    {
        return c_cachingAccessor.getClassDescriptor( classname, classLoader, null );
    }
}
