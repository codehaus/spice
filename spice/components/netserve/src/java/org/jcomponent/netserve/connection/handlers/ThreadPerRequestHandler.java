/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.handlers;

import org.jcomponent.netserve.connection.ConnectionHandlerManager;
import org.jcomponent.threadpool.ThreadPool;
import java.net.Socket;

/**
 * A Handler that uses a thread from a pool for each different request.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-10-24 08:10:58 $
 */
public class ThreadPerRequestHandler
    extends DefaultRequestHandler
{
    /**
     * the thread pool that used to handle requests.
     */
    private final ThreadPool m_threadPool;

    /**
     * Create handler.
     *
     * @param handlerManager the underlying handler
     * @param threadPool the thread pool to use to create handler threads
     */
    public ThreadPerRequestHandler( final ConnectionHandlerManager handlerManager,
                                    final ThreadPool threadPool )
    {
        super( handlerManager );
        if( null == threadPool )
        {
            throw new NullPointerException( "threadPool" );
        }
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
}
