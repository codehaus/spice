/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.loggerstore;

import java.io.InputStream;
import java.util.HashMap;
import org.codehaus.spice.loggerstore.factories.DOMLog4JLoggerStoreFactory;
import org.codehaus.spice.loggerstore.factories.ExcaliburLogKitLoggerStoreFactory;
import org.codehaus.spice.loggerstore.factories.InitialLoggerStoreFactory;
import org.codehaus.spice.loggerstore.factories.Jdk14LoggerStoreFactory;
import org.codehaus.spice.loggerstore.factories.PropertyLog4JLoggerStoreFactory;
import org.codehaus.spice.loggerstore.factories.SimpleLogKitLoggerStoreFactory;

/**
 * Configurator is a collection of utility methods to create and configure
 * LoggerStore objects of different types using configuration resources. LogKit,
 * Log4J and JDK14 Loggers are supported. In the case of Log4J, both DOM and
 * Property configuration types are supported.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class Configurator
{
    /** Constant used to define Log4J type with DOMConfigurator */
    public static final String LOG4J_DOM = "log4j-dom";

    /** Constant used to define Log4J type with PropertyConfigurator */
    public static final String LOG4J_PROPERTY = "log4j-property";

    /** Constant used to define LogKit type with Excalibur configuration */
    public static final String LOGKIT_EXCALIBUR = "logkit-excalibur";

    /** Constant used to define LogKit type with Simple configuration */
    public static final String LOGKIT_SIMPLE = "logkit-simple";

    /** Constant used to define JDK14 type */
    public static final String JDK14 = "jdk14";

    /**
     * Create and configure a {@link LoggerStore} from a specified configuration
     * resource.
     *
     * @param configuratorType the type of the configurator
     * @param resource the String encoding the path of the configuration
     * resource
     * @return the configured LoggerStore
     * @throws Exception if unable to create the LoggerStore
     */
    public static LoggerStore createLoggerStore( final String configuratorType,
                                                 final String resource )
        throws Exception
    {
        final InitialLoggerStoreFactory factory = new InitialLoggerStoreFactory();
        final HashMap data = new HashMap();
        data.put( InitialLoggerStoreFactory.INITIAL_FACTORY,
                  getFactoryClassName( configuratorType ) );
        data.put( LoggerStoreFactory.FILE_LOCATION, resource );
        return factory.createLoggerStore( data );
    }

    /**
     * Create and configure a {@link LoggerStore} from a specified configuration
     * resource.
     *
     * @param configuratorType the type of the configurator
     * @param resource the InputStream of the configuration resource
     * @return the configured LoggerStore
     * @throws Exception if unable to create the LoggerStore
     */
    public static LoggerStore createLoggerStore( final String configuratorType,
                                                 final InputStream resource )
        throws Exception
    {
        final InitialLoggerStoreFactory factory = new InitialLoggerStoreFactory();
        final HashMap data = new HashMap();
        data.put( InitialLoggerStoreFactory.INITIAL_FACTORY,
                  getFactoryClassName( configuratorType ) );
        data.put( InputStream.class.getName(), resource );
        return factory.createLoggerStore( data );
    }

    /**
     * Get the Factory class name of the LoggerStoreFactory that corresponds to
     * specified type of Logger.
     *
     * @param type the type of Configurator
     */
    private static String getFactoryClassName( final String type )
    {
        if( LOG4J_DOM.equals( type ) )
        {
            return DOMLog4JLoggerStoreFactory.class.getName();
        }
        else if( LOG4J_PROPERTY.equals( type ) )
        {
            return PropertyLog4JLoggerStoreFactory.class.getName();
        }
        else if( LOGKIT_EXCALIBUR.equals( type ) )
        {
            return ExcaliburLogKitLoggerStoreFactory.class.getName();
        }
        else if( LOGKIT_SIMPLE.equals( type ) )
        {
            return SimpleLogKitLoggerStoreFactory.class.getName();
        }
        else if( JDK14.equals( type ) )
        {
            return Jdk14LoggerStoreFactory.class.getName();
        }
        else
        {
            final String message = "Unknown type " + type;
            throw new IllegalArgumentException( message );
        }
    }
}
