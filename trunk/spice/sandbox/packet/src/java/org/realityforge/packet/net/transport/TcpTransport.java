/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.net.transport;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import org.realityforge.sca.selector.SelectorEventHandler;
import org.realityforge.sca.selector.SelectorManager;

/**
 * An underlying transport layer that uses TCP/IP.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-07 02:40:44 $
 */
public class TcpTransport
{
    /** The key used to register channel in selector. */
    private SelectionKey m_key;

    /** The associated channel. */
    private final SocketChannel m_channel;

    /** The buffer used to store incoming data. */
    private final CircularBuffer m_receiveBuffer;

    /** The buffer used to store outgoing data. */
    private final CircularBuffer m_transmitBuffer;

    /**
     * Create transport.
     * 
     * @param channel the underlying channel
     * @param receiveBufferSize the size of the read buffer
     * @param transmitBufferSize the size of the write buffer
     */
    public TcpTransport( final SocketChannel channel,
                         final int receiveBufferSize,
                         final int transmitBufferSize )
    {
        if( null == channel )
        {
            throw new NullPointerException( "channel" );
        }
        m_channel = channel;
        m_receiveBuffer = new CircularBuffer( receiveBufferSize );
        m_transmitBuffer = new CircularBuffer( transmitBufferSize );
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
        if( getTransmitBuffer().getAvailable() > 0 )
        {
            ops |= SelectionKey.OP_WRITE;
        }
        if( getReceiveBuffer().getSpace() > 0 )
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
    public CircularBuffer getReceiveBuffer()
    {
        return m_receiveBuffer;
    }

    /**
     * Return the write buffer.
     * 
     * @return the write buffer.
     */
    public CircularBuffer getTransmitBuffer()
    {
        return m_transmitBuffer;
    }

    /**
     * Register this transport with specified managger and using specified
     * EventHandler.
     * 
     * @param selectorManager the manager.
     * @param eventHandler the eventHandler
     */
    public void register( final SelectorManager selectorManager,
                          final SelectorEventHandler eventHandler )
        throws IOException
    {
        final SelectionKey key =
            selectorManager.registerChannel( getChannel(),
                                             getSelectOps(),
                                             eventHandler,
                                             this );
        m_key = key;
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
