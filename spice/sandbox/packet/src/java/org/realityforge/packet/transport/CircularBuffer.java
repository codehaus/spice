package org.realityforge.packet.transport;

import java.nio.ByteBuffer;

/**
 * A circular byte buffer.
 * 
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-11-24 05:19:34 $
 */
public class CircularBuffer
{
    /** The underlying byte array containing data. */
    private final ByteBuffer[] _byteBuffers;

    /** The underlying byte array containing data. */
    private final byte[] _data;

    /** The index into array marking the start of the available data. */
    private int _start;

    /** The index into array marking the byte after end of the available data. */
    private int _end;

    /**
     * Create a circular buffer.
     * 
     * @param size the size of underlying byte buffer
     */
    public CircularBuffer( final int size )
    {
        _data = new byte[ size ];
        _byteBuffers = new ByteBuffer[ 2 ];
        _byteBuffers[ 0 ] = ByteBuffer.wrap( _data );
        _byteBuffers[ 1 ] = ByteBuffer.wrap( _data );
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
            _byteBuffers[ 0 ].position( _start );
            _byteBuffers[ 0 ].limit( getCapacity() );
            _byteBuffers[ 1 ].position( 0 );
            _byteBuffers[ 1 ].limit( _end );
        }
        else
        {
            _byteBuffers[ 0 ].position( _start );
            _byteBuffers[ 0 ].limit( _end );
            _byteBuffers[ 1 ].position( 0 );
            _byteBuffers[ 1 ].limit( 0 );
        }
        return _byteBuffers;
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
            _byteBuffers[ 0 ].position( _end );
            _byteBuffers[ 0 ].limit( _start );
            _byteBuffers[ 1 ].position( 0 );
            _byteBuffers[ 1 ].limit( 0 );
        }
        else
        {
            _byteBuffers[ 0 ].position( _end );
            _byteBuffers[ 0 ].limit( getCapacity() );
            _byteBuffers[ 1 ].position( 0 );
            _byteBuffers[ 1 ].limit( _start );
        }
        return _byteBuffers;
    }

    /**
     * Increment start counter by specified amount. (ie Simulating a read from
     * buffer)
     * 
     * @param count the number of bytes read
     */
    public void readBytes( final int count )
    {
        if( count > getAvailable() )
        {
            final String message = count + " > " + getAvailable();
            throw new IllegalArgumentException( message );
        }
        _start = (_start + count) % getCapacity();
    }

    /**
     * Increment end counter by specified amount. (ie Simulating a write to
     * buffer)
     * 
     * @param count the number of bytes written
     */
    public void writeBytes( final int count )
    {
        if( count > getSpace() )
        {
            final String message = count + " > " + getSpace();
            throw new IllegalArgumentException( message );
        }
        _end = (_end + count) % getCapacity();
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
            return _data.length - _start + _end;
        }
        else
        {
            return _end - _start;
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
        return _data.length;
    }

    /**
     * Return true if buffer is wrapped.
     * 
     * @return true if buffer is wrapped.
     */
    boolean isWrappedBuffer()
    {
        return _start > _end;
    }

    /**
     * Return the index at start of available data.
     * 
     * @return the index at start of available data.
     */
    int getStart()
    {
        return _start;
    }

    /**
     * Return the index after end of available data.
     * 
     * @return the index after end of available data.
     */
    int getEnd()
    {
        return _end;
    }
}
