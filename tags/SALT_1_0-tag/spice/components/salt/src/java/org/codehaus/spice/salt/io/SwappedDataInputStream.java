/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.salt.io;

import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * DataInput for systems relying on little endian data formats.
 *
 * @author Peter Donald
 */
public class SwappedDataInputStream
    implements DataInput
{
    //The underlying input stream
    private InputStream m_input;

    public SwappedDataInputStream( final InputStream input )
    {
        m_input = input;
    }

    public boolean readBoolean()
        throws IOException, EOFException
    {
        return ( 0 != readByte() );
    }

    public byte readByte()
        throws IOException, EOFException
    {
        return (byte)m_input.read();
    }

    public int readUnsignedByte()
        throws IOException, EOFException
    {
        return m_input.read();
    }

    public char readChar()
        throws IOException, EOFException
    {
        return (char)readShort();
    }

    public short readShort()
        throws IOException, EOFException
    {
        return EndianUtil.readSwappedShort( m_input );
    }

    public int readUnsignedShort()
        throws IOException, EOFException
    {
        return EndianUtil.readSwappedUnsignedShort( m_input );
    }

    public int readInt()
        throws IOException, EOFException
    {
        return EndianUtil.readSwappedInteger( m_input );
    }

    public long readLong()
        throws IOException, EOFException
    {
        return EndianUtil.readSwappedLong( m_input );
    }

    public float readFloat()
        throws IOException, EOFException
    {
        return EndianUtil.readSwappedFloat( m_input );
    }

    public double readDouble()
        throws IOException, EOFException
    {
        return EndianUtil.readSwappedDouble( m_input );
    }

    public void readFully( final byte[] data )
        throws IOException, EOFException
    {
        readFully( data, 0, data.length );
    }

    public void readFully( final byte[] data,
                           final int offset,
                           final int length )
        throws IOException, EOFException
    {
        int remaining = length;
        while( remaining > 0 )
        {
            final int location = offset + ( length - remaining );
            final int count = read( data, location, remaining );

            if( -1 == count )
            {
                throw new EOFException();
            }

            remaining -= count;
        }
    }

    public String readLine()
        throws IOException, EOFException
    {
        throw new IOException( "Operation not supported" );
    }

    public String readUTF()
        throws IOException, EOFException
    {
        throw new IOException( "Operation not supported" );
    }

    public int skipBytes( final int count )
        throws IOException, EOFException
    {
        return (int)skip( count );
    }

    public int available()
        throws IOException, EOFException
    {
        return m_input.available();
    }

    public int read()
        throws IOException, EOFException
    {
        return m_input.read();
    }

    public int read( final byte[] data )
        throws IOException, EOFException
    {
        return read( data, 0, data.length );
    }

    public int read( final byte[] data, final int offset, final int length )
        throws IOException, EOFException
    {
        return m_input.read( data, offset, length );
    }

    public long skip( final long count )
        throws IOException, EOFException
    {
        return m_input.skip( count );
    }

    public void mark( final int readLimit )
    {
        m_input.mark( readLimit );
    }

    public boolean markSupported()
    {
        return m_input.markSupported();
    }

    public void reset()
        throws IOException
    {
        m_input.reset();
    }

    public void close()
        throws IOException, EOFException
    {
        m_input.close();
    }
}
