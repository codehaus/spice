/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.impl;

import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.apache.avalon.framework.container.ContainerUtil;
import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.service.DefaultServiceManager;
import org.jcomponent.netserve.connection.ConnectionManager;
import org.jcomponent.threadpool.ThreadPool;

/**
 * TestCase for {@link AvalonConnectionManager}.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @version $Revision: 1.8 $ $Date: 2003-09-02 04:15:29 $
 */
public class AvalonConnectionTestCase
    extends AbstractConnectionTestCase
{
    public AvalonConnectionTestCase( final String name )
    {
        super( name );
    }

    protected void setUp() throws Exception
    {
        setMonitor( createConnectionMonitor() );
    }

    protected ConnectionManager createConnectionManager( boolean addThreadPool, final int soTimeoutVal, final boolean forceShutdown, final int shutdownTimeout )
        throws Exception
    {
        final ConsoleLogger logger = new ConsoleLogger( ConsoleLogger.LEVEL_DISABLED );

        final DefaultServiceManager manager = new DefaultServiceManager();
        if( addThreadPool )
        {
            manager.put( ThreadPool.class.getName(), new TestThreadPool() );
        }

        final DefaultConfiguration config = new DefaultConfiguration( "root", "" );
        final DefaultConfiguration soTimeoutConfig = new DefaultConfiguration( "soTimeout", "" );
        soTimeoutConfig.setValue( String.valueOf( soTimeoutVal ) );
        config.addChild( soTimeoutConfig );
        final DefaultConfiguration forceShutdownConfig =
            new DefaultConfiguration( "forceShutdown", "" );
        forceShutdownConfig.setValue( String.valueOf( forceShutdown ) );
        config.addChild( forceShutdownConfig );
        final DefaultConfiguration shutdownTimeoutConfig =
            new DefaultConfiguration( "shutdownTimeout", "" );
        shutdownTimeoutConfig.setValue( String.valueOf( shutdownTimeout ) );
        config.addChild( shutdownTimeoutConfig );

        final ConnectionManager cm = new AvalonConnectionManager();
        ContainerUtil.enableLogging( cm, logger );
        ContainerUtil.service( cm, manager );
        ContainerUtil.configure( cm, config );
        ContainerUtil.initialize( cm );
        return cm;
    }

    protected ConnectionMonitor createConnectionMonitor()
    {
        final AvalonConnectionMonitor monitor = new AvalonConnectionMonitor();
        ContainerUtil.enableLogging( monitor, new ConsoleLogger() );
        return monitor;
    }
    protected ConnectionMonitor createConnectionMonitorNoLogging()
    {
        final AvalonConnectionMonitor monitor = new AvalonConnectionMonitor();
        ContainerUtil.enableLogging( monitor, new ConsoleLogger( ConsoleLogger.LEVEL_DISABLED) );
        return monitor;
    }

    protected void disposeConnectionManager( final ConnectionManager cm )
    {
        ContainerUtil.dispose( cm );
    }
}
