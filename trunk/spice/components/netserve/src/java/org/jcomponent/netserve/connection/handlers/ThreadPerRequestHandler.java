/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.handlers;

import java.net.Socket;
import org.jcomponent.netserve.connection.ConnectionHandler;
import org.jcomponent.threadpool.ThreadPool;

/**
 * A Handler that uses a thread from a pool for each different request.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-10-24 08:21:40 $
 */
public class ThreadPerRequestHandler
    extends AbstractRequestHandler
{
    /**
     * The underlying handler to delegate to.
     */
    private final ConnectionHandler m_handler;

    /**
     * the thread pool that used to handle requests.
     */
    private final ThreadPool m_threadPool;

    /**
     * Create handler.
     *
     * @param handler the underlying handler
     * @param threadPool the thread pool to use to create handler threads
     */
    public ThreadPerRequestHandler( final ConnectionHandler handler,
                                    final ThreadPool threadPool )
    {
        if( null == handler )
        {
            throw new NullPointerException( "handler" );
        }
        if( null == threadPool )
        {
            throw new NullPointerException( "threadPool" );
        }
        m_handler = handler;
        m_threadPool = threadPool;
    }

    /**
     * Execute each request in a separate thread.
     *
     * @param socket the socket to handle
     */
    public void handleConnection( final Socket socket )
    {
        final Runnable runnable = createRunnable( socket );
        m_threadPool.execute( runnable );
    }

    /**
     * Delegate request to supplied handler.
     *
     * @param socket the socket
     * @throws Exception on error
     */
    protected void doPerformRequest( final Socket socket )
        throws Exception
    {
        m_handler.handleConnection( socket );
    }
}
