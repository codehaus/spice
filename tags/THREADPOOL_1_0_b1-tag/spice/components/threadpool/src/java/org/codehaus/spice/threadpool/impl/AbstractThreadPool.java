/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.threadpool.impl;

import org.codehaus.spice.threadpool.ThreadPool;
import org.codehaus.spice.threadpool.ThreadControl;
import org.codehaus.spice.threadpool.Executable;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.Iterator;

/**
 * This is the base class of all ThreadPools.
 * Sub-classes should implement the abstract methods to
 * retrieve and return Threads to the pool.
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-03-21 23:42:58 $
 * @phoenix.service type="ThreadPool"
 */
public abstract class AbstractThreadPool
    implements ThreadPool
{
    /**
     * The set of threads that are currently in use.
     */
    private Set m_inUse = Collections.synchronizedSet( new HashSet() );

    /**
     * The thread group associated with pool.
     */
    private ThreadGroup m_threadGroup;

    /**
     * The name of the thread pool.
     * Used in naming threads.
     */
    private String m_name;

    /**
     * A Running number that indicates the number
     * of threads created by pool. Starts at 0 and
     * increases.
     */
    private int m_level;

    /**
     * A falg indicating whether the pool should create daemon threads.
     */
    private boolean m_isDaemon;

    /**
     * The priorty of the threads created by pool.
     */
    private int m_priority;

    /**
     * The maximum amount of time that will be spent disposing a thread.
     */
    private int m_disposeTime = 100;

    /**
     * Destroy a worker thread by scheduling it for shutdown.
     *
     * @param worker the worker thread
     */
    protected void destroyWorker( final WorkerThread worker )
    {
        worker.dispose( getDisposeTime() );
    }

    /**
     * Create a WorkerThread and start it up.
     *
     * @return the worker thread.
     */
    protected WorkerThread createWorker()
    {
        final String name = getName() + " Worker #" + m_level++;

        final WorkerThread worker = newWorkerThread( name );
        worker.setDaemon( m_isDaemon );
        worker.setPriority( m_priority );
        worker.start();
        return worker;
    }

    /**
     * Create a new worker for pool.
     *
     * @param name the name of worker
     * @return the new WorkerThread
     */
    protected WorkerThread newWorkerThread( final String name )
    {
        return new WorkerThread( this, getThreadGroup(), name );
    }

    /**
     * Run work in separate thread.
     * Return a valid ThreadControl to control work thread.
     *
     * @param work the work to be executed.
     * @return the ThreadControl
     */
    public ThreadControl execute( final Runnable work )
    {
        return execute( new ExecutableRunnable( work ) );
    }

    /**
     * Execute some executable work in a thread.
     *
     * @param work the work
     * @return the ThreadControl
     */
    public ThreadControl execute( final Executable work )
    {
        final WorkerThread worker = getWorker();
        worker.setPriority( m_priority );
        m_inUse.add( worker );
        return worker.execute( work );
    }

    /**
     * Helper method that attempts to shutdown all
     * threads that originated from this pool and are
     * currently in use.
     */
    protected void shutdownInUseThreads()
    {
        synchronized( m_inUse )
        {
            final Iterator iterator = m_inUse.iterator();
            while( iterator.hasNext() )
            {
                final WorkerThread worker = (WorkerThread)iterator.next();
                destroyWorker( worker );
            }
            m_inUse.clear();
        }
    }

    /**
     * Get the name used for thread pool.
     * (Used in naming threads).
     *
     * @return the thread pool name
     */
    protected String getName()
    {
        return m_name;
    }

    /**
     * Set the name used for thread pool.
     * Used in naming threads.
     *
     * @param name the thread pool name
     */
    protected void setName( final String name )
    {
        m_name = name;
    }

    /**
     * Set flag indicating whether daemon threads should be created by pool.
     *
     * @param daemon flag indicating whether daemon threads should be created by pool.
     */
    protected void setDaemon( final boolean daemon )
    {
        m_isDaemon = daemon;
    }

    /**
     * Return flag indicating whether daemon threads should be created by pool.
     *
     * @return flag indicating whether daemon threads should be created by pool.
     */
    protected boolean isDaemon()
    {
        return m_isDaemon;
    }

    /**
     * Set the priorty of threads created for pool.
     *
     * @param priority the priorty of threads created for pool.
     */
    protected void setPriority( int priority )
    {
        m_priority = priority;
    }

    /**
     * Return the priorty of threads created for pool.
     *
     * @return the priorty of threads created for pool.
     */
    protected int getPriority()
    {
        return m_priority;
    }

    /**
     * Return the thread group that thread pool is associated with.
     *
     * @return the thread group that thread pool is associated with.
     */
    protected ThreadGroup getThreadGroup()
    {
        return m_threadGroup;
    }

    /**
     * Set the thread group that thread pool is associated with.
     *
     * @param threadGroup the thread group that thread pool is associated with.
     */
    protected void setThreadGroup( final ThreadGroup threadGroup )
    {
        m_threadGroup = threadGroup;
    }

    /**
     * Return the maximum amount of time that will be spent disposing a thread.
     *
     * @return the maximum amount of time that will be spent disposing a thread.
     */
    protected int getDisposeTime()
    {
        return m_disposeTime;
    }

    /**
     * Set the maximum amount of time that will be spent disposing a thread.
     *
     * @param disposeTime the maximum amount of time that will be spent disposing a thread.
     */
    protected void setDisposeTime( final int disposeTime )
    {
        m_disposeTime = disposeTime;
    }

    /**
     * Return the WorkerThread to management by the ThreadPool object.
     *
     * @param worker the worker
     */
    protected void threadCompleted( final WorkerThread worker )
    {
        m_inUse.remove( worker );
        releaseWorker( worker );
    }

    /**
     * Retrieve a worker thread from pool.
     *
     * @return the worker thread retrieved from pool
     */
    protected abstract WorkerThread getWorker();

    /**
     * Return the WorkerThread to the pool.
     *
     * @param worker the worker thread to put back in pool
     */
    protected abstract void releaseWorker( WorkerThread worker );
}
