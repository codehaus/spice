/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.threadpool.impl;

import org.jcomponent.threadpool.ThreadControl;

/**
 * Default implementation of ThreadControl interface.
 * Is used by worker thread to supply control information to the
 * clients of thread pool.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-07-13 18:03:03 $
 */
final class DefaultThreadControl
    implements ThreadControl
{
    ///Thread that this control is associated with
    private Thread m_thread;

    ///Throwable that caused thread to terminate
    private Throwable m_throwable;

    /**
     * Construct thread control for a specific thread.
     *
     * @param thread the thread to control
     */
    protected DefaultThreadControl( final Thread thread )
    {
        m_thread = thread;
    }

    /**
     * Wait for specified time for thread to complete it's work.
     *
     * @param milliSeconds the duration in milliseconds to wait until the thread has finished work
     * @throws IllegalStateException if isValid() == false
     * @throws InterruptedException if another thread has interrupted the current thread.
     *         The interrupted status of the current thread is cleared when this exception
     *         is thrown.
     */
    public synchronized void join( final long milliSeconds )
        throws IllegalStateException, InterruptedException
    {
        final long start = System.currentTimeMillis();
        final long end = start + milliSeconds;
        while( !isFinished() )
        {
            final long now = System.currentTimeMillis();
            if( now >= end )
            {
                break;
            }
            final long remaining = end - now;
            wait( remaining );
        }
    }

    /**
     * Call Thread.interrupt() on thread being controlled.
     *
     * @throws IllegalStateException if isValid() == false
     * @throws SecurityException if caller does not have permission to call interupt()
     */
    public synchronized void interrupt()
        throws IllegalStateException, SecurityException
    {
        if( !isFinished() )
        {
            m_thread.interrupt();
        }
    }

    /**
     * Determine if thread has finished execution
     *
     * @return true if thread is finished, false otherwise
     */
    public synchronized boolean isFinished()
    {
        return ( null == m_thread );
    }

    /**
     * Retrieve throwable that caused thread to cease execution.
     * Only valid when true == isFinished()
     *
     * @return the throwable that caused thread to finish execution
     */
    public Throwable getThrowable()
    {
        return m_throwable;
    }

    /**
     * Method called by thread to release control.
     *
     * @param throwable Throwable that caused thread to complete (may be null)
     */
    protected synchronized void finish( final Throwable throwable )
    {
        m_thread = null;
        m_throwable = throwable;
        notifyAll();
    }
}
