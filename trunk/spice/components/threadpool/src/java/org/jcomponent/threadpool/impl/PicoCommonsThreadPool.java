/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.threadpool.impl;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.jcomponent.threadpool.ThreadPool;
import org.jcomponent.threadpool.ThreadPoolMonitor;

/**
 * The CommonsThreadPool is a component that provides a basic
 * mechanism for pooling threads. A sample configuration for this
 * component is;
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-08-29 07:23:11 $
 */
public class PicoCommonsThreadPool
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
    private final ThreadPoolMonitor m_monitor;

    public static class Default extends PicoCommonsThreadPool {
        public Default() {
            super(new NullThreadPoolMonitor(), "Default ThreadPool",
                Thread.NORM_PRIORITY, false,
                false, 10, 5);
        }
    }

    public static class WithMonitor extends PicoCommonsThreadPool {
        public WithMonitor(ThreadPoolMonitor threadPoolMonitor) {
            super(threadPoolMonitor, "Default ThreadPool",
                Thread.NORM_PRIORITY, false,
                false, 10, 5);
        }
    }

    public static class WithMonitorAndConfig extends PicoCommonsThreadPool {
        public WithMonitorAndConfig(ThreadPoolMonitor threadPoolMonitor,
                   String name, int priority,
                   boolean isDaemon, boolean limited, int maxActiveThreads,
                   int maxIdleThreads) {
            super(threadPoolMonitor, name, priority, isDaemon, limited, maxActiveThreads, maxIdleThreads);
        }
    }

    /**
     * Constructor
     *
     */
    protected PicoCommonsThreadPool(ThreadPoolMonitor threadPoolMonitor, String name,
                                      int priority, boolean isDaemon, boolean limited,
                                      int maxActiveThreads, int maxIdleThreads )
    {
        m_monitor = threadPoolMonitor;
        setName( name );
        setPriority( priority );
        setDaemon( isDaemon );

        if( limited )
        {
            m_config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
        }
        else
        {
            m_config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_GROW;
        }

        m_config.maxActive = maxActiveThreads;
        m_config.maxIdle = maxIdleThreads;
        setThreadGroup( Thread.currentThread().getThreadGroup() );
        m_monitor.newThreadPool(getName(),getPriority(), isDaemon(), m_config.maxActive, m_config.maxIdle);
        m_pool = new GenericObjectPool( this, m_config );
        setDisposeTime( 100 );
    }


    /**
     * Shutdown all threads associated with pool.
     */

    public void dispose ()
    {
        try {
            finalize();
        } catch (Throwable throwable) {
            m_monitor.unexpectedError("Dispose Failed", throwable);
        }
    }
    protected void finalize() throws Throwable
    {
        shutdownInUseThreads();
        m_disposed = true;
        try
        {
            m_pool.close();
        }
        catch( Exception e )
        {
            m_monitor.unexpectedError("Closing Pool", e);
        }
    }

    /**
     * Retrieve a worker thread from pool.
     *
     * @return the worker thread retrieved from pool
     */
    protected WorkerThread getWorker()
    {
        WorkerThread worker = null;
        try
        {
            worker = (WorkerThread)m_pool.borrowObject();
            m_monitor.threadRetrieved(worker);
            return worker;
        }
        catch( Exception e )
        {
            m_monitor.unexpectedError("Retrieving thread from pool - " + ( worker == null ? "n/a" : worker.getName()), e );
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
            destroyWorker( worker );
            return;
        }

        m_monitor.threadReturned(worker);

        try
        {
            m_pool.returnObject( worker );
        }
        catch( Exception e )
        {
            m_monitor.unexpectedError("Return Object To Pool", e);
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

        m_monitor.threadCreated(worker);

        return worker;
    }

    /**
     * Overide destruction of worker to add logging.
     *
     * @param worker the worker thread
     */
    protected void destroyWorker( final WorkerThread worker )
    {
        m_monitor.threadDisposing(worker);
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
