/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.encoder;

import junit.framework.TestCase;
import java.nio.ByteBuffer;
import org.realityforge.packet.Packet;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-11-11 11:39:30 $
 */
public class BasicEncoderTestCase
    extends TestCase
{
    public void testEncodeSimplePacket()
        throws Exception
    {
        final int sequence = 2;
        final int dataSize = 4;

        final ByteBuffer data = ByteBuffer.allocate( 5 );
        data.clear();
        data.put( (byte)'B' );
        data.put( (byte)'i' );
        data.put( (byte)'n' );
        data.put( (byte)'g' );
        data.flip();
        data.position( sequence );

        final Packet packet = new Packet( (short)sequence, 0, data );
        final BasicEncoder encoder = new BasicEncoder();

        final ByteBuffer output = ByteBuffer.allocate( 50 );
        output.position( 23 );
        final boolean result = encoder.encode( packet, output );
        assertEquals( "encoded?", true, result );
        assertEquals( "output.position()",
                      23 + BasicEncoder.SIZEOF_HEADER + dataSize,
                      output.position() );
        assertEquals( "output.getShort( 23 )", dataSize, output.getShort( 23 ) );
        assertEquals( "output.getShort( 25 )", sequence, output.getShort( 25 ) );
        assertEquals( "output.getShort( 27 )", 'B', output.get( 27 ) );
        assertEquals( "output.getShort( 28 )", 'i', output.get( 28 ) );
        assertEquals( "output.getShort( 29 )", 'n', output.get( 29 ) );
        assertEquals( "output.getShort( 30 )", 'g', output.get( 30 ) );
    }

    public void testEncodePacketThatNoFit()
        throws Exception
    {
        final int sequence = 2;

        final ByteBuffer data = ByteBuffer.allocate( 5 );
        data.clear();
        data.put( (byte)'B' );
        data.put( (byte)'i' );
        data.put( (byte)'n' );
        data.put( (byte)'g' );
        data.flip();
        data.position( sequence );

        final Packet packet = new Packet( (short)sequence, 0, data );
        final BasicEncoder encoder = new BasicEncoder();

        final ByteBuffer output = ByteBuffer.allocate( 50 );
        output.position( 48 );
        final boolean result = encoder.encode( packet, output );
        assertEquals( "encoded?", false, result );
    }

    public void testDecodePacketThatHeaderNoPresent()
        throws Exception
    {
        final ByteBuffer input = ByteBuffer.allocate( 40 );
        input.clear();
        input.limit( 3 );
        input.position( 2 );

        final BasicEncoder encoder = new BasicEncoder();
        final Packet packet = encoder.decode( input );
        assertEquals( "packet?", null, packet );
        assertEquals( "input.position()", 2, input.position() );
        assertEquals( "input.limit()", 3, input.limit() );
    }

    public void testDecodePacketThatDataNoPresent()
        throws Exception
    {
        final ByteBuffer input = ByteBuffer.allocate( 40 );
        input.clear();
        input.limit( 10 );
        input.position( 0 );
        input.putShort( (short)7 );
        input.putShort( (short)23 );
        input.position( 2 );

        final BasicEncoder encoder = new BasicEncoder();
        final Packet packet = encoder.decode( input );
        assertEquals( "packet", null, packet );
        assertEquals( "input.position()", 2, input.position() );
        assertEquals( "input.limit()", 10, input.limit() );
    }
}
