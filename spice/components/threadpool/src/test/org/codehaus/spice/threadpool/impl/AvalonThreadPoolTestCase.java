/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.threadpool.impl;

import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.apache.avalon.framework.container.ContainerUtil;
import org.apache.avalon.framework.logger.ConsoleLogger;
import org.codehaus.spice.threadpool.ThreadPool;

/**
 *  A TestCase for the AvalonCommonsThreadPool.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @author Mauro Talevi
 * @version $Revision: 1.2 $ $Date: 2004-02-28 21:13:22 $
 */
public class AvalonThreadPoolTestCase
    extends AbstractThreadPoolTestCase
{
    public AvalonThreadPoolTestCase( final String name )
    {
        super( name );
    }

    protected AbstractThreadPool createThreadPool() throws Exception
    {
        return createAvalonThreadPool( ConsoleLogger.LEVEL_DISABLED );
    }

    protected AbstractThreadPool createThreadPoolWithDebug() throws Exception
    {
        return createAvalonThreadPool( ConsoleLogger.LEVEL_DEBUG );
    }

    protected void destroyThreadPool( final ThreadPool threadPool ) throws Exception
    {
        ContainerUtil.shutdown( threadPool );
    }

    private AvalonCommonsThreadPool createAvalonThreadPool( final int debug ) throws Exception
    {
        final AvalonCommonsThreadPool threadPool = new AvalonCommonsThreadPool();
        ContainerUtil.enableLogging( threadPool, new ConsoleLogger( debug ) );
        DefaultConfiguration configuration = buildConfiguration();
        ContainerUtil.configure( threadPool, configuration );
        ContainerUtil.initialize( threadPool );
        return threadPool;
    }

    private DefaultConfiguration buildConfiguration()
    {
        final DefaultConfiguration configuration = new DefaultConfiguration( "root", "" );
        addChild( configuration, "name", "testThreadPool" );
        addChild( configuration, "priority", "5" );
        addChild( configuration, "is-daemon", "false" );
        addChild( configuration, "max-threads", "3" );
        addChild( configuration, "max-idle", "1" );
        return configuration;
    }

    private void addChild( final DefaultConfiguration configuration,
                           final String name,
                           final String value )
    {
        final DefaultConfiguration child = new DefaultConfiguration( name, "" );
        child.setValue( value );
        configuration.addChild( child );
    }
}
