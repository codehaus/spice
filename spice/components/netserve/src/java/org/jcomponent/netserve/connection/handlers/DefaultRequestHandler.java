package org.jcomponent.netserve.connection.handlers;

import java.io.IOException;
import java.net.ProtocolException;
import java.net.Socket;
import org.jcomponent.netserve.connection.ConnectionHandler;
import org.jcomponent.netserve.connection.ConnectionHandlerManager;

/**
 * A default request handler that just handles
 * the connection.
 */
public abstract class DefaultRequestHandler
    implements ConnectionHandler
{
    /**
     * the Manager responsible for creating/managing
     * all handlers.
     */
    private final ConnectionHandlerManager _handlerManager;

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
        _handlerManager = handlerManager;
    }

    /**
     * Handle a connection.
     *
     * @param socket the socket
     * @throws IOException if an error occurs
     */
    public void handleConnection( Socket socket )
        throws IOException, ProtocolException
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
        final String name = getThreadName( socket );
        Thread.currentThread().setName( name );

        ConnectionHandler handler = null;
        try
        {
            handler = _handlerManager.aquireHandler();
            handler.handleConnection( socket );
        }
        catch( final Throwable t )
        {
            errorHandlingConnection( socket, t );
        }
        finally
        {
            if( null != handler )
            {
                _handlerManager.releaseHandler( handler );
            }
            endConnection( socket );
        }
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
