/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.loggerstore;

import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.logger.LogEnabled;
import org.apache.avalon.framework.logger.Logger;

/**
 * AbstractLoggerStore is an abstract implementation of LoggerStore for the
 * functionality common to all Loggers.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public abstract class AbstractLoggerStore 
    implements LoggerStore, LogEnabled
{
    /** Map of Loggers held in the store */
    protected final Map m_loggers = new HashMap ();
    
    /** The Logger used by LogEnabled. */
    private Logger m_logger;
    
    /** The root Logger */
    private Logger m_rootLogger;
    
    /**
     * Provide a logger.
     *
     * @param logger the logger
     */
    public void enableLogging ( final Logger logger )
    {
        m_logger = logger;
    }
    
    /**
     * Retrieves the root Logger from the store.
     * @return the Logger
     * @throws Exception if unable to retrieve Logger
     */
    public Logger getLogger ( )
        throws Exception
    {
        return getRootLogger ();
    }
    
    /**
     * Retrieves a Logger hierarchy from the store for a given category name.
     * @param categoryName the name of the logger category.
     * @return the Logger
     * @throws Exception if unable to retrieve Logger
     */
    public Logger getLogger ( final String categoryName )
        throws Exception
    {
        if ( categoryName == null )
        {
            final String message = "categoryName cannot be null.  Use getLogger() for root Logger";
            throw new Exception ( message );
        }
        Logger logger = retrieveLogger ( categoryName );
        if ( logger == null )
        {
            if( m_logger != null
            && m_logger.isDebugEnabled () )
            {
                final String message = "Logger for category " + categoryName +
                " not defined in configuration. New Logger created and returned";
                m_logger.debug ( message );
            }
            logger = createLogger ( categoryName );
            storeLogger ( categoryName, logger);
        }
        return logger;
    }
    
    /**
     *  Creates new Logger for the given category.
     *  This is logger-implementation specific and will be implemented in
     *  concrete subclasses.
     */
    protected abstract Logger createLogger ( String categoryName );
    
    /**
     * Sets the root Logger.
     */
    protected void setRootLogger (Logger rootLogger)
    {
        m_rootLogger = rootLogger;
    }
    
    /**
     * Returns the root Logger.
     */
    private Logger getRootLogger ()  throws Exception
    {
        if( m_logger != null && m_logger.isDebugEnabled () )
        {
            final String message = "Root Logger returned";
            m_logger.debug ( message );
        }
        if ( m_rootLogger == null )
        {
            final String message = "Root Logger is not defined";
            throw new Exception ( message );
        }
        return m_rootLogger;
    }
    
    /**
     *  Retrieve Logger from store map.
     *  @param categoryName the category of the Logger
     *  @return the Logger instance or <code>null</code> if not found in map.
     */
    private Logger retrieveLogger ( final String categoryName )
    {
        Logger logger = (Logger)m_loggers.get ( categoryName );
        
        if( null != logger )
        {
            if( m_logger.isDebugEnabled () )
            {
                final String message = "Logger for category " + categoryName +
                " retrieved";
                m_logger.debug ( message );
            }
        }
        
        return logger;
    }
    
    /**
     *  Stores Logger in map.
     *  @param categoryName the category of the Logger
     *  @param logger the Logger instance
     */
    private void storeLogger ( final String categoryName, final Logger logger )
    {
        m_loggers.put ( categoryName, logger );
    }
    
}
