/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.threadpool.impl;

import org.apache.avalon.framework.logger.LogEnabled;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.container.ContainerUtil;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * The AvalonCommonsThreadPool wraps the CommonsThreadPool for
 * Avalon-compatible systems.
 * 
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-11-20 00:23:44 $
 * @phoenix.service type="ThreadPool"
 */
public class AvalonCommonsThreadPool
    extends CommonsThreadPool
    implements LogEnabled, Configurable, Initializable, Disposable
{
    /**
     * The logger for component.
     */
    private Logger m_logger;

    /**
     * Set the logger for component.
     *
     * @param logger the logger for component.
     */
    public void enableLogging( final Logger logger )
    {
        m_logger = logger;
    }

    /**
     * Configure the pool. See class javadocs for example.
     *
     * @param configuration the configuration object
     * @throws ConfigurationException if malformed configuration
     * @phoenix.configuration
     *    type="http://relaxng.org/ns/structure/1.0"
     *    location="CommonsThreadPool-schema.xml"
     */
    public void configure( final Configuration configuration )
        throws ConfigurationException
    {
        final String name =
            configuration.getChild( "name" ).getValue();
        setName( name );
        final int priority =
            configuration.getChild( "priority" ).getValueAsInteger( Thread.NORM_PRIORITY );
        setPriority( priority );
        final boolean isDaemon =
            configuration.getChild( "is-daemon" ).getValueAsBoolean( false );
        setDaemon( isDaemon );

        final GenericObjectPool.Config config = getCommonsConfig();

        final boolean limit =
            configuration.getChild( "resource-limiting" ).getValueAsBoolean( false );
        if( limit )
        {
            config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
        }
        else
        {
            config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_GROW;
        }

        config.maxActive =
            configuration.getChild( "max-threads" ).getValueAsInteger( 10 );
        config.maxIdle = configuration.getChild( "max-idle" ).
            getValueAsInteger( config.maxActive / 2 );
    }

    /**
     * Initialize the monitor then initialize parent class.
     */
    public void initialize()
        throws Exception
    {
        final AvalonLoggerThreadPoolMonitor monitor = new AvalonLoggerThreadPoolMonitor();
        ContainerUtil.enableLogging( monitor, m_logger );
        setMonitor( monitor );
        setup();
    }

    public void dispose()
    {
        shutdown();
    }
}
