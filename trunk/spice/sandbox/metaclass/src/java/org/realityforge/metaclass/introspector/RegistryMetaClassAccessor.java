/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.introspector;

import org.realityforge.metaclass.introspector.MetaClassAccessor;
import org.realityforge.metaclass.introspector.MetaClassException;
import org.realityforge.metaclass.model.ClassDescriptor;
import java.util.Map;
import java.util.HashMap;
import java.util.WeakHashMap;

/**
 * This Accessor implementation will only return ClassDescriptor
 * objects that have been explicitly registered with accessor.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-10-26 11:35:53 $
 */
public class RegistryMetaClassAccessor
    implements MetaClassAccessor
{
    /**
     * Permission needed to register descriptor.
     */
    private static final RuntimePermission REGISTER_DESCRIPTOR_PERMISSION =
        new RuntimePermission( "metaclass.registry.registerDescriptor" );

    /**
     * The registry containing ClassLoader --> DataMap mapping.
     * Where the DataMap contains a classname --> ClassDescriptor
     * mapping.
     */
    private final Map m_registry = new WeakHashMap();

    /**
     * @see MetaClassAccessor#getClassDescriptor
     */
    public synchronized ClassDescriptor getClassDescriptor( final String classname,
                                                            final ClassLoader classLoader )
        throws MetaClassException
    {
        final Map map = getClassLoaderMap( classLoader );
        final ClassDescriptor descriptor = (ClassDescriptor)map.get( classname );
        if( null == descriptor )
        {
            final String message = "Missing descriptor for " + classname;
            throw new MetaClassException( message );
        }
        return descriptor;
    }

    /**
     * Register a ClassDescriptor associated with specified classLoader.
     *
     * @param descriptor the descriptor
     * @param classLoader the classloader
     */
    public synchronized void registerDescriptor( final ClassDescriptor descriptor,
                                                 final ClassLoader classLoader )
    {
        if( null == descriptor )
        {
            throw new NullPointerException( "descriptor" );
        }
        if( null == classLoader )
        {
            throw new NullPointerException( "classLoader" );
        }

        final SecurityManager sm = System.getSecurityManager();
        if( null != sm )
        {
            sm.checkPermission( REGISTER_DESCRIPTOR_PERMISSION );
        }
        final Map map = getClassLoaderMap( classLoader );
        map.put( descriptor.getName(), descriptor );
    }

    /**
     * Get DataMap for specified classLoader.
     *
     * @param classLoader the ClassLoader
     * @return the DataMap
     */
    protected Map getClassLoaderMap( final ClassLoader classLoader )
    {
        Map map = (Map)m_registry.get( classLoader );
        if( null == map )
        {
            map = new HashMap();
            m_registry.put( classLoader, map );
        }
        return map;
    }
}
