package org.realityforge.packet.transport;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * An underlying transport layer that uses TCP/IP.
 * 
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-11-24 05:25:30 $
 */
public class TcpTransport
{
    /** The key used to register channel in selector. */
    private SelectionKey _key;

    /** The associated channel. */
    private final SocketChannel _channel;

    /** The buffer used to store incoming data. */
    private final CircularBuffer _readBuffer;

    /** The buffer used to store outgoing data. */
    private final CircularBuffer _writeBuffer;

    /**
     * Create transport.
     * 
     * @param channel the underlying channel
     * @param readBufferSize the size of the read buffer
     * @param writeBufferSize the size of the write buffer
     */
    public TcpTransport( final SocketChannel channel,
                         final int readBufferSize,
                         final int writeBufferSize )
    {
        _channel = channel;
        _readBuffer = new CircularBuffer( readBufferSize );
        _writeBuffer = new CircularBuffer( writeBufferSize );
    }

    /**
     * Set the SelectionKey.
     * 
     * @param key the SelectionKey.
     */
    public void setKey( final SelectionKey key )
    {
        _key = key;
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
     * Return the operations transport can is waiting on. The value is the AND
     * of {@link SelectionKey#OP_WRITE} and {@link SelectionKey#OP_READ}.
     * 
     * @return the operations transport will wait on.
     */
    public int getSelectOps()
    {
        int ops = 0;
        if( _writeBuffer.getAvailable() > 0 )
        {
            ops = SelectionKey.OP_WRITE;
        }
        if( _readBuffer.getSpace() > 0 )
        {
            ops = SelectionKey.OP_READ;
        }
        return ops;
    }

    /**
     * Get underlying channel for transport.
     * 
     * @return the channel
     */
    public SocketChannel getChannel()
    {
        return _channel;
    }

    /**
     * Return the read buffer.
     * 
     * @return the read buffer.
     */
    public CircularBuffer getReadBuffer()
    {
        return _readBuffer;
    }

    /**
     * Return the write buffer.
     * 
     * @return the write buffer.
     */
    public CircularBuffer getWriteBuffer()
    {
        return _writeBuffer;
    }

    /**
     * Close the channel and disconnect the key.
     */
    public void close()
    {
        if( _channel.isOpen() )
        {
            try
            {

                _channel.close();
            }
            catch( final IOException ioe )
            {
                //Ignore
            }
        }
    }
}
