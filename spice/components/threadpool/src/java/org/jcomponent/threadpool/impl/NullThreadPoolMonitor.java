package org.jcomponent.threadpool.impl;

import org.jcomponent.threadpool.ThreadPoolMonitor;

/**
 * @author Mauro n Paul
 */
public class NullThreadPoolMonitor implements ThreadPoolMonitor {
    public void newThreadPool(String name, int priority, boolean daemon, int maxActive, int maxIdle) {
    }

    public void unexpectedThrowable(String reference, Throwable t) {
    }

    public void threadRetrieved(WorkerThread worker) {
    }

    public void threadReturned(WorkerThread worker) {
    }

    public void threadCreated(WorkerThread worker) {
    }

    public void threadDisposing(WorkerThread worker) {
    }
}
