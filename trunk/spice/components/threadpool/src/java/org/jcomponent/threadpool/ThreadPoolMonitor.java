package org.jcomponent.threadpool;

/**
 * @author Mauro n Paul
 */
public interface ThreadPoolMonitor
{
   void newThreadPool( String name, int priority, boolean daemon, int maxActive, int maxIdle );

   void unexpectedThrowable( String reference, Throwable t );

   void threadRetrieved( Thread worker );

   void threadReturned( Thread worker );

   void threadCreated( Thread worker );

   void threadDisposing( Thread worker );
}
