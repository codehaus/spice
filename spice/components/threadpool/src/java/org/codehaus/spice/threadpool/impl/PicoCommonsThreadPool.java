/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.threadpool.impl;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.codehaus.spice.threadpool.ThreadPoolMonitor;

/**
 * The PicoCommonsThreadPool wraps the CommonsThreadPool for
 * Pico-compatible systems.
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-03-21 23:42:58 $
 */
public class PicoCommonsThreadPool
    extends CommonsThreadPool
{
    public static class Default
        extends PicoCommonsThreadPool
    {
        public Default()
        {
            super( new NullThreadPoolMonitor(),
                   "Default ThreadPool",
                   Thread.NORM_PRIORITY,
                   false,
                   false,
                   10,
                   5 );
        }
    }

    public static class WithMonitor
        extends PicoCommonsThreadPool
    {
        public WithMonitor( final ThreadPoolMonitor monitor )
        {
            super( monitor,
                   "Default ThreadPool",
                   Thread.NORM_PRIORITY,
                   false,
                   false,
                   10,
                   5 );
        }
    }

    public static class WithMonitorAndConfig
        extends PicoCommonsThreadPool
    {
        public WithMonitorAndConfig( final ThreadPoolMonitor monitor,
                                     final String name,
                                     final int priority,
                                     final boolean isDaemon,
                                     final boolean limited,
                                     final int maxActiveThreads,
                                     final int maxIdleThreads )
        {
            super( monitor, name, priority, isDaemon, limited, maxActiveThreads, maxIdleThreads );
        }
    }

    /**
     * Constructor
     *
     */
    protected PicoCommonsThreadPool( final ThreadPoolMonitor monitor,
                                     final String name,
                                     final int priority,
                                     final boolean isDaemon,
                                     final boolean limited,
                                     final int maxActiveThreads,
                                     final int maxIdleThreads )
    {
        setMonitor( monitor );
        setName( name );
        setPriority( priority );
        setDaemon( isDaemon );

        final GenericObjectPool.Config config = getCommonsConfig();
        if( limited )
        {
            config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
        }
        else
        {
            config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_GROW;
        }

        config.maxActive = maxActiveThreads;
        config.maxIdle = maxIdleThreads;
        setup();
    }

    /**
     * Make sure that finalize results in disposal
     * of the system.
     */
    protected void finalize()
    {
        shutdown();
    }
}
