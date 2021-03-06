package org.codehaus.spice.netserve.connection.handlers;

import java.net.Socket;
import org.codehaus.spice.netserve.connection.RequestHandler;

/**
 * A handler that allows user to handle ConnectionHandlers.
 * Subclasses may pool or create transient handlers etc.
 */
public abstract class ManagedRequestHandler
    extends AbstractRequestHandler
{
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
        final RequestHandler handler = aquireHandler( socket );
        try
        {
            handler.handleConnection( socket );
        }
        finally
        {
            releaseHandler( handler );
        }
    }

    /**
     * Retrieve the underlying handler.
     *
     * @param socket the socket
     * @return the RequestHandler
     */
    protected abstract RequestHandler aquireHandler( Socket socket );

    /**
     * Release the underlying handler.
     *
     * @param handler the handler
     */
    protected abstract void releaseHandler( RequestHandler handler );
}
