/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet;

import java.nio.ByteBuffer;

/**
 * The Packet class represents a chunk of data sent across network.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-11-11 09:32:07 $
 */
public class Packet
{
    /**
     * The sequence ID of packet.
     */
    private final short m_sequence;

    /**
     * The packet flags. Indicate things
     * like whether the packet needs to be
     * acked etc. Some encoder/decoders may
     * specify the flags as always 0.
     */
    private final int m_flags;

    /**
     * The buffer containing data contained within packet.
     */
    private final ByteBuffer m_data;

    /**
     * Create packet.
     *
     * @param sequence the sequence
     * @param flags the flags
     * @param data the data
     */
    public Packet( final short sequence,
                   final int flags,
                   final ByteBuffer data )
    {
        if( null == data )
        {
            throw new NullPointerException( "data" );
        }
        m_sequence = sequence;
        m_flags = flags;
        m_data = data;
    }

    /**
     * Return the packet sequence number.
     *
     * @return the packet sequence number.
     */
    public short getSequence()
    {
        return m_sequence;
    }

    /**
     * Return the packet flags.
     *
     * @return the packet flags.
     */
    public int getFlags()
    {
        return m_flags;
    }

    /**
     * Return the data containined within packet.
     * Note that the position may not be set to start
     * of packet.
     *
     * @return the data containined within packet.
     */
    public ByteBuffer getData()
    {
        return m_data;
    }
}
