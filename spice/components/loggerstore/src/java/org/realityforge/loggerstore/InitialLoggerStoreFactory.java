/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.loggerstore;

import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.Properties;
import java.net.URL;
import java.io.InputStream;

/**
 * This is the initial LoggerStoreFactory tyhat the user accesses
 * to create their LoggerStore when the type is configurable.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @version $Revision: 1.1 $ $Date: 2003-05-24 22:08:19 $
 */
public class InitialLoggerStoreFactory
    implements LoggerStoreFactory
{
    /**
     * The INITIAL_FACTORY key.  Used to define the classname of the
     * initial LoggerStoreFactory. If not specified will attempt to use
     * the ConsoleLoggerStoreFactory.
     */
    public static final String INITIAL_FACTORY = LoggerStoreFactory.class.getName() + ".factory";

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

    private ClassLoader getClassLoader( final Map data )
    {
        ClassLoader loader = (ClassLoader)data.get( ClassLoader.class.getName() );
        if( null == loader )
        {
            loader = Thread.currentThread().getContextClassLoader();
            if( null == loader )
            {
                loader = Configurator.class.getClassLoader();
            }
        }
        return loader;
    }

    private Map loadDefaultConfig( final Map initial,
                                   final ClassLoader classLoader )
        throws Exception
    {
        final HashMap map = new HashMap();

        final Enumeration resources =
            classLoader.getResources( "META-INF/spice/loggerstore.properties" );
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
    private static LoggerStoreFactory createLoggerStoreFactory( final String type,
                                                                final ClassLoader classLoader )
    {
        String classname = type;
        if( null == classname )
        {
            classname = ConsoleLoggerStore.class.getName();
        }

        try
        {
            final Class clazz = classLoader.loadClass( classname );
            return (LoggerStoreFactory)clazz.newInstance();
        }
        catch( final Exception e )
        {
            final String message =
                "Failed to created LoggerStoreFactory " + classname;
            throw new IllegalArgumentException( message );
        }
    }
}
