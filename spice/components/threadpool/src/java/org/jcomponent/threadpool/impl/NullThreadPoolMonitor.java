/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
 package org.jcomponent.threadpool.impl;

/**
 * No-op implementation of ThreadPoolMonitor.
 * 
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @author <a href="mailto:paul_hammant at yahoo.com">Paul Hammant</a>
 */
class NullThreadPoolMonitor
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
