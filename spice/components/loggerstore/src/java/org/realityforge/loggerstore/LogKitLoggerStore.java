/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.loggerstore;

import org.apache.avalon.excalibur.logger.LogKitLoggerManager;
import org.apache.avalon.excalibur.logger.LoggerManager;
import org.apache.avalon.excalibur.logger.SimpleLogKitManager;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.container.ContainerUtil;
import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.context.DefaultContext;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.logger.NullLogger;

/**
 * <p>LogKitLoggerStore extends AbstractLoggerStore to provide the implementation
 * specific to the LogKit logger. </p>
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class LogKitLoggerStore
    extends AbstractLoggerStore
{
    /** The Logger Manager */
    private LoggerManager m_loggerManager;

    /**
     * Creates a <code>LogKitLoggerStore</code> using the given configuration 
     *
     * @param loggerManager the LoggerManager used to configure the store
     * @param logger the Logger to logEnable the LoggerManager
     * @param context the Context of the LoggerManager
     * @param configuration the logger configuration
     * @throws Exception if fails to create or configure Logger
     */
    public LogKitLoggerStore( final LoggerManager loggerManager,
                              final Logger logger,
                              final Context context,
                              final Configuration configuration )
        throws Exception
    {
        m_loggerManager = loggerManager;
        checkLoggerManager();
        if( null != logger )
        {
            ContainerUtil.enableLogging( m_loggerManager, logger );
        }
        else
        {
            ContainerUtil.enableLogging( m_loggerManager, new NullLogger() );
        }
        if( null != context )
        {
            ContainerUtil.contextualize( m_loggerManager, context );
        }
        else
        {
            ContainerUtil.contextualize( m_loggerManager, new DefaultContext() );
        }
        ContainerUtil.configure( m_loggerManager, configuration );
        setRootLogger( m_loggerManager.getDefaultLogger() );
    }


	/**
     *  Creates new LogKitLogger for the given category.
     */
    protected Logger createLogger( final String name )
    {
        return m_loggerManager.getLoggerForCategory( name );
    }

    /**
     *  Closes the LoggerStore and shuts down the logger hierarchy.
     */
    public void close()
    {
        try
        {
            ContainerUtil.shutdown( m_loggerManager );
        }
        catch( Exception e )
        {
        }
    }

    /**
     *  Checks the validity of the LoggerManager
     *  @throws Exception if invalid
     */
    private void checkLoggerManager() throws Exception
    {
        if ( m_loggerManager == null) 
        {
            throw new NullPointerException( "loggerManager" );
        }
        final boolean valid  = 
               (  m_loggerManager instanceof LogKitLoggerManager
               || m_loggerManager instanceof SimpleLogKitManager ); 
        if ( !valid )
        {
            throw new IllegalArgumentException( "loggerManager" );
        }
    }
}
