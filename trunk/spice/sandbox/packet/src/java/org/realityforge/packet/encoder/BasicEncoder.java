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
 * A basic encoder that writes packet using simple mechanisms.
 *
 * @author Peter Donald
 * @version $Revision: 1.8 $ $Date: 2003-12-05 06:57:11 $
 */
public class BasicEncoder
    implements Encoder
{
    /** The number of bytes in packets header. */
    static final int SIZEOF_HEADER = 2 + 2;

    /**
     * @see Encoder#encode
     */
    public boolean encode( final Packet packet,
                           final ByteBuffer output )
    {
        final int space = output.limit() - output.position();
        final ByteBuffer data = packet.getData();
        final int size = data.limit();
        if( space < SIZEOF_HEADER + size )
        {
            return false;
        }
        output.putShort( (short)size );
        output.putShort( packet.getSequence() );
        output.put( data.array(), 0, size );
        return true;
    }

    /**
     * @see Encoder#decode
     */
    public Packet decode( final ByteBuffer input )
    {
        final int size = input.limit();
        if( size < SIZEOF_HEADER )
        {
            return null;
        }
        final int index = input.position();
        input.position( 0 );
        final short messageSize = input.getShort();
        final short sequence = input.getShort();
        final int available = input.remaining();
        if( available >= messageSize )
        {
            final ByteBuffer byteBuffer = ByteBuffer.allocate( messageSize );
            byteBuffer.limit( messageSize );
            byteBuffer.position( 0 );
            System.arraycopy( input.array(), SIZEOF_HEADER,
                              byteBuffer.array(), 0,
                              messageSize );
            final int end = messageSize + SIZEOF_HEADER;
            input.position( end );
            input.compact();
            input.position( 0 );
            input.limit( size - end );
            return new Packet( sequence, 0, byteBuffer );
        }
        else
        {
            input.position( index );
            return null;
        }
    }
}
