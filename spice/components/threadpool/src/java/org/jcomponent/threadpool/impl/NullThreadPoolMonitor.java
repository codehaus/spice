package org.jcomponent.threadpool.impl;

import org.jcomponent.threadpool.ThreadPoolMonitor;

/**
 * @author Mauro n Paul
 */
public class NullThreadPoolMonitor
   implements ThreadPoolMonitor
{
   public void newThreadPool( String name, int priority, boolean daemon, int maxActive, int maxIdle )
   {
   }

   public void unexpectedThrowable( String reference, Throwable t )
   {
   }

   public void threadRetrieved( Thread worker )
   {
   }

   public void threadReturned( Thread worker )
   {
   }

   public void threadCreated( Thread worker )
   {
   }

   public void threadDisposing( Thread worker )
   {
   }
}
