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

/**
 * A simple handler that delegates to another handler.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-26 01:04:18 $
 */
public abstract class DelegatingRequestHandler
    extends AbstractRequestHandler
{
    /**
     * The underlying handler to delegate to.
     */
    private final ConnectionHandler m_handler;

    /**
     * Create handler.
     *
     * @param handler the handler to delegate to
     */
    public DelegatingRequestHandler( final ConnectionHandler handler )
    {
        if( null == handler )
        {
            throw new NullPointerException( "handler" );
        }
        m_handler = handler;
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

    /**
     * @see AbstractRequestHandler#shutdown
     */
    public void shutdown( final long timeout )
    {
        m_handler.shutdown( timeout );
        super.shutdown( timeout );
    }
}
