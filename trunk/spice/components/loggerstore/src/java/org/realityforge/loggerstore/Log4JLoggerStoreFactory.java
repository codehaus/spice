/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.loggerstore;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.logger.LogEnabled;
import org.apache.avalon.framework.logger.Logger;

/**
 * Log4JLoggerStoreFactory is an implementation of LoggerStoreFactory
 * for the Log4J Logger.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class Log4JLoggerStoreFactory
    implements LoggerStoreFactory, LogEnabled
{
    /** The logger used by LogEnabled  */
    private Logger m_logger;

    /**
     * Provide a logger.
     *
     * @param logger the logger
     */
    public void enableLogging( final Logger logger )
    {
        m_logger = logger;
    }
    
    /**
     * Creates a LoggerStore from a given set of configuration parameters.
     * The configuration Map must contain an object keyed on <code>LoggerStoreFactory.CONFIGURATION</code>
     * containing either a <code>Properties</code> object 
     *
     * @param config the Map of parameters for the configuration of the store
     * @return the LoggerStore
     * @throws Exception if unable to create the LoggerStore
     */
    public LoggerStore createLoggerStore( final Map config )
        throws Exception
    {
        InputStream resource = (InputStream) config.get( CONFIGURATION );
        if ( resource != null )
        {
            String type = (String) config.get( CONFIGURATION_TYPE ); 
            if ( type != null )
            {
                return new Log4JLoggerStore( type, resource );
            }
        }
        throw new Exception( "Invalid configuration" );
    }
    
}
