package org.jcomponent.threadpool.impl;

import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.jcomponent.threadpool.ThreadPoolMonitor;

/**
 * @author Mauro n Paul
 */
public class AvalonThreadPoolMonitor
   extends AbstractLogEnabled
   implements ThreadPoolMonitor
{
   public void newThreadPool( String name, int priority, boolean daemon, int maxActive, int maxIdle )
   {
      if ( getLogger().isInfoEnabled() )
      {
         getLogger().info( "Creating a new ThreadPool " + name +
                           "(priority=" + priority +
                           ",isDaemon=" + daemon + ") with " +
                           "max-threads=" + maxActive + " and " +
                           "max-idle=" + maxIdle );
      }
   }

   public void unexpectedThrowable( String reference, Throwable t )
   {
      if ( getLogger().isErrorEnabled() )
      {

         getLogger().error( "Unexpected Exception (" + reference + ")", t );
      }
   }

   public void threadRetrieved( Thread worker )
   {
      if ( getLogger().isDebugEnabled() )
      {

         getLogger().debug( "Thread retrieved - " + worker.getName() );
      }
   }

   public void threadReturned( Thread worker )
   {
      if ( getLogger().isDebugEnabled() )
      {

         getLogger().debug( "Thread returned - " + worker.getName() );
      }
   }

   public void threadCreated( Thread worker )
   {
      if ( getLogger().isInfoEnabled() )
      {

         getLogger().info( "Thread Created - " + worker.getName() );
      }
   }

   public void threadDisposing( final Thread worker )
   {
      if ( getLogger().isInfoEnabled() )
      {
         getLogger().info( "Thread Disposing - " + worker.getName() );
      }
   }
}
