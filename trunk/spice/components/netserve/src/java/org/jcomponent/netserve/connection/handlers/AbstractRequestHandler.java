/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.handlers;

import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import org.jcomponent.netserve.connection.RequestHandler;

/**
 * Abstract base class for request handlers.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.5 $ $Date: 2003-10-28 06:28:54 $
 */
public abstract class AbstractRequestHandler
    implements RequestHandler
{
    /**
     * The set of active requests.
     */
    private final Set m_activeRequests = new HashSet();

    /**
     * Handle a connection.
     *
     * @param socket the socket
     */
    public void handleConnection( Socket socket )
    {
        performRequest( socket );
    }

    /**
     * @see RequestHandler#shutdown
     */
    public void shutdown( final long timeout )
    {
        final Thread[] threads;
        synchronized( this )
        {
            threads = (Thread[])m_activeRequests.
                toArray( new Thread[ m_activeRequests.size() ] );
        }
        for( int i = 0; i < threads.length; i++ )
        {
            final Thread thread = threads[ i ];
            thread.interrupt();
        }
        final long now = System.currentTimeMillis();
        final long then = now + timeout;

        while( System.currentTimeMillis() < then || 0 == timeout )
        {
            synchronized( this )
            {
                if( 0 == m_activeRequests.size() )
                {
                    return;
                }
                try
                {
                    wait( timeout );
                }
                catch( final InterruptedException ie )
                {
                    //Ignore
                }
            }
        }
    }

    /**
     * Perform the request for socket by delegating to
     * underlying handler.
     *
     * @param socket the socket to handle
     */
    protected void performRequest( final Socket socket )
    {
        synchronized( this )
        {
            m_activeRequests.add( Thread.currentThread() );
        }
        setupThreadName( socket );
        try
        {
            doPerformRequest( socket );
        }
        catch( final Throwable t )
        {
            errorHandlingConnection( socket, t );
        }
        finally
        {
            endConnection( socket );
            synchronized( this )
            {
                m_activeRequests.remove( Thread.currentThread() );
                notifyAll();
            }
        }
    }

    /**
     * Method implemented to actually do the work.
     *
     * @param socket the socket
     * @throws Exception if an error occurs
     */
    protected abstract void doPerformRequest( Socket socket )
        throws Exception;

    /**
     * Setup the name of the thread.
     *
     * @param socket the socket associated with request
     */
    protected void setupThreadName( final Socket socket )
    {
        final String name = getThreadName( socket );
        Thread.currentThread().setName( name );
    }

    /**
     * End connection for socket.
     *
     * @param socket the socket
     */
    protected void endConnection( final Socket socket )
    {
        if( socket.isConnected() )
        {
            try
            {
                socket.close();
            }
            catch( final IOException ioe )
            {
                errorClosingConnection( socket, ioe );
            }
        }
    }

    /**
     * Create Runnable to perform the request.
     *
     * @param socket the socket to handle
     * @return thee runnable
     */
    protected Runnable createRunnable( final Socket socket )
    {
        return new Runnable()
        {
            public void run()
            {
                performRequest( socket );
            }
        };
    }

    /**
     * Return the name should be set for current thread.
     *
     * @param socket the socket being handled in thread
     * @return the thread name.
     */
    protected String getThreadName( final Socket socket )
    {
        if( socket.isConnected() )
        {
            return "RequestHandler for " +
                socket.getInetAddress().getHostAddress() + ":" +
                socket.getPort();
        }
        else
        {
            return "RequestHandler for " + socket;
        }
    }

    /**
     * Notify handler of an error handling socket.
     *
     * @param socket the socket
     * @param t the error
     */
    protected void errorHandlingConnection( final Socket socket,
                                            final Throwable t )
    {
    }

    /**
     * Notify handler of an error closing socket.
     *
     * @param socket the socket
     * @param t the error
     */
    protected void errorClosingConnection( final Socket socket,
                                           final Throwable t )
    {
        errorHandlingConnection( socket, t );
    }
}
