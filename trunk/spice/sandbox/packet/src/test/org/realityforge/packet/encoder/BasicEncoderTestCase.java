/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.encoder;

import java.nio.ByteBuffer;
import junit.framework.TestCase;
import org.realityforge.packet.Packet;

/**
 * @author Peter Donald
 * @version $Revision: 1.5 $ $Date: 2003-12-05 06:57:12 $
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
        data.position( 2 );

        final Packet packet = new Packet( (short)sequence, 0, data );
        final BasicEncoder encoder = new BasicEncoder();

        final ByteBuffer output = ByteBuffer.allocate( 50 );
        output.position( 23 );
        final boolean result = encoder.encode( packet, output );
        assertEquals( "encoded?", true, result );
        assertEquals( "output.position()",
                      23 + BasicEncoder.SIZEOF_HEADER + dataSize,
                      output.position() );
        assertEquals( "output.getShort( 23 )",
                      dataSize,
                      output.getShort( 23 ) );
        assertEquals( "output.getShort( 25 )",
                      sequence,
                      output.getShort( 25 ) );
        assertEquals( "output.getShort( 27 )", 'B', output.get( 27 ) );
        assertEquals( "output.getShort( 28 )", 'i', output.get( 28 ) );
        assertEquals( "output.getShort( 29 )", 'n', output.get( 29 ) );
        assertEquals( "output.getShort( 30 )", 'g', output.get( 30 ) );
    }

    public void testEncodePacketThatNoFit()
        throws Exception
    {
        final ByteBuffer data = ByteBuffer.allocate( 5 );
        data.clear();
        data.put( (byte)'B' );
        data.put( (byte)'i' );
        data.put( (byte)'n' );
        data.put( (byte)'g' );
        data.flip();
        data.position( 2 );

        final Packet packet = new Packet( (short)2, 0, data );
        final BasicEncoder encoder = new BasicEncoder();

        final ByteBuffer output = ByteBuffer.allocate( 50 );
        output.position( 48 );
        final boolean result = encoder.encode( packet, output );
        assertEquals( "encoded?", false, result );
        assertEquals( "output.position()", 48, output.position() );
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

    public void testDecodePacket()
        throws Exception
    {
        final ByteBuffer input = ByteBuffer.allocate( 40 );
        input.clear();
        input.limit( 13 );
        input.position( 0 );
        input.putShort( (short)2 );
        input.putShort( (short)23 );
        input.put( (byte)'H' );
        input.put( (byte)'i' );

        //The next set of data defines data still encoded
        input.putShort( (short)3 );
        input.putShort( (short)24 );
        input.put( (byte)'B' );
        input.put( (byte)'a' );
        input.put( (byte)'i' );
        input.position( 2 );

        final BasicEncoder encoder = new BasicEncoder();
        final Packet packet = encoder.decode( input );
        assertNotNull( "packet", packet );

        assertEquals( "packet.getSequence()", 23, packet.getSequence() );
        final ByteBuffer data = packet.getData();
        assertEquals( "data.limit()", 2, data.limit() );
        assertEquals( "data.position()", 0, data.position() );
        assertEquals( "data.get( 0 )", 'H', data.get( 0 ) );
        assertEquals( "data.get( 1 )", 'i', data.get( 1 ) );

        assertEquals( "input.position()", 0, input.position() );
        assertEquals( "input.limit()", 7, input.limit() );
        assertEquals( "input.getShort( 0 )", 3, input.getShort( 0 ) );
        assertEquals( "input.getShort( 2 )", 24, input.getShort( 2 ) );
        assertEquals( "input.get( 4 )", 'B', input.get( 4 ) );
        assertEquals( "input.get( 5 )", 'a', input.get( 5 ) );
        assertEquals( "input.get( 6 )", 'i', input.get( 6 ) );
    }
}
