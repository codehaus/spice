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

/**
 * Log4JLoggerStoreFactory is an implementation of LoggerStoreFactory
 * for the Log4J Logger.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class Log4JLoggerStoreFactory
    implements LoggerStoreFactory
{
    /**
     * Creates a LoggerStore from a given set of configuration parameters.
     * The configuration Map must contain:
     * <ol> 
     * <li> <code>InputStream</code> object keyed on <code>LoggerStoreFactory.CONFIGURATION</code>
     * encoding the configuration resource</li>
     * <li> a <code>LoggerStoreFactory.CONFIGURATION_TYPE</code>
     * containing  the configuration type - either <code>LoggerStoreFactory.PROPERTIES</code> 
     * or <code>LoggerStoreFactory.XML</code></li>
     * </ol>
     * @param config the Map of parameters for the configuration of the store
     * @return the LoggerStore
     * @throws Exception if unable to create the LoggerStore
     */
    public LoggerStore createLoggerStore( final Map config )
        throws Exception
    {
        InputStream resource = (InputStream)config.get( CONFIGURATION );
        if( resource != null )
        {
            String type = (String)config.get( CONFIGURATION_TYPE );
            if( type != null )
            {
                if( type.equals( LoggerStoreFactory.PROPERTIES ) )
                {
                    return new Log4JLoggerStore( Configurator.buildProperties( resource ) );
                }
                else if( type.equals( LoggerStoreFactory.XML ) )
                {
                    return new Log4JLoggerStore( resource );
                }
            }
        }
        throw new Exception( "Invalid configuration" );
    }
}
