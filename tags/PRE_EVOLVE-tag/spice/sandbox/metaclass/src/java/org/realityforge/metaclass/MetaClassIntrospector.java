/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass;

import java.io.InputStream;
import java.io.IOException;
import java.util.WeakHashMap;

import org.realityforge.metaclass.io.MetaClassIO;
import org.realityforge.metaclass.io.MetaClassIOBinary;
import org.realityforge.metaclass.model.ClassDescriptor;

/**
 * This class is responsible for loading and caching the {@link ClassDescriptor}
 * objects for corresponding java classes. It serves a similar purpose as the
 * {@link java.beans.Introspector} class does for Java Beans.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-06-10 01:39:55 $
 */
public final class MetaClassIntrospector
{
    /**
     * Class used to read the MetaData.
     */
    private static final MetaClassIO c_metaClassIO = new MetaClassIOBinary();

    /**
     * The cache in which info objects are stored.
     */
    private static final WeakHashMap c_cache = new WeakHashMap();

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
     */
    public static void flushCaches()
    {
        c_cache.clear();
    }

    /**
     * Return a {@link ClassDescriptor} for specified class.
     *
     * @param clazz the class to {@link ClassDescriptor} for
     * @return the newly created {@link ClassDescriptor}
     * @throws InvalidMetaClassException if unable to create {@link ClassDescriptor}
     * @throws IOException if incorrect version or read/write error
     */
    public static ClassDescriptor getClassInfo( final Class clazz )
        throws InvalidMetaClassException, IOException
    {
        ClassDescriptor info = (ClassDescriptor)c_cache.get( clazz );
        if( null != info )
        {
            return info;
        }
        else
        {
            final String className = clazz.getName();
            info = createClassInfo( className,
                                    clazz.getClassLoader() );
            c_cache.put( className, info );
            return info;
        }
    }

    /**
     * Return a {@link ClassDescriptor} for specified class.
     *
     * @param className the className to get {@link ClassDescriptor} for
     * @param classLoader the classLoader to use
     * @return the newly created {@link ClassDescriptor}
     * @throws InvalidMetaClassException if unable to create {@link ClassDescriptor}
     * @throws IOException if unable to create {@link ClassDescriptor}
     */
    public static ClassDescriptor getClassInfo( final String className,
                                                final ClassLoader classLoader )
        throws InvalidMetaClassException, IOException
    {
        ClassDescriptor info = (ClassDescriptor)c_cache.get( className );
        if( null != info )
        {
            return info;
        }
        else
        {
            info = createClassInfo( className,
                                    classLoader );
            c_cache.put( className, info );
            return info;
        }
    }

    /**
     * Create a {@link ClassDescriptor} for specified class.
     *
     * @param classname the classname of the class
     * @param classLoader the classLoader to load {@link ClassDescriptor} from
     * @return the newly created {@link ClassDescriptor}
     * @throws InvalidMetaClassException if unable to create {@link ClassDescriptor}
     * @throws IOException if incorrect version or read/write error
     */
    private static ClassDescriptor createClassInfo( final String classname,
                                                    final ClassLoader classLoader )
        throws InvalidMetaClassException, IOException
    {
        final String resource = classname.replace( '.', '/' ) + ".mad";
        final InputStream inputStream = classLoader.getResourceAsStream( resource );
        if( null == inputStream )
        {
            final String message =
                "Unable to locate metadata for: " + classname;
            throw new InvalidMetaClassException( message );
        }

        return c_metaClassIO.deserialize( inputStream );
    }
}
