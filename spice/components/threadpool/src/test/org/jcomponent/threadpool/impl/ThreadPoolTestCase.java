/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.threadpool.impl;

import junit.framework.TestCase;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.apache.avalon.framework.container.ContainerUtil;
import org.apache.avalon.framework.logger.ConsoleLogger;
import org.jcomponent.threadpool.Executable;
import org.jcomponent.threadpool.ThreadPool;
import org.realityforge.configkit.ConfigValidator;
import org.realityforge.configkit.ConfigValidatorFactory;
import org.realityforge.configkit.ValidateException;
import org.xml.sax.ErrorHandler;

import java.io.InputStream;

/**
 *  An basic test case for the ThreadPools.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-08-27 19:10:42 $
 */
public class ThreadPoolTestCase
    extends TestCase
{
    private int m_debug;

    public ThreadPoolTestCase( final String name )
    {
        super( name );
    }

    public void testSchemaValidation()
        throws Exception
    {
        final InputStream schema =
            getClass().getResourceAsStream( "CommonsThreadPool-schema.xml" );
        assertNotNull( "Schema file", schema );
        final ConfigValidator validator =
            ConfigValidatorFactory.create( "http://relaxng.org/ns/structure/1.0", schema );
        final InputStream config =
            getClass().getResourceAsStream( "commons-config.xml" );
        try
        {
            validator.validate( config, (ErrorHandler)null );
        }
        catch( ValidateException e )
        {
            fail( "Unexpected validation failure: " + e );
        }
    }

    public void testNullInCtor()
        throws Exception
    {
        try
        {
            new ExecutableRunnable( null );
            fail( "Expected a NPE" );
        }
        catch( NullPointerException e )
        {
            assertEquals( e.getMessage(), "runnable" );
        }
        try
        {
            new WorkerThread( createThreadPool(), Thread.currentThread().getThreadGroup(), null );
            fail( "Expected a NPE" );
        }
        catch( NullPointerException e )
        {
            assertEquals( e.getMessage(), "name" );
        }
        try
        {
            new WorkerThread( null, Thread.currentThread().getThreadGroup(), "blah" );
            fail( "Expected a NPE" );
        }
        catch( NullPointerException e )
        {
            assertEquals( e.getMessage(), "pool" );
        }
    }

    public void testThreadPool()
        throws Exception
    {
        m_debug = ConsoleLogger.LEVEL_DISABLED;
        doThreadPoolTest();
        m_debug = ConsoleLogger.LEVEL_DEBUG;
        doThreadPoolTest();
    }

    private void doThreadPoolTest() throws Exception
    {
        ThreadPool threadPool = null;
        ThreadPoolEntry entry = null;

        threadPool = createThreadPool();

        //Just make sure we test run therunnable version aswell
        threadPool.execute( (Runnable)new Work( false, 0, null ) );

        entry = createEntry( threadPool, new Work( true, 0, null ) );
        verifyEntry( entry );
        entry.m_work.unlock();
        verifyEntry( entry );
        entry.m_control.join( 20 );

        entry = createEntry( threadPool, new Work( true, 0, null ) );
        verifyEntry( entry );
        entry.m_control.join( 20 );
        verifyEntry( entry );
        entry.m_work.unlock();

        entry = createEntry( threadPool, new Work( true, 0, null ) );
        verifyEntry( entry );
        entry.m_work.unlock();
        verifyEntry( entry );
        entry.m_control.interrupt();

        entry = createEntry( threadPool, new Work( true, 0, null ) );
        verifyEntry( entry );
        entry.m_control.interrupt();
        verifyEntry( entry );
        entry.m_work.unlock();

        createEntry( threadPool, new Work( false, 500, null ) );
        createEntry( threadPool, new Work( false, 500, null ) );
        createEntry( threadPool, new Work( false, 500, null ) );
        createEntry( threadPool, new Work( false, 500, null ) );

        //Sleep a second to allow reaper to remove some of those workers
        Thread.sleep( 1000 );

        entry = createEntry( threadPool, new Work( false, 50, new Exception() ) );
        entry.m_control.join( 500 );
        verifyEntry( entry );

        entry = createEntry( threadPool, new Work( false, 50, new ThreadDeath() ) );
        entry.m_control.join( 500 );
        verifyEntry( entry );

        destroyThreadPool( threadPool );

        threadPool = createThreadPool();
        //Okay the following creates a thread that wont end
        //but we try to shutdown pull anyways - should not hang
        entry = createEntry( threadPool, new Work( true, 0, null ) );
        destroyThreadPool( threadPool );

        entry.m_work.unlock();

        //Sleep a second to allow an attempt to
        //recycle worker when pool disposed and thus
        //worker not "alive"
        Thread.sleep( 1000 );
    }

    private void destroyThreadPool( final ThreadPool threadPool ) throws Exception
    {
        ContainerUtil.shutdown( threadPool );
    }

    private void verifyEntry( ThreadPoolEntry entry )
    {
        synchronized( entry.m_control )
        {
            final boolean finished = entry.m_control.isFinished();
            assertEquals( "Matching finished flags",
                          finished,
                          entry.m_work.isDone() );

            if( finished )
            {
                assertEquals( "Matching exceptions",
                              entry.m_control.getThrowable(),
                              entry.m_work.getException() );
            }
            else
            {
                assertNull( "Null exception for unfinished",
                            entry.m_control.getThrowable() );
            }
        }
    }

    private ThreadPoolEntry createEntry( final ThreadPool threadPool, final Work work )
    {
        final ThreadPoolEntry entry = new ThreadPoolEntry();
        entry.m_work = work;
        entry.m_control = threadPool.execute( (Executable)work );
        return entry;
    }

    private AbstractThreadPool createThreadPool() throws Exception
    {
        final AbstractThreadPool threadPool = new CommonsThreadPool();
        ContainerUtil.enableLogging( threadPool, new ConsoleLogger( m_debug ) );
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
