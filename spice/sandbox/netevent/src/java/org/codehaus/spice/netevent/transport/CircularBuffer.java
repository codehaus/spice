/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netevent.transport;

import java.nio.ByteBuffer;

/**
 * A circular byte buffer.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-07 03:25:08 $
 */
public class CircularBuffer
{
    /** The underlying byte array containing data. */
    private final ByteBuffer[] m_byteBuffers;

    /** The underlying byte array containing data. */
    private final byte[] m_data;

    /** The index into array marking the start of the available data. */
    private int m_start;

    /** The index into array marking the byte after end of the available data. */
    private int m_end;

    /** Flag set to true if the buffer wrape past end of array. */
    private boolean m_isWrappedBuffer;

    /**
     * Create a circular buffer.
     * 
     * @param size the size of underlying byte buffer
     */
    public CircularBuffer( final int size )
    {
        m_data = new byte[ size ];
        m_byteBuffers = new ByteBuffer[ 2 ];
        m_byteBuffers[ 0 ] = ByteBuffer.wrap( m_data );
        m_byteBuffers[ 1 ] = ByteBuffer.wrap( m_data );
    }

    /**
     * Return an array of ByteBuffers representing Circular buffer in state
     * ready to read.
     * 
     * @return the read buffers
     */
    public ByteBuffer[] asReadBuffers()
    {
        if( isWrappedBuffer() )
        {
            m_byteBuffers[ 0 ].position( m_start );
            m_byteBuffers[ 0 ].limit( getCapacity() );
            m_byteBuffers[ 1 ].position( 0 );
            m_byteBuffers[ 1 ].limit( m_end );
        }
        else
        {
            m_byteBuffers[ 0 ].position( m_start );
            m_byteBuffers[ 0 ].limit( m_end );
            m_byteBuffers[ 1 ].position( 0 );
            m_byteBuffers[ 1 ].limit( 0 );
        }
        return m_byteBuffers;
    }

    /**
     * Return an array of ByteBuffers representing Circular buffer in state
     * ready to write.
     * 
     * @return the write buffers
     */
    public ByteBuffer[] asWriteBuffers()
    {
        if( isWrappedBuffer() )
        {
            m_byteBuffers[ 0 ].position( m_end );
            m_byteBuffers[ 0 ].limit( m_start );
            m_byteBuffers[ 1 ].position( 0 );
            m_byteBuffers[ 1 ].limit( 0 );
        }
        else
        {
            m_byteBuffers[ 0 ].position( m_end );
            m_byteBuffers[ 0 ].limit( getCapacity() );
            m_byteBuffers[ 1 ].position( 0 );
            m_byteBuffers[ 1 ].limit( m_start );
        }
        return m_byteBuffers;
    }

    /**
     * Increment start counter by specified amount. (ie Simulating a read from
     * buffer)
     * 
     * @param count the number of bytes read
     */
    public void readBytes( final int count )
    {
        if( 0 == count )
        {
            return;
        }
        if( count > getAvailable() )
        {
            final String message = count + " > " + getAvailable();
            throw new IllegalArgumentException( message );
        }
        final int oldStart = m_start;
        m_start = (m_start + count) % getCapacity();
        if( m_start <= oldStart )
        {
            m_isWrappedBuffer = false;
        }
    }

    /**
     * Increment end counter by specified amount. (ie Simulating a write to
     * buffer)
     * 
     * @param count the number of bytes written
     */
    public void writeBytes( final int count )
    {
        if( 0 == count )
        {
            return;
        }
        if( count > getSpace() )
        {
            final String message = count + " > " + getSpace();
            throw new IllegalArgumentException( message );
        }
        final int oldEnd = m_end;
        m_end = (m_end + count) % getCapacity();
        if( m_end <= oldEnd )
        {
            m_isWrappedBuffer = true;
        }
    }

    /**
     * Return the used space in buffer in bytes.
     * 
     * @return the used space in buffer in bytes.
     */
    public int getAvailable()
    {
        if( isWrappedBuffer() )
        {
            return m_data.length - m_start + m_end;
        }
        else
        {
            return m_end - m_start;
        }
    }

    /**
     * Return the unused space in buffer in bytes.
     * 
     * @return the unused space in buffer in bytes.
     */
    public int getSpace()
    {
        return getCapacity() - getAvailable();
    }

    /**
     * Return the buffers capacity in bytes.
     * 
     * @return the buffers capacity in bytes.
     */
    public int getCapacity()
    {
        return m_data.length;
    }

    /**
     * Return true if buffer is wrapped.
     * 
     * @return true if buffer is wrapped.
     */
    boolean isWrappedBuffer()
    {
        return m_isWrappedBuffer;
    }

    /**
     * Return the index at start of available data.
     * 
     * @return the index at start of available data.
     */
    int getStart()
    {
        return m_start;
    }

    /**
     * Return the index after end of available data.
     * 
     * @return the index after end of available data.
     */
    int getEnd()
    {
        return m_end;
    }
}
