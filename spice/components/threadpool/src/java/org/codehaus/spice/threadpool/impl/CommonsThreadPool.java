/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.threadpool.impl;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.codehaus.spice.threadpool.ThreadPool;
import org.codehaus.spice.threadpool.ThreadPoolMonitor;

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
 * @version $Revision: 1.1 $ $Date: 2003-11-20 00:23:44 $
 */
public class CommonsThreadPool
    extends AbstractThreadPool
    implements ThreadPool, PoolableObjectFactory
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
     * Flag indicating whether component is disposed.
     * If it is disposed it should not try to repool workers.
     */
    private boolean m_disposed;

    /**
     * The monitor that receives notifications of
     * changes in pool.
     */
    private ThreadPoolMonitor m_monitor;

   /**
     * Initialize the underlying pool.
     */
    public void setup()
    {
        setThreadGroup( Thread.currentThread().getThreadGroup() );
        m_monitor.newThreadPool( getName(),
                                 getPriority(),
                                 isDaemon(),
                                 m_config.maxActive,
                                 m_config.maxIdle );
        m_pool = new GenericObjectPool( this, m_config );
        setDisposeTime( 100 );
    }

    /**
     * Shutdown all threads associated with pool.
     */
    public void shutdown()
    {
        shutdownInUseThreads();
        m_disposed = true;
        try
        {
            m_pool.close();
        }
        catch( final Exception e )
        {
            final String message = "Error closing pool: " + e;
            m_monitor.unexpectedError( message, e );
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
            m_monitor.threadRetrieved( worker );
            return worker;
        }
        catch( final Exception e )
        {
            m_monitor.unexpectedError( "Retrieving thread from pool", e );
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
            final String message =
               "Ignoring attempt to return worker to " +
               "disposed pool: " + worker.getName() +
               ". Attempting dispose of worker.";
            m_monitor.unexpectedError( message, null );
            destroyWorker( worker );
            return;
        }

        m_monitor.threadReturned( worker );
        try
        {
            m_pool.returnObject( worker );
        }
        catch( final Exception e )
        {
           final String message =
              "Returning '" + worker.getName() + "' To Pool";
            m_monitor.unexpectedError( message, e );
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
        m_monitor.threadCreated( worker );
        return worker;
    }

    /**
     * Overide destruction of worker to add logging.
     *
     * @param worker the worker thread
     */
    protected void destroyWorker( final WorkerThread worker )
    {
        m_monitor.threadDisposing( worker );
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

    /**
     * Return the configuration object for Commons Pool.
     *
     * @return the configuration object for Commons Pool.
     */
    protected final GenericObjectPool.Config getCommonsConfig()
    {
        return m_config;
    }

    /**
     * Set the Monitor to use to notify of changes in the Pool.
     *
     * @param monitor the Monitor to use to notify of changes in the Pool.
     */
    protected final void setMonitor( final ThreadPoolMonitor monitor )
    {
        m_monitor = monitor;
    }
}
