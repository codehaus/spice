/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.threadpool;

/**
 * This interface defines the method through which Threads can
 * be controlled.
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-03-21 23:43:00 $
 */
public interface ThreadControl
{
    /**
     * Wait for specified time for thread to complete it's work.
     *
     * @param milliSeconds the duration in milliseconds to wait until the thread has finished work
     * @throws IllegalStateException if isValid() == false
     * @throws InterruptedException if another thread has interrupted the current thread.
     *            The interrupted status of the current thread is cleared when this exception
     *            is thrown.
     */
    void join( long milliSeconds )
        throws IllegalStateException, InterruptedException;

    /**
     * Call {@link Thread#interrupt()} on thread being controlled.
     *
     * @throws IllegalStateException if isValid() == false
     * @throws SecurityException if caller does not have permission to call interupt()
     */
    void interrupt()
        throws IllegalStateException, SecurityException;

    /**
     * Determine if thread has finished execution
     *
     * @return true if thread is finished, false otherwise
     */
    boolean isFinished();

    /**
     * Retrieve throwable that caused thread to cease execution.
     * Only valid when true == isFinished()
     *
     * @return the throwable that caused thread to finish execution
     */
    Throwable getThrowable();
}
