/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.threadpool.impl;

import org.codehaus.dna.impl.ConsoleLogger;
import org.codehaus.dna.impl.ContainerUtil;
import org.codehaus.dna.impl.DefaultConfiguration;
import org.codehaus.spice.threadpool.ThreadPool;

/**
 *  A TestCase for the DNACommonsThreadPool.
 *
 * @author Mauro Talevi
 * @version $Revision: 1.3 $ $Date: 2004-04-25 11:14:17 $
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
