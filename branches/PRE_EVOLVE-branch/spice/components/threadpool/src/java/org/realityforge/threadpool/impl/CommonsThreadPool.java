/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.threadpool.impl;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.logger.LogEnabled;
import org.apache.avalon.framework.logger.Logger;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.realityforge.threadpool.ThreadPool;

/**
 * The CommonsThreadPool is a component that provides a basic
 * mechanism for pooling threads. A sample configuration for this
 * component is;
 * <pre>
 * &lt;config&gt;
 *   &lt;name&gt;MyThreadPool&lt;/name&gt; &lt;!-- base name of all threads --&gt;
 *   &lt;priority&gt;5&lt;/priority&gt; &lt;!-- set to default priority --&gt;
 *   &lt;is-daemon&gt;false&lt;/is-daemon&gt; &lt;!-- are threads daemon threads? --&gt;
 *   &lt;resource-limiting&gt;false&lt;/resource-limiting&gt; &lt;!-- will pool block when max threads reached? --&gt;
 *   &lt;max-threads&gt;10&lt;/max-threads&gt;
 *   &lt;max-idle&gt;5&lt;/max-idle&gt; &lt;!-- maximum number of idle threads --&gt;
 * &lt;/config&gt;
 * </pre>
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-04 11:39:38 $
 * @phoenix.service type="ThreadPool"
 */
public class CommonsThreadPool
    extends AbstractThreadPool
    implements ThreadPool, LogEnabled, Configurable, Initializable, Disposable, PoolableObjectFactory
{
    /**
     * The configuration 'struct' for our object pool.
     */
    private final GenericObjectPool.Config m_config = new GenericObjectPool.Config();

    /**
     * The underlying pool used to pool threads.
     */
    private GenericObjectPool m_pool;

    /**
     * The logger for component.
     */
    private Logger m_logger;

    /**
     * Flag indicating whether component is disposed.
     * If it is disposed it should not try to repool workers.
     */
    private boolean m_disposed;

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

        final boolean limit =
            configuration.getChild( "resource-limiting" ).getValueAsBoolean( false );
        if( limit )
        {
            m_config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
        }
        else
        {
            m_config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_GROW;
        }

        m_config.maxActive =
            configuration.getChild( "max-threads" ).getValueAsInteger( 10 );
        m_config.maxIdle = configuration.getChild( "max-idle" ).
            getValueAsInteger( m_config.maxActive / 2 );
    }

    /**
     * Initialize the underlying pool.
     *
     * @throws Exception if error occurs creating pool
     */
    public void initialize()
        throws Exception
    {
        setThreadGroup( Thread.currentThread().getThreadGroup() );
        if( m_logger.isInfoEnabled() )
        {
            m_logger.info( "Creating a new ThreadPool " + getName() +
                           "(priority=" + getPriority() +
                           ",isDaemon=" + isDaemon() + ") with " +
                           "max-threads=" + m_config.maxActive + " and " +
                           "max-idle=" + m_config.maxIdle );
        }
        m_pool = new GenericObjectPool( this, m_config );
        setDisposeTime( 100 );
    }

    /**
     * Shutdown all threads associated with pool.
     */
    public void dispose()
    {
        shutdownInUseThreads();
        m_disposed = true;
        try
        {
            m_pool.close();
        }
        catch( Exception e )
        {
            m_logger.error( "Error closing pool: " + e, e );
        }
    }

    /**
     * Retrieve a worker thread from pool.
     *
     * @return the worker thread retrieved from pool
     */
    protected WorkerThread getWorker()
    {
        try
        {
            final WorkerThread worker = (WorkerThread)m_pool.borrowObject();
            if( m_logger.isDebugEnabled() )
            {
                m_logger.debug( "Retrieved thread from pool: " + worker.getName() );
            }
            return worker;
        }
        catch( Exception e )
        {
            if( m_logger.isWarnEnabled() )
            {
                m_logger.warn( "Error retrieving thread from pool: " + e, e );
            }
            return createWorker();
        }
    }

    /**
     * Return the WorkerThread to the pool.
     *
     * @param worker the worker thread to put back in pool
     */
    protected void releaseWorker( final WorkerThread worker )
    {
        if( m_disposed )
        {
            if( m_logger.isDebugEnabled() )
            {
                m_logger.debug( "Ignoring attempt to return worker to " +
                                "disposed pool: " + worker.getName() +
                                ". Attempting dispose of worker." );
            }
            destroyWorker( worker );
            return;
        }

        if( m_logger.isDebugEnabled() )
        {
            m_logger.debug( "Returning thread to pool: " + worker.getName() );
        }

        try
        {
            m_pool.returnObject( worker );
        }
        catch( Exception e )
        {
            if( m_logger.isWarnEnabled() )
            {
                m_logger.warn( "Error returning thread to pool: " + e, e );
            }
        }
    }

    /**
     * Overide creation of worker to add logging.
     *
     * @return the new worker thread
     */
    protected WorkerThread createWorker()
    {
        final WorkerThread worker = super.createWorker();
        if( m_logger.isDebugEnabled() )
        {
            m_logger.debug( "Created thread: " + worker.getName() );
        }
        return worker;
    }

    /**
     * Overide destruction of worker to add logging.
     *
     * @param worker the worker thread
     */
    protected void destroyWorker( final WorkerThread worker )
    {
        if( m_logger.isDebugEnabled() )
        {
            m_logger.debug( "Disposing thread: " + worker.getName() );
        }
        super.destroyWorker( worker );
    }

    /**
     * Create a new worker. (Part of {@link PoolableObjectFactory} interface)
     *
     * @return the new worker
     */
    public Object makeObject()
    {
        return createWorker();
    }

    /**
     * Destroy a worker. (Part of {@link PoolableObjectFactory} interface)
     *
     * @param worker the new worker
     */
    public void destroyObject( final Object worker )
    {
        destroyWorker( (WorkerThread)worker );
    }

    /**
     * validate a worker. (Part of {@link PoolableObjectFactory} interface)
     *
     * @return true (no validation occurs)
     */
    public boolean validateObject( final Object worker )
    {
        return true;
    }

    /**
     * activate a worker. (Part of {@link PoolableObjectFactory} interface)
     * No-op.
     */
    public void activateObject( final Object worker )
    {
    }

    /**
     * passivate a worker. (Part of {@link PoolableObjectFactory} interface)
     * No-op.
     */
    public void passivateObject( final Object worker )
    {
    }
}
