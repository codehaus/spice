package org.jcomponent.threadpool.impl;

import org.jcomponent.threadpool.ThreadPoolMonitor;

/**
 * @author Mauro n Paul
 */
public class NullThreadPoolMonitor
   implements ThreadPoolMonitor
{
   public void newThreadPool( final String name,
                              final int priority,
                              final boolean daemon,
                              final int maxActive,
                              final int maxIdle )
   {
   }

   public void unexpectedError( final String message,
                                final Throwable t )
   {
   }

   public void threadRetrieved( final Thread thread )
   {
   }

   public void threadReturned( final Thread thread )
   {
   }

   public void threadCreated( final Thread thread )
   {
   }

   public void threadDisposing( final Thread thread )
   {
   }
}
