/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.transport;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * An underlying transport layer that uses TCP/IP.
 *
 * @author Peter Donald
 * @version $Revision: 1.5 $ $Date: 2003-12-05 06:57:12 $
 */
public class TcpTransport
{
    /** The key used to register channel in selector. */
    private SelectionKey m_key;

    /** The associated channel. */
    private final SocketChannel m_channel;

    /** The buffer used to store incoming data. */
    private final CircularBuffer m_readBuffer;

    /** The buffer used to store outgoing data. */
    private final CircularBuffer m_writeBuffer;

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
        if( null == channel )
        {
            throw new NullPointerException( "channel" );
        }
        m_channel = channel;
        m_readBuffer = new CircularBuffer( readBufferSize );
        m_writeBuffer = new CircularBuffer( writeBufferSize );
    }

    /**
     * Set the SelectionKey.
     *
     * @param key the SelectionKey.
     */
    public void setKey( final SelectionKey key )
    {
        m_key = key;
    }

    /**
     * Return the SelectionKey.
     *
     * @return the SelectionKey.
     */
    public SelectionKey getKey()
    {
        return m_key;
    }

    /**
     * Return the operations transport can is waiting on. The value is the AND
     * of {@link SelectionKey#OP_WRITE} and {@link SelectionKey#OP_READ} masks.
     *
     * @return the operations transport will wait on.
     */
    public int getSelectOps()
    {
        int ops = 0;
        if( m_writeBuffer.getAvailable() > 0 )
        {
            ops |= SelectionKey.OP_WRITE;
        }
        if( m_readBuffer.getSpace() > 0 )
        {
            ops |= SelectionKey.OP_READ;
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
        return m_channel;
    }

    /**
     * Return the read buffer.
     *
     * @return the read buffer.
     */
    public CircularBuffer getReadBuffer()
    {
        return m_readBuffer;
    }

    /**
     * Return the write buffer.
     *
     * @return the write buffer.
     */
    public CircularBuffer getWriteBuffer()
    {
        return m_writeBuffer;
    }

    /**
     * Close the channel and disconnect the key.
     */
    public void close()
    {
        m_key = null;
        if( m_channel.isOpen() )
        {
            try
            {

                m_channel.close();
            }
            catch( final IOException ioe )
            {
                //Ignore
            }
        }
    }
}
