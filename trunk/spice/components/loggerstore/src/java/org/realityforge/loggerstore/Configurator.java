/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.loggerstore;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Configurator is a collection of utility methods to create and configure
 * LoggerStore objects of different types using configuration resources.
 * LogKit, Log4J and JDK14 Loggers are supported
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class Configurator
{
    /**
     * Constant used to define LogKit Logger type
     */
    public static final String LOGKIT = "logkit";

    /**
     * Constant used to define Log4J Logger type
     */
    public static final String LOG4J = "log4j";

    /**
     * Constant used to define JDK14 Logger type
     */
    public static final String JDK14 = "jdk14";

    /**
     * Create and configure a {@link LoggerStore} from a specified
     * configuration file.
     *
     * @param loggerType the type of the Logger to use.
     * @param configurationType the type of the configuration
     * @param resource the String encoding the path of the configuration resource
     * @return the configured LoggerStore
     * @throws Exception if unable to create the LoggerStore
     */
    public static LoggerStore createLoggerStore( final String loggerType,
                                                 final String configurationType,
                                                 final String resource )
        throws Exception
    {
        return createLoggerStore( loggerType, configurationType,
                                  new FileInputStream( resource ) );
    }

    /**
     * Create and configure a {@link LoggerStore} from a specified
     * configuration resource.
     *
     * @param loggerType the type of the Logger to use.
     * @param configurationType the type of the configuration
     * @param resource the InputStream of the configuration resource
     * @return the configured LoggerStore
     * @throws Exception if unable to create the LoggerStore
     */
    public static LoggerStore createLoggerStore( final String loggerType,
                                                 final String configurationType,
                                                 final InputStream resource )
        throws Exception
    {
        final LoggerStoreFactory factory = createLoggerStoreFactory( loggerType );
        final Map config = buildConfigurationMap( configurationType, resource );
        return factory.createLoggerStore( config );
    }

    /**
     * Create a {@link LoggerStoreFactory} for specified loggerType.
     *
     * @param type the type of the Logger to use.
     * @return the created {@link LoggerStoreFactory}
     */
    public static LoggerStoreFactory createLoggerStoreFactory( final String type )
    {
        final String className = getFactoryClassName( type );
        try
        {
            final ClassLoader classLoader = Configurator.class.getClassLoader();
            final Class clazz = classLoader.loadClass( className );
            return (LoggerStoreFactory)clazz.newInstance();
        }
        catch( final Exception e )
        {
            final String message =
                "Failed to created LoggerStoreFactory for " + type;
            throw new IllegalArgumentException( message );
        }
    }

    /**
     * Get the Factory class name of the LoggerStoreFactory that corresponds
     * to specified type of Logger.
     *
     * @param type the type of Logger
     */
    private static String getFactoryClassName( final String type )
    {
        if( type.equals( LOGKIT ) )
        {
            return LogKitLoggerStoreFactory.class.getName();
        }
        else if( type.equals( LOG4J ) )
        {
            return Log4JLoggerStoreFactory.class.getName();
        }
        else if( type.equals( JDK14 ) )
        {
            return Jdk14LoggerStoreFactory.class.getName();
        }
        else
        {
            final String message = "Unknown type " + type;
            throw new IllegalArgumentException( message );
        }
    }

    /**
     *  Builds a configuration Map for factory
     *  @param configurationType the type of the configuration
     *  @param resource the InputStream of the configuration resource
     */
    private static Map buildConfigurationMap( final String configurationType,
                                              final InputStream resource )
        throws Exception
    {
        Map map = new HashMap();
        map.put( LoggerStoreFactory.CONFIGURATION, resource );
        map.put( LoggerStoreFactory.CONFIGURATION_TYPE, configurationType );
        return map;
    }
}
