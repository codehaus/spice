/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.threadpool.impl;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.jcontainer.dna.Active;
import org.jcontainer.dna.Configurable;
import org.jcontainer.dna.Configuration;
import org.jcontainer.dna.ConfigurationException;
import org.jcontainer.dna.LogEnabled;
import org.jcontainer.dna.Logger;
import org.jcontainer.dna.impl.ContainerUtil;

/**
 * The DNACommonsThreadPool wraps the CommonsThreadPool for
 * DNA-compatible systems.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @version $Revision: 1.1 $ $Date: 2003-09-02 08:09:21 $
 * @dna.service type="ThreadPool"
 */
public class DNACommonsThreadPool
    extends CommonsThreadPool
    implements LogEnabled, Configurable, Active
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
     * @dna.configuration
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
        final DNAThreadPoolMonitor monitor = new DNAThreadPoolMonitor();
        ContainerUtil.enableLogging( monitor, m_logger );
        setMonitor( monitor );
        setup();
    }

    public void dispose()
    {
        shutdown();
    }
}
