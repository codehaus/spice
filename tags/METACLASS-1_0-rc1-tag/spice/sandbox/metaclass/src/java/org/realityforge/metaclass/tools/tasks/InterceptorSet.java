/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

/**
 * An Ant type representing a set of Interceptor definitions.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-11-20 06:42:49 $
 */
public class InterceptorSet
    extends PluginSet
{
    /**
     * Add an interceptor definition that will create interceptor to process metadata.
     *
     * @param element the interceptor definition
     */
    public void addInterceptor( final PluginElement element )
    {
        addPlugin( "Interceptor", element );
    }

    /**
     * Add a set of Interceptors.
     *
     * @param set the interceptor set
     */
    public void addInterceptorSet( final InterceptorSet set )
    {
        addPluginSet( set );
    }
}
