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
 * Jdk14LoggerStoreFactory is an implementation of LoggerStoreFactory
 * for the JDK14 Logger.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class Jdk14LoggerStoreFactory
    implements LoggerStoreFactory
{
    /**
     * Creates a LoggerStore from a given set of configuration parameters.
     *
     * @param config the Map of parameters for the configuration of the store
     * @return the LoggerStore
     * @throws Exception if unable to create the LoggerStore
     */
    public LoggerStore createLoggerStore( final Map config )
        throws Exception
    {
        Object o = config.get( CONFIGURATION );
        if( o != null && o instanceof InputStream )
        {
            return new Jdk14LoggerStore( (InputStream)o );
        }
        throw new Exception( "Invalid configuration" );
    }
}
