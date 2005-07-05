/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
/*
 * This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/).
 */
package org.codehaus.spice.jervlet.tools.isolate;

import java.util.Properties;

/**
 * This is a utility class making it easy to install redirecting
 * Properties objects for the underlying System.getProeprties()
 * object. The properties object will redirect to the properties
 * object that is associated with the current thread. A properties
 * object becomes associated with the current thread when a user
 * calls one of the bind*() methods or by inheriting the parent
 * threads properties object when thread is initially created. The
 * default streams will redirect to the default System properties.
 *
 * @author Peter Donald
 * @author Johan Sjoberg
 */
public final class SysPropertiesRedirector
{
    private static final String NOT_INSTALLED_ERROR =
      "SysPropertiesRedirector not installed";

    /**
     * Cached version of original System Properties.
     * (Or at least the properties that were installed in JVM
     * when this class is loaded and resovled).
     */
    private static final Properties c_systemProperties =
      System.getProperties();

    private static DemuxProperties c_properties;

    /**
     * Private constructor to block instantiation.
     */
    private SysPropertiesRedirector()
    {
    }

    /**
     * Install the redirecting Properties object.
     *
     * @return true if installed, false if already installed
     */
    public synchronized static boolean install()
    {
        if( c_properties == System.getProperties() )
        {
            //Already installed so lets ignore
            return false;
        }
        c_properties = new DemuxProperties( c_systemProperties );
        System.setProperties( c_properties );
        return true;
    }

    /**
     * Uninstall the redirecting streams and replace them with
     * streams that point to FileDescriptor.in, FileDescriptor.out
     * and FileDescriptor.err.
     *
     * @return true if uninstalled, false if already uninstalled
     */
    public synchronized static boolean uninstall()
    {
        if( c_properties != System.getProperties() )
        {
            //Already uninstalled so lets ignore
            return false;
        }
        c_properties = null;
        System.setProperties( c_systemProperties );
        return true;
    }

    /**
     * Bind the specified Properties object to this thread.
     *
     * @param properties the stream to bind
     * @throws NullPointerException if the given Properties object was null
     * @throws SecurityException if don't have permission to bind to input
     * @throws IllegalStateException if redirector not installed
     */
    public static Properties bindProperties( final Properties properties )
        throws SecurityException, IllegalStateException
    {
        if( null == properties )
        {
            throw new NullPointerException( "properties" );
        }
        if( null == c_properties )
        {
            throw new IllegalStateException( NOT_INSTALLED_ERROR );
        }
        final SecurityManager manager = System.getSecurityManager();
        if( null != manager )
        {
            final RuntimePermission permission =
                new RuntimePermission( "SysPropertiesRedirector.setProperties" );
            manager.checkPermission( permission );
        }
        return c_properties.bindProperties( properties );
    }
}
