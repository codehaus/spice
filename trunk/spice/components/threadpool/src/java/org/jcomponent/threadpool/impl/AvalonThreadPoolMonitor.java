package org.jcomponent.threadpool.impl;

import org.jcomponent.threadpool.ThreadPoolMonitor;
import org.apache.avalon.framework.logger.AbstractLogEnabled;

/**
 * @author Mauro n Paul
 */
public class AvalonThreadPoolMonitor extends AbstractLogEnabled
    implements ThreadPoolMonitor {
    public void newThreadPool(String name, int priority, boolean daemon, int maxActive, int maxIdle) {

        if (getLogger().isInfoEnabled()) {
            getLogger().info("Creating a new ThreadPool " + name +
                "(priority=" + priority +
                ",isDaemon=" + daemon + ") with " +
                "max-threads=" + maxActive + " and " +
                "max-idle=" + maxIdle);
        }

    }

    public void unexpectedThrowable(String reference, Throwable t) {
        if (getLogger().isErrorEnabled()) {

            getLogger().error("Unexpected Exception (" + reference + ")", t);
        }
    }

    public void threadRetrieved(WorkerThread worker) {
        if (getLogger().isDebugEnabled()) {

            getLogger().debug("Thread retrieved - " + worker.getName());
        }
    }

    public void threadReturned(WorkerThread worker) {
        if (getLogger().isDebugEnabled()) {

            getLogger().debug("Thread returned - " + worker.getName());
        }
    }

    public void threadCreated(WorkerThread worker) {
        if (getLogger().isInfoEnabled()) {

            getLogger().info("Thread Created - " + worker.getName());
        }
    }

    public void threadDisposing(WorkerThread worker) {
        if (getLogger().isInfoEnabled()) {

            getLogger().info("Thread Disposing - " + worker.getName());
        }
    }
}
