/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.loggerstore;

import java.io.InputStream;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.context.DefaultContext;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.logger.LogKitLogger;
import org.apache.avalon.excalibur.logger.LogKitLoggerManager;

/**
 * <p>LogKitLoggerStore extends AbstractLoggerStore to provide the implementation
 * specific to the LogKit logger. </p>
 * 
 * <p>LogKitLoggerStore is currently a facade for the Excalibur LogKitLoggerManager
 * which allows the configuration of the LogKit.</p>
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class LogKitLoggerStore extends AbstractLoggerStore
{
    /** The LogKitLoggerManager used for create and configure LogKit Loggers */
    private LogKitLoggerManager m_manager;
    
    /**
     * Creates a <code>LogKitLoggerStore</code> using the configuration resource
     * Currently only the XML configuration type is supported.
     *
     * @param resource the InputStream encoding the configuration resource
     * @throws Exception if fails to create or configure Logger
     */
    public LogKitLoggerStore( final InputStream resource )
        throws Exception
    {
        m_manager = new LogKitLoggerManager();
        m_manager.contextualize( new DefaultContext() );
        m_manager.configure( Configurator.buildConfiguration( resource ) );
        setRootLogger( m_manager.getDefaultLogger() );
    }

    /** 
     *  Creates new LogKitLogger for the given category.  
     */
    protected Logger createLogger( final String categoryName ) 
    {
        return m_manager.getLoggerForCategory( categoryName );
    }

    /** 
     *  Closes the LoggerStore and shuts down the logger hierarchy. 
     */
    public void close()
    {
    }

}
