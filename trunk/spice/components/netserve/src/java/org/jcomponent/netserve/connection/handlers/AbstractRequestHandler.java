/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.handlers;

import org.jcomponent.netserve.connection.ConnectionHandler;
import java.net.Socket;
import java.io.IOException;

/**
 * Abstract base class for request handlers.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-10-25 22:29:49 $
 */
public abstract class AbstractRequestHandler
    implements ConnectionHandler
{
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
     * Perform the request for socket by delegating to
     * underlying handler.
     *
     * @param socket the socket to handle
     */
    protected void performRequest( final Socket socket )
    {
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
        return "RequestHandler for " +
            socket.getInetAddress().getHostAddress() + ":" +
            socket.getPort();
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
