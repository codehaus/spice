/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.loggerstore;

import java.io.InputStream;
import java.util.Map;

import org.apache.avalon.excalibur.logger.LoggerManager;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.logger.Logger;

/**
 * LogKitLoggerStoreFactory is an implementation of LoggerStoreFactory
 * for the LogKit Logger.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.9 $ $Date: 2003-05-31 15:21:34 $
 */
public class LogKitLoggerStoreFactory
    extends AbstractLoggerStoreFactory
{
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
        final LoggerManager loggerManager =
            (LoggerManager)config.get( LoggerManager.class.getName() );
            
        Logger logger =
            (Logger)config.get( Logger.class.getName() );
        if( null == logger )
        {
            logger = getLogger();
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
}
