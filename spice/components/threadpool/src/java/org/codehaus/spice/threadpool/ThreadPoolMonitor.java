/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
 package org.codehaus.spice.threadpool;

/**
 * Monitor interface for ThreadPool.  
 * Provides a facade to support different types of events, including logging.
 * 
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @author <a href="mailto:paul_hammant at yahoo.com">Paul Hammant</a>
 */
public interface ThreadPoolMonitor
{
   void newThreadPool( String name,
                       int priority,
                       boolean daemon,
                       int maxActive,
                       int maxIdle );

   void threadRetrieved( Thread thread );

   void threadReturned( Thread thread );

   void threadCreated( Thread thread );

   void threadDisposing( Thread thread );

   void unexpectedError( String message,
                          Throwable t );

}
