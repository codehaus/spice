/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
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
   public void newThreadPool( final String name,
                              final int priority,
                              final boolean daemon,
                              final int maxActive,
                              final int maxIdle )
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

   public void unexpectedError( final String message,
                                    final Throwable t )
   {
      if ( getLogger().isWarnEnabled() )
      {
         getLogger().warn( "Unexpected Error (" + message + ")", t );
      }
   }

   public void threadRetrieved( final Thread thread )
   {
      if ( getLogger().isDebugEnabled() )
      {
         getLogger().debug( "Thread retrieved - " + thread.getName() );
      }
   }

   public void threadReturned( final Thread thread )
   {
      if ( getLogger().isDebugEnabled() )
      {
         getLogger().debug( "Thread returned - " + thread.getName() );
      }
   }

   public void threadCreated( final Thread thread )
   {
      if ( getLogger().isInfoEnabled() )
      {
         getLogger().info( "Thread Created - " + thread.getName() );
      }
   }

   public void threadDisposing( final Thread thread )
   {
      if ( getLogger().isInfoEnabled() )
      {
         getLogger().info( "Thread Disposing - " + thread.getName() );
      }
   }
}
