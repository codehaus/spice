/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.threadpool.impl;

import org.jcomponent.threadpool.ThreadPool;
import org.jcontainer.dna.impl.ConsoleLogger;
import org.jcontainer.dna.impl.ContainerUtil;
import org.jcontainer.dna.impl.DefaultConfiguration;

/**
 *  A TestCase for the DNACommonsThreadPool.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @version $Revision: 1.2 $ $Date: 2003-09-02 09:50:28 $
 */
public class DNAThreadPoolTestCase
    extends AbstractThreadPoolTestCase
{
    public DNAThreadPoolTestCase( final String name )
    {
        super( name );
    }

    protected AbstractThreadPool createThreadPool() throws Exception
    {
        return createDNAThreadPool( ConsoleLogger.LEVEL_NONE);
    }

    protected AbstractThreadPool createThreadPoolWithDebug() throws Exception
    {
        return createDNAThreadPool( ConsoleLogger.LEVEL_DEBUG );
    }

    protected void destroyThreadPool( final ThreadPool threadPool ) throws Exception
    {
        ContainerUtil.dispose( threadPool );
    }

    private DNACommonsThreadPool createDNAThreadPool( final int debug ) throws Exception
    {
        final DNACommonsThreadPool threadPool = new DNACommonsThreadPool();
        ContainerUtil.enableLogging( threadPool, new ConsoleLogger( debug ) );
        DefaultConfiguration configuration = buildConfiguration();
        ContainerUtil.configure( threadPool, configuration );
        ContainerUtil.initialize( threadPool );
        return threadPool;
    }

    private DefaultConfiguration buildConfiguration()
    {
        final DefaultConfiguration configuration = new DefaultConfiguration( "root", "", "" );
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
        final DefaultConfiguration child = new DefaultConfiguration( name, "", "" );
        child.setValue( value );
        configuration.addChild( child );
    }
}
