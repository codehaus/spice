/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netevent.transport;

import java.io.IOException;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.spi.AbstractSelectableChannel;
import org.codehaus.spice.event.impl.collections.Buffer;
import org.codehaus.spice.netevent.buffers.BufferManager;
import org.codehaus.spice.netevent.selector.SocketEventSource;

/**
 * An underlying transport layer that uses TCP/IP.
 * 
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2004-01-12 02:32:41 $
 */
public class ChannelTransport
{
    /** The associated channel. */
    private final Channel m_channel;

    /** The buffer used to store outgoing data. */
    private final Buffer m_transmitBuffer;

    /** The Stream representing data received from channel. */
    private final MultiBufferInputStream m_receivedData;

    /** The key used to register channel in selector. */
    private SelectionKey m_key;

    /** The associated user data. */
    private Object m_userData;

    /**
     * Create transport.
     * 
     * @param channel the underlying channel
     * @param transmitBuffer the transmit buffer
     */
    public ChannelTransport( final Channel channel,
                             final Buffer transmitBuffer,
                             final BufferManager bufferManager )
    {
        if( null == channel )
        {
            throw new NullPointerException( "channel" );
        }
        if( null == transmitBuffer )
        {
            throw new NullPointerException( "transmitBuffer" );
        }
        m_channel = channel;
        m_transmitBuffer = transmitBuffer;
        m_receivedData = new MultiBufferInputStream( bufferManager );
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
     * Return the user data associated with transport.
     * 
     * @return the user data associated with transport.
     */
    public Object getUserData()
    {
        return m_userData;
    }

    /**
     * Set the user data associated with transport.
     * 
     * @param userData the user data associated with transport.
     */
    public void setUserData( final Object userData )
    {
        m_userData = userData;
    }

    /**
     * Return the operations transport can is waiting on. The value is the AND
     * of {@link SelectionKey#OP_WRITE} and {@link SelectionKey#OP_READ} masks.
     * 
     * @return the operations transport will wait on.
     */
    public int getSelectOps()
    {
        int ops = SelectionKey.OP_READ;
        if( getTransmitBuffer().size() > 0 )
        {
            ops |= SelectionKey.OP_WRITE;
        }
        return ops;
    }

    /**
     * Get underlying channel for transport.
     * 
     * @return the channel
     */
    public Channel getChannel()
    {
        return m_channel;
    }

    /**
     * Return the write buffer.
     * 
     * @return the write buffer.
     */
    public Buffer getTransmitBuffer()
    {
        return m_transmitBuffer;
    }

    /**
     * Return the stream containing received data.
     * 
     * @return the stream containing received data.
     */
    public MultiBufferInputStream getReceivedData()
    {
        return m_receivedData;
    }

    /**
     * Register this transport with specified managger and using specified
     * EventHandler.
     * 
     * @param source the source.
     */
    public void register( final SocketEventSource source )
        throws IOException
    {
        final AbstractSelectableChannel channel =
            (AbstractSelectableChannel)getChannel();
        m_key = source.registerChannel( channel,
                                        getSelectOps(),
                                        this );
    }

    /**
     * Reregister key operations. Call this after the transmit buffer has been
     * modified.
     */
    public void reregister()
    {
        if( null != m_key )
        {
            m_key.interestOps( getSelectOps() );
        }
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
