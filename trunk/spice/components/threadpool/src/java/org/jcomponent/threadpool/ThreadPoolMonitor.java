package org.jcomponent.threadpool;

/**
 * @author Mauro n Paul
 */
public interface ThreadPoolMonitor
{
   void newThreadPool( String name,
                       int priority,
                       boolean daemon,
                       int maxActive,
                       int maxIdle );

   void unexpectedError( String message,
                             Throwable t );

   void threadRetrieved( Thread thread );

   void threadReturned( Thread thread );

   void threadCreated( Thread thread );

   void threadDisposing( Thread thread );
}
