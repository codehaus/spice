package org.jcomponent.netserve.connection.handlers;

import java.net.Socket;
import org.jcomponent.netserve.connection.ConnectionHandler;
import org.jcomponent.netserve.connection.ConnectionHandlerManager;

/**
 * A default request handler that just handles
 * the connection.
 */
public abstract class DefaultRequestHandler
    extends AbstractRequestHandler
{
    /**
     * the Manager responsible for creating/managing
     * all handlers.
     */
    private final ConnectionHandlerManager m_handlerManager;

    /**
     * Create a new RequestHandler.
     *
     * @param handlerManager the handlerManager
     */
    public DefaultRequestHandler( final ConnectionHandlerManager handlerManager )
    {
        if( null == handlerManager )
        {
            throw new NullPointerException( "handlerManager" );
        }
        m_handlerManager = handlerManager;
    }

    /**
     * Actually handle the request.
     * Assume that the caller will gracefully
     * handle unexpected exceptions and shutdown
     * the socket when this method returns.
     *
     * @param socket the socket
     * @throws Exception if an erro roccurs
     */
    protected void doPerformRequest( final Socket socket )
        throws Exception
    {
        final ConnectionHandler handler = m_handlerManager.aquireHandler();
        try
        {
            handler.handleConnection( socket );
        }
        finally
        {
            m_handlerManager.releaseHandler( handler );
        }
    }
}
