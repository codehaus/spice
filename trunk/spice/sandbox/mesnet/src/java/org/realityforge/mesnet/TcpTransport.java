package org.realityforge.mesnet;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.channels.SelectableChannel;
import java.io.IOException;

/**
 * An underlying transport layer that uses TCP/IP.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-11-10 05:54:18 $
 */
public class TcpTransport
{
    /**
     * The key used to register channel in selector.
     */
    private final SelectionKey _key;

    /**
     * The associated channel.
     */
    private final SocketChannel _channel;

    /**
     * The associated session if any.
     */
    private Session _session;

    /**
     * Create transport.
     *
     * @param key the key
     */
    public TcpTransport( final SelectionKey key )
    {
        _key = key;
        _channel = (SocketChannel)key.channel();
    }

    /**
     * Return the SelectionKey.
     *
     * @return the SelectionKey.
     */
    public SelectionKey getKey()
    {
        return _key;
    }

    /**
     * Get underlying channel for transport.
     *
     * @return the transport
     */
    public SocketChannel getChannel()
    {
        return _channel;
    }

    /**
     * Get session associated with transport.
     *
     * @return the session
     */
    public Session getSession()
    {
        return _session;
    }

    /**
     * Associate a session with transport.
     *
     * @param session the session
     */
    public void setSession( final Session session )
    {
        _session = session;
    }

    /**
     * Close the channel and disconnect the key.
     */
    public void close()
    {
        final SelectableChannel channel = _key.channel();
        _key.attach( null );
        _key.cancel();
        try
        {
            channel.close();
        }
        catch( final IOException ioe )
        {
            //Ignore
        }
    }
}
