/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netserve.connection.handlers;

import java.net.Socket;
import org.codehaus.spice.netserve.connection.RequestHandler;

/**
 * A simple handler that delegates to another handler.
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-03-21 23:42:58 $
 */
public class DelegatingRequestHandler
    extends AbstractRequestHandler
{
    /**
     * The underlying handler to delegate to.
     */
    private final RequestHandler m_handler;

    /**
     * Create handler.
     *
     * @param handler the handler to delegate to
     */
    public DelegatingRequestHandler( final RequestHandler handler )
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
