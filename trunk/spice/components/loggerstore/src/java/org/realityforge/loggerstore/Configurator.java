/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.loggerstore;

import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationUtil;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.avalon.framework.container.ContainerUtil;
import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.context.DefaultContext;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.logger.Logger;
import org.w3c.dom.Element;
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
     * @param loggerType the type of the Logger to use. 
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
            final String message = "Failed to created LoggerStoreFactory for " + type;
            throw new IllegalArgumentException( message );
        }
    }

     
    /**
     *  Parses XML InputStream to build a Configuration object
     *  @param resource the path of the configuration resource
     */
    public static Configuration buildConfiguration( final String resource )
        throws Exception
    {
        return buildConfiguration( new FileInputStream( resource ) );
    }
    
    /**
     *  Parses XML InputStream to build a Configuration object
     *  @param resource the InputStream of the configuration resource
     */
    public static Configuration buildConfiguration( final InputStream resource )
        throws Exception
    {
        DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
        return builder.build( resource );
    }
    
    /**
     *  Parses XML InputStream to build an Element object
     *  @param resource the path of the configuration resource
     */
    public static Element buildElement( final String resource )
        throws Exception
    {
        return buildElement( new FileInputStream( resource ) );
    }
    
    /**
     *  Parses XML InputStream to build an Element object
     *  @param resource the InputStream of the configuration resource
     */
    public static Element buildElement( final InputStream resource )
        throws Exception
    {
        return ConfigurationUtil.toElement( buildConfiguration( resource ) );
    }
    
    /**
     *  Parses Properties InputStream to build a Properties object
     *  @param resource the path of the configuration resource
     */
    public static Properties buildProperties( final String resource )
        throws Exception
    {
        return buildProperties(  new FileInputStream( resource ) );
    }
    
    /**
     *  Parses Properties InputStream to build a Properties object
     *  @param resource the InputStream of the configuration resource
     */
    public static Properties buildProperties( final InputStream resource )
        throws Exception
    {
        Properties properties = new Properties();
        properties.load( resource );
        return properties;
    }

    /**
     * Get the Factory class name of the LoggerStoreFactory that corresponds
     * to specified type of Logger.
     *
     * @param type the type of Logger
     */
    static String getFactoryClassName( final String type )
    {
        if( type.equals( LoggerStoreFactory.LOGKIT ) )
        {
            return LogKitLoggerStoreFactory.class.getName();
        }
        else if( type.equals( LoggerStoreFactory.LOG4J ) )
        {
            return Log4JLoggerStoreFactory.class.getName();
        }
        else if( type.equals( LoggerStoreFactory.JDK14 ) )
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
    static Map buildConfigurationMap( final String configurationType,
                                      final InputStream resource )
        throws Exception
    {
        Map map = new HashMap();
        map.put( LoggerStoreFactory.CONFIGURATION, resource );
        map.put( LoggerStoreFactory.CONFIGURATION_TYPE, configurationType );
        return map;
    }

}
