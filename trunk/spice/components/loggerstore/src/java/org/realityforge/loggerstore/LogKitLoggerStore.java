/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.loggerstore;

import org.apache.avalon.excalibur.logger.LogKitLoggerManager;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.context.DefaultContext;
import org.apache.avalon.framework.logger.Logger;
import org.apache.log.Hierarchy;
import org.apache.log.LogTarget;
import org.apache.log.util.Closeable;

/**
 * <p>LogKitLoggerStore extends AbstractLoggerStore to provide the implementation
 * specific to the LogKit logger. </p>
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class LogKitLoggerStore
    extends AbstractLoggerStore
{
    /** The LoggerManager */
    private final LogKitLoggerManager m_manager;

    /**
     * Creates a <code>LogKitLoggerStore</code> using the configuration configuration
     *
     * @param configuration the logger configuration
     * @throws Exception if fails to create or configure Logger
     */
    public LogKitLoggerStore( final Configuration configuration )
        throws Exception
    {
        m_manager = new LogKitLoggerManager( new Hierarchy() );
        m_manager.contextualize( new DefaultContext() );
        m_manager.configure( configuration );
        setRootLogger( m_manager.getDefaultLogger() );
    }

    /**
     *  Creates new LogKitLogger for the given category.
     */
    protected Logger createLogger( final String name )
    {
        return m_manager.getLoggerForCategory( name );
    }

    /**
     *  Closes the LoggerStore and shuts down the logger hierarchy.
     */
    public void close()
    {
        final String[] names = getCategoryNames();
        for ( int i = 0; i < names.length; i++ )
        {
            final LogTarget[] targets = m_manager.getLogTargets( names[i] );
            for ( int j = 0; j < targets.length; j++ ) 
            {
                if ( targets[j] instanceof Closeable )
                {
                    ((Closeable)targets[j]).close();
                }
            }
        }
    }
}
