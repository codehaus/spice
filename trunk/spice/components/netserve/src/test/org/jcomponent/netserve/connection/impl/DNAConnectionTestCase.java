/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.impl;

import org.jcontainer.dna.impl.DefaultConfiguration;
import org.jcontainer.dna.impl.ContainerUtil;
import org.jcontainer.dna.impl.ConsoleLogger;
import org.jcontainer.dna.impl.DefaultResourceLocator;
import org.jcomponent.netserve.connection.ConnectionManager;
import org.jcomponent.threadpool.ThreadPool;

/**
 * TestCase for {@link DNAConnectionManager}.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @version $Revision: 1.1 $ $Date: 2003-09-21 12:43:12 $
 */
public class DNAConnectionTestCase
    extends AbstractConnectionTestCase
{
    public DNAConnectionTestCase( final String name )
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
        final ConsoleLogger logger = new ConsoleLogger( ConsoleLogger.LEVEL_NONE );

        final DefaultResourceLocator locator = new DefaultResourceLocator();
        if( addThreadPool )
        {
            locator.put( ThreadPool.class.getName(), new TestThreadPool() );
        }

        final DefaultConfiguration config = new DefaultConfiguration( "root", "", "" );
        final DefaultConfiguration soTimeoutConfig = new DefaultConfiguration( "soTimeout", "", "" );
        soTimeoutConfig.setValue( String.valueOf( soTimeoutVal ) );
        config.addChild( soTimeoutConfig );
        final DefaultConfiguration forceShutdownConfig =
            new DefaultConfiguration( "forceShutdown", "", "" );
        forceShutdownConfig.setValue( String.valueOf( forceShutdown ) );
        config.addChild( forceShutdownConfig );
        final DefaultConfiguration shutdownTimeoutConfig =
            new DefaultConfiguration( "shutdownTimeout", "", "" );
        shutdownTimeoutConfig.setValue( String.valueOf( shutdownTimeout ) );
        config.addChild( shutdownTimeoutConfig );

        final ConnectionManager cm = new DNAConnectionManager();
        ContainerUtil.enableLogging( cm, logger );
        ContainerUtil.compose( cm, locator );
        ContainerUtil.configure( cm, config );
        ContainerUtil.initialize( cm );
        return cm;
    }

    protected ConnectionMonitor createConnectionMonitor()
    {
        final DNAConnectionMonitor monitor = new DNAConnectionMonitor();
        ContainerUtil.enableLogging( monitor, new ConsoleLogger() );
        return monitor;
    }
    protected ConnectionMonitor createConnectionMonitorNoLogging()
    {
        final DNAConnectionMonitor monitor = new DNAConnectionMonitor();
        ContainerUtil.enableLogging( monitor, new ConsoleLogger( ConsoleLogger.LEVEL_NONE ) );
        return monitor;
    }

    protected void disposeConnectionManager( final ConnectionManager cm ) throws Exception
    {
        ContainerUtil.dispose( cm );
    }
}
