/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.encoder;

import java.nio.ByteBuffer;
import org.realityforge.packet.Packet;

/**
 * The Encoder is responsible for encoding and decoding Packets to ByteBuffers.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-11-26 02:14:04 $
 */
public interface Encoder
{
    /**
     * Encode the packet into ByteBuffer if there is enough space.
     *
     * @param packet the packet
     * @param output the output buffer
     * @return true if packet encoded, false otherwise
     */
    boolean encode( Packet packet, ByteBuffer output );

    /**
     * Decode packet from ByteBuffer if enough data is present.
     *
     * @param input the input buffer
     * @return the Packet if decoded else null
     */
    Packet decode( ByteBuffer input );
}