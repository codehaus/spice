/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.protocol;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

/**
 * A helper class for writing writing protocol to channel. Note that this class
 * is single threaded as it uses a shared message buffer.
 *
 * @author Peter Donald
 * @version $Revision: 1.4 $ $Date: 2003-12-05 06:57:12 $
 */
class ProtocolWriter
{
    /**
     * The sizeof the connected message.
     *
     * @see ProtocolConstants#S2C_CONNECTED
     */
    static final int SIZEOF_CONNECTED_MSG = 1 + 2 + 2;

    /** The maximum size of any control message. */
    static final int MAX_CONTROL_SIZE =
        Math.max( ProtocolConstants.MAGIC.length, SIZEOF_CONNECTED_MSG );

    /** Internal message buffer used to send control messages. */
    private final ByteBuffer m_message =
        ByteBuffer.allocateDirect( MAX_CONTROL_SIZE );

    /**
     * Send a single byte control message to channel.
     *
     * @param sessionID the SessionID
     * @param sessionAuth the SessionAuth
     * @param channel the channel
     * @throws IOException if error writing to channel
     * @throws BufferOverflowException if can not write whole control to
     * channel
     */
    void sendConnectedMessage( final short sessionID,
                               final short sessionAuth,
                               final WritableByteChannel channel )
        throws IOException
    {
        m_message.clear();
        m_message.put( ProtocolConstants.S2C_CONNECTED );
        m_message.putShort( sessionID );
        m_message.putShort( sessionAuth );
        m_message.flip();
        forceWriteMessage( m_message, channel );
    }

    /**
     * Send a single byte control message to channel.
     *
     * @param control the control code
     * @param channel the channel
     * @throws IOException if error writing to channel
     * @throws BufferOverflowException if can not write whole control to
     * channel
     */
    void sendControlMessage( final byte control,
                             final WritableByteChannel channel )
        throws IOException
    {
        m_message.clear();
        m_message.put( control );
        m_message.flip();
        forceWriteMessage( m_message, channel );
    }

    /**
     * Write message to specified channel. If channel can not be written to then
     * a BufferOverflowException is thrown.
     *
     * @param message the message
     * @param channel the channel
     * @throws IOException if error writing to channel
     * @throws BufferOverflowException if can not write whole message to
     * channel
     */
    void forceWriteMessage( final ByteBuffer message,
                            final WritableByteChannel channel )
        throws IOException
    {
        final int available = message.remaining();
        final int writeCount = channel.write( message );
        if( available != writeCount )
        {
            throw new BufferOverflowException();
        }
    }
}
