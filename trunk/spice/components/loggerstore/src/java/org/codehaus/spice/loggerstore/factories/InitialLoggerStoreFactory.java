/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.loggerstore.factories;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.codehaus.spice.loggerstore.LoggerStore;
import org.codehaus.spice.loggerstore.LoggerStoreFactory;

/**
 * This is the initial LoggerStoreFactory tyhat the user accesses
 * to create their LoggerStore when the type is configurable.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @version $Revision: 1.1 $ $Date: 2003-11-19 18:22:44 $
 */
public class InitialLoggerStoreFactory
    implements LoggerStoreFactory
{
    /**
     * The INITIAL_FACTORY key.  Used to define the classname of the
     * initial LoggerStoreFactory. If not specified will attempt to use
     * the ConsoleLoggerStoreFactory.
     */
    public static final String INITIAL_FACTORY = "org.codehaus.spice.loggerstore.factory";

    /**
     * The name of properties file loaded from ClassLoader. This property
     * file will be used to load default configuration settings if user failed
     * to specify them.
     */
    public static final String DEFAULT_PROPERTIES = "META-INF/spice/loggerstore.properties";

    /**
     * Create LoggerStore by first determining the correct LoggerStoreFactory
     * to use and then delegating to that factory. See Class Javadocs for the
     * process of locating LoggerStore.
     *
     * @param config the input configuration
     * @return the LoggerStore
     * @throws Exception if unable to create the LoggerStore for any reason.
     */
    public LoggerStore createLoggerStore( final Map config )
        throws Exception
    {
        final ClassLoader classLoader = getClassLoader( config );

        String type = (String)config.get( INITIAL_FACTORY );
        Map data = config;
        if( null == type )
        {
            data = loadDefaultConfig( data, classLoader );
            type = (String)data.get( INITIAL_FACTORY );
        }
        final LoggerStoreFactory factory =
            createLoggerStoreFactory( type, classLoader );
        return factory.createLoggerStore( data );
    }

    /**
     * Retrieve the classloader from data map. If no classloader is specified
     * then use ContextClassLoader. If ContextClassLoader not specified then
     * use ClassLoader that loaded this class.
     *
     * @param data the configuration data
     * @return a ClassLoader
     */
    private ClassLoader getClassLoader( final Map data )
    {
        ClassLoader loader = (ClassLoader)data.get( ClassLoader.class.getName() );
        if( null == loader )
        {
            loader = Thread.currentThread().getContextClassLoader();
            if( null == loader )
            {
                loader = InitialLoggerStoreFactory.class.getClassLoader();
            }
        }
        return loader;
    }

    /**
     * Load the default properties for LoggerStoreFactory.
     *
     * @param initial the input data
     * @param classLoader the classLoader to load properties files from
     * @return the new configuration data
     * @throws Exception if unable to load properties
     */
    private Map loadDefaultConfig( final Map initial,
                                   final ClassLoader classLoader )
        throws Exception
    {
        final HashMap map = new HashMap();

        final Enumeration resources =
            classLoader.getResources( DEFAULT_PROPERTIES );
        while( resources.hasMoreElements() )
        {
            final URL url = (URL)resources.nextElement();
            final InputStream stream = url.openStream();
            final Properties properties = new Properties();
            properties.load( stream );
            map.putAll( properties );
        }

        map.putAll( initial );
        return map;
    }

    /**
     * Create a {@link LoggerStoreFactory} for specified loggerType.
     *
     * @param type the type of the Logger to use.
     * @return the created {@link LoggerStoreFactory}
     */
    private LoggerStoreFactory createLoggerStoreFactory( final String type,
                                                         final ClassLoader classLoader )
    {
        if( null == type )
        {
            final String message = "No LoggerStoreFactory type specified.";
            throw new IllegalStateException( message );
        }

        try
        {
            final Class clazz = classLoader.loadClass( type );
            return (LoggerStoreFactory)clazz.newInstance();
        }
        catch( final Exception e )
        {
            final String message =
                "Failed to created LoggerStoreFactory " + type;
            throw new IllegalArgumentException( message );
        }
    }
}
