/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netevent.transport;

import java.nio.ByteBuffer;
import junit.framework.TestCase;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-07 03:25:09 $
 */
public class CircularBufferTestCase
    extends TestCase
{
    public void testCreation()
        throws Exception
    {
        final CircularBuffer buffer = new CircularBuffer( 55 );
        assertEquals( "buffer.getAvailable()", 0, buffer.getAvailable() );
        assertEquals( "buffer.getCapacity()", 55, buffer.getCapacity() );
        assertEquals( "buffer.getSpace()", 55, buffer.getSpace() );
        assertEquals( "buffer.getStart()", 0, buffer.getStart() );
        assertEquals( "buffer.getEnd()", 0, buffer.getEnd() );
        assertEquals( "buffer.isWrappedBuffer()", false,
                      buffer.isWrappedBuffer() );
    }

    public void testBufferPostWrite()
        throws Exception
    {
        final CircularBuffer buffer = new CircularBuffer( 55 );
        buffer.writeBytes( 15 );
        assertEquals( "buffer.getAvailable()", 15, buffer.getAvailable() );
        assertEquals( "buffer.getCapacity()", 55, buffer.getCapacity() );
        assertEquals( "buffer.getSpace()", 40, buffer.getSpace() );
        assertEquals( "buffer.getStart()", 0, buffer.getStart() );
        assertEquals( "buffer.getEnd()", 15, buffer.getEnd() );
        assertEquals( "buffer.isWrappedBuffer()", false,
                      buffer.isWrappedBuffer() );
    }

    public void testWriteZero()
        throws Exception
    {
        final CircularBuffer buffer = new CircularBuffer( 55 );
        buffer.writeBytes( 0 );
        assertEquals( "buffer.getAvailable()", 0, buffer.getAvailable() );
        assertEquals( "buffer.getCapacity()", 55, buffer.getCapacity() );
        assertEquals( "buffer.getSpace()", 55, buffer.getSpace() );
        assertEquals( "buffer.getStart()", 0, buffer.getStart() );
        assertEquals( "buffer.getEnd()", 0, buffer.getEnd() );
        assertEquals( "buffer.isWrappedBuffer()", false,
                      buffer.isWrappedBuffer() );
    }

    public void testBufferPostRead()
        throws Exception
    {
        final CircularBuffer buffer = new CircularBuffer( 55 );
        buffer.writeBytes( 15 );
        buffer.readBytes( 5 );
        assertEquals( "buffer.getAvailable()", 10, buffer.getAvailable() );
        assertEquals( "buffer.getCapacity()", 55, buffer.getCapacity() );
        assertEquals( "buffer.getSpace()", 45, buffer.getSpace() );
        assertEquals( "buffer.getStart()", 5, buffer.getStart() );
        assertEquals( "buffer.getEnd()", 15, buffer.getEnd() );
        assertEquals( "buffer.isWrappedBuffer()", false,
                      buffer.isWrappedBuffer() );
    }

    public void testReadZero()
        throws Exception
    {
        final CircularBuffer buffer = new CircularBuffer( 55 );
        buffer.writeBytes( 15 );
        buffer.readBytes( 0 );
        assertEquals( "buffer.getAvailable()", 15, buffer.getAvailable() );
        assertEquals( "buffer.getCapacity()", 55, buffer.getCapacity() );
        assertEquals( "buffer.getSpace()", 40, buffer.getSpace() );
        assertEquals( "buffer.getStart()", 0, buffer.getStart() );
        assertEquals( "buffer.getEnd()", 15, buffer.getEnd() );
        assertEquals( "buffer.isWrappedBuffer()", false,
                      buffer.isWrappedBuffer() );
    }

    public void testWriteWrappedBuffer()
        throws Exception
    {
        final CircularBuffer buffer = new CircularBuffer( 55 );
        buffer.writeBytes( 50 );
        buffer.readBytes( 40 );
        buffer.writeBytes( 20 );
        assertEquals( "buffer.getAvailable()", 30, buffer.getAvailable() );
        assertEquals( "buffer.getCapacity()", 55, buffer.getCapacity() );
        assertEquals( "buffer.getSpace()", 25, buffer.getSpace() );
        assertEquals( "buffer.getStart()", 40, buffer.getStart() );
        assertEquals( "buffer.getEnd()", 15, buffer.getEnd() );
        assertEquals( "buffer.isWrappedBuffer()", true,
                      buffer.isWrappedBuffer() );
    }

    public void testReadWrappedBuffer()
        throws Exception
    {
        final CircularBuffer buffer = new CircularBuffer( 55 );
        buffer.writeBytes( 50 );
        buffer.readBytes( 50 );
        buffer.writeBytes( 20 );
        buffer.readBytes( 10 );
        assertEquals( "buffer.getAvailable()", 10, buffer.getAvailable() );
        assertEquals( "buffer.getCapacity()", 55, buffer.getCapacity() );
        assertEquals( "buffer.getSpace()", 45, buffer.getSpace() );
        assertEquals( "buffer.getStart()", 5, buffer.getStart() );
        assertEquals( "buffer.getEnd()", 15, buffer.getEnd() );
        assertEquals( "buffer.isWrappedBuffer()", false,
                      buffer.isWrappedBuffer() );
    }

    public void testOverflowWriteBytes()
        throws Exception
    {
        final CircularBuffer buffer = new CircularBuffer( 55 );
        try
        {
            buffer.writeBytes( 60 );
        }
        catch( final IllegalArgumentException iae )
        {
            assertEquals( "iae.getMessage()",
                          "60 > 55",
                          iae.getMessage() );
        }
    }

    public void testUnderflowReadBytes()
        throws Exception
    {
        final CircularBuffer buffer = new CircularBuffer( 55 );
        try
        {
            buffer.readBytes( 20 );
        }
        catch( final IllegalArgumentException iae )
        {
            assertEquals( "iae.getMessage()",
                          "20 > 0",
                          iae.getMessage() );
        }
    }

    public void testAsReadBuffers()
        throws Exception
    {
        final CircularBuffer buffer = new CircularBuffer( 55 );
        buffer.writeBytes( 20 );
        buffer.readBytes( 10 );
        final ByteBuffer[] byteBuffers = buffer.asReadBuffers();
        assertEquals( "byteBuffers[ 0 ].position()",
                      10,
                      byteBuffers[ 0 ].position() );
        assertEquals( "byteBuffers[ 0 ].limit()",
                      20,
                      byteBuffers[ 0 ].limit() );
        assertEquals( "byteBuffers[ 1 ].position()",
                      0,
                      byteBuffers[ 1 ].position() );
        assertEquals( "byteBuffers[ 1 ].limit()",
                      0,
                      byteBuffers[ 1 ].limit() );
    }

    public void testAsReadBuffersWithWrap()
        throws Exception
    {
        final CircularBuffer buffer = new CircularBuffer( 55 );
        buffer.writeBytes( 50 );
        buffer.readBytes( 40 );
        buffer.writeBytes( 20 );
        final ByteBuffer[] byteBuffers = buffer.asReadBuffers();
        assertEquals( "byteBuffers[ 0 ].position()",
                      40,
                      byteBuffers[ 0 ].position() );
        assertEquals( "byteBuffers[ 0 ].limit()",
                      55,
                      byteBuffers[ 0 ].limit() );
        assertEquals( "byteBuffers[ 1 ].position()",
                      0,
                      byteBuffers[ 1 ].position() );
        assertEquals( "byteBuffers[ 1 ].limit()",
                      15,
                      byteBuffers[ 1 ].limit() );
    }

    public void testAsWriteBuffers()
        throws Exception
    {
        final CircularBuffer buffer = new CircularBuffer( 55 );
        buffer.writeBytes( 20 );
        final ByteBuffer[] byteBuffers = buffer.asWriteBuffers();
        assertEquals( "byteBuffers[ 0 ].position()",
                      20,
                      byteBuffers[ 0 ].position() );
        assertEquals( "byteBuffers[ 0 ].limit()",
                      55,
                      byteBuffers[ 0 ].limit() );
        assertEquals( "byteBuffers[ 1 ].position()",
                      0,
                      byteBuffers[ 1 ].position() );
        assertEquals( "byteBuffers[ 1 ].limit()",
                      0,
                      byteBuffers[ 1 ].limit() );
    }

    public void testAsWriteBuffersWithWrap()
        throws Exception
    {
        final CircularBuffer buffer = new CircularBuffer( 55 );
        buffer.writeBytes( 50 );
        buffer.readBytes( 40 );
        buffer.writeBytes( 20 );
        final ByteBuffer[] byteBuffers = buffer.asWriteBuffers();
        assertEquals( "byteBuffers[ 0 ].position()",
                      15,
                      byteBuffers[ 0 ].position() );
        assertEquals( "byteBuffers[ 0 ].limit()",
                      40,
                      byteBuffers[ 0 ].limit() );
        assertEquals( "byteBuffers[ 1 ].position()",
                      0,
                      byteBuffers[ 1 ].position() );
        assertEquals( "byteBuffers[ 1 ].limit()",
                      0,
                      byteBuffers[ 1 ].limit() );
    }
}
