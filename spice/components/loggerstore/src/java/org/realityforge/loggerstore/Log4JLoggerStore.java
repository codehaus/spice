/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.loggerstore;

import java.io.InputStream;
import java.util.Properties;

import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.logger.Log4JLogger;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * Log4JLoggerStore extends AbstractLoggerStore to provide the implementation
 * specific to the Log4J logger.  
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class Log4JLoggerStore extends AbstractLoggerStore
{
    /** The logger repository */
    private LoggerRepository m_repository;
    
    /**
     * Creates a <code>Log4JLoggerStore</code> using the configuration resource
     * @param type the String encoding the configuration type
     * @param resource the InputStream encoding the configuration resource
     * @throws Exception if fails to create or configure Logger
     */
    public Log4JLoggerStore( final String type, final InputStream resource )
        throws Exception
    {   
        m_repository = LogManager.getLoggerRepository();
        configure( type, resource, m_repository );
        setRootLogger( new Log4JLogger( m_repository.getRootLogger() ) );
    }

    /** 
     *  Creates new Log4JLogger for the given category.  
     */
    protected Logger createLogger( final String categoryName ) 
    {
        return new Log4JLogger( m_repository.getLogger( categoryName ) );
    }

    /** 
     *  Closes the LoggerStore and shuts down the logger hierarchy. 
     */
    public void close()
    {
        m_repository.shutdown();
    }

    /**
     *  Configure LoggerRepository
     */
    private void configure( String type, InputStream resource, LoggerRepository repository )
        throws Exception
    {
        if ( type.equals( LoggerStoreFactory.PROPERTIES ) )
        {
            final PropertyConfigurator configurator = new PropertyConfigurator();
            configurator.doConfigure( Configurator.buildProperties( resource ), repository );
        } else if ( type.equals( LoggerStoreFactory.PROPERTIES ) ) 
        {
            final DOMConfigurator configurator = new DOMConfigurator();
            configurator.doConfigure( resource, repository );
        }
    }
}
