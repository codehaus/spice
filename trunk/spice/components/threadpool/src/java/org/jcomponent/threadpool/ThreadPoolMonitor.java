package org.jcomponent.threadpool;

import org.jcomponent.threadpool.impl.WorkerThread;

/**
 * @author Mauro n Paul
 */
public interface ThreadPoolMonitor {
    void newThreadPool(String name, int priority, boolean daemon, int maxActive, int maxIdle);

    void unexpectedThrowable(String reference, Throwable t);

    void threadRetrieved(WorkerThread worker);

    void threadReturned(WorkerThread worker);

    void threadCreated(WorkerThread worker);

    void threadDisposing(WorkerThread worker);
}
