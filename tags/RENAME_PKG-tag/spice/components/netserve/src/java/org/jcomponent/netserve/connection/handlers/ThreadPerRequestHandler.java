/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.handlers;

import java.net.Socket;
import java.util.Map;
import java.util.Hashtable;
import java.util.Collection;
import org.jcomponent.netserve.connection.RequestHandler;
import org.jcomponent.threadpool.ThreadPool;
import org.jcomponent.threadpool.ThreadControl;

/**
 * A Handler that uses a thread from a pool for each different request.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.6 $ $Date: 2003-10-29 03:51:58 $
 */
public class ThreadPerRequestHandler
    extends DelegatingRequestHandler
{
    /**
     * A map of Socket->ThreadControl.
     */
    private final Map m_controlMap = new Hashtable();

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
    public ThreadPerRequestHandler( final RequestHandler handler,
                                    final ThreadPool threadPool )
    {
        super( handler );
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
        final ThreadControl control = m_threadPool.execute( runnable );
        m_controlMap.put( socket, control );
    }

    /**
     * Remove ThreadControl from list of active threads.
     *
     * @param socket the socket
     */
    protected void endConnection( Socket socket )
    {
        m_controlMap.remove( socket );
        super.endConnection( socket );
    }

    /**
     * Shutdown all requests including those executing in thread pool.
     *
     * @param timeout the timeout
     */
    public void shutdown( final long timeout )
    {
        markAsShutdown();
        final ThreadControl[] controls;
        synchronized( m_controlMap )
        {
            final Collection collection = m_controlMap.values();
            controls = (ThreadControl[])collection.
                toArray( new ThreadControl[ collection.size() ] );
        }
        for( int i = 0; i < controls.length; i++ )
        {
            final ThreadControl control = controls[ i ];
            if( !control.isFinished() )
            {
                control.interrupt();
            }
        }
        super.shutdown( timeout );
        for( int i = 0; i < controls.length; i++ )
        {
            final ThreadControl control = controls[ i ];
            if( !control.isFinished() )
            {
                try
                {
                    control.join( timeout );
                }
                catch( final InterruptedException ie )
                {
                    //Ignore
                }
            }
        }
    }
}
