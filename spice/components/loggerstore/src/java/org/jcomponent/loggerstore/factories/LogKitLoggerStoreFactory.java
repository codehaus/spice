/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.loggerstore.factories;

import java.io.InputStream;
import java.util.Map;
import org.apache.avalon.excalibur.logger.LogKitLoggerManager;
import org.apache.avalon.excalibur.logger.LoggerManager;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.logger.Logger;
import org.jcomponent.loggerstore.LoggerStore;
import org.jcomponent.loggerstore.stores.LogKitLoggerStore;

/**
 * LogKitLoggerStoreFactory is an implementation of LoggerStoreFactory
 * for the LogKit Logger.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-10-19 01:51:35 $
 */
public class LogKitLoggerStoreFactory
    extends AbstractLoggerStoreFactory
{
    /**
     * The LOGGER_MANAGER key.  Used to define the classname of the
     * LoggerManager to use in creating a LogKitLoggerStore when not specified in the
     * configuration map.
     */
    public static final String LOGGER_MANAGER = "org.jcomponent.loggerstore.logkit.loggermanager";

    /**
     * The default LoggerManager class name
     */
    private static final String DEFAULT_LOGGER_MANAGER = LogKitLoggerManager.class.getName();

    /**
     * Creates a LoggerStore from a given set of configuration parameters.
     *
     * @param config the Map of parameters for the configuration of the store
     * @return the LoggerStore
     * @throws Exception if unable to create the LoggerStore
     */
    protected LoggerStore doCreateLoggerStore( final Map config )
        throws Exception
    {
        LoggerManager loggerManager =
            (LoggerManager)config.get( LoggerManager.class.getName() );
        if( null == loggerManager )
        {
            String type = (String)config.get( LOGGER_MANAGER );
            if( null == type )
            {
                type = DEFAULT_LOGGER_MANAGER;
            }
            final ClassLoader classLoader = getClassLoader( config );
            loggerManager = createLoggerManager( type, classLoader );
        }

        Logger logger =
            (Logger)config.get( Logger.class.getName() );
        if( null == logger )
        {
            logger = loggerManager.getDefaultLogger();
        }

        final Context context =
            (Context)config.get( Context.class.getName() );

        final Configuration configuration =
            (Configuration)config.get( Configuration.class.getName() );
        if( null != configuration )
        {
            return new LogKitLoggerStore( loggerManager, logger, context, configuration );
        }

        final InputStream resource = getInputStream( config );
        if( null != resource )
        {
            final DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
            return new LogKitLoggerStore( loggerManager, logger, context, builder.build( resource ) );
        }

        return missingConfiguration();
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
                loader = LogKitLoggerStoreFactory.class.getClassLoader();
            }
        }
        return loader;
    }

    /**
     * Create a {@link LoggerManager} for specified type.
     *
     * @param type the type of the LoggerManager to use.
     * @return the created {@link LoggerManager}
     */
    private LoggerManager createLoggerManager( final String type,
                                               final ClassLoader classLoader )
    {
        try
        {
            final Class clazz = classLoader.loadClass( type );
            return (LoggerManager)clazz.newInstance();
        }
        catch( final Exception e )
        {
            final String message =
                "Failed to created LoggerManager: " + type;
            throw new IllegalArgumentException( message );
        }
    }
}
