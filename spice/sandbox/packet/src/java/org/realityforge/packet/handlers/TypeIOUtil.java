package org.realityforge.packet.handlers;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2004-02-06 02:34:17 $
 */
public class TypeIOUtil
{
    public static short readShort( final InputStream input )
        throws IOException
    {
        return (short)((read( input ) << 8) +
                       read( input ));
    }

    public static void writeShort( final OutputStream output,
                                   final short value )
        throws IOException
    {
        output.write( (byte)((value >> 8) & 0xff) );
        output.write( (byte)((value >> 0) & 0xff) );
    }

    public static void writeLong( final OutputStream output,
                                  final long value )
        throws IOException
    {
        output.write( (int)(value >>> 56) & 0xFF );
        output.write( (int)(value >>> 48) & 0xFF );
        output.write( (int)(value >>> 40) & 0xFF );
        output.write( (int)(value >>> 32) & 0xFF );
        output.write( (int)(value >>> 24) & 0xFF );
        output.write( (int)(value >>> 16) & 0xFF );
        output.write( (int)(value >>> 8) & 0xFF );
        output.write( (int)(value >>> 0) & 0xFF );
    }

    public static long readLong( final InputStream input )
        throws IOException
    {
        final long value1 = read( input );
        final long value2 = read( input );
        final long value3 = read( input );
        final long value4 = read( input );
        final long value5 = read( input );
        final long value6 = read( input );
        final long value7 = read( input );
        final long value8 = read( input );

        return ((value1 << 56) +
                (value2 << 48) +
                (value3 << 40) +
                (value4 << 32) +
                (value5 << 24) +
                (value6 << 16) +
                (value7 << 8) +
                (value8 << 0));
    }

    public static final int readInt( final InputStream input )
        throws IOException
    {
        final int ch1 = read( input );
        final int ch2 = read( input );
        final int ch3 = read( input );
        final int ch4 = read( input );
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

    private static int read( final InputStream input )
        throws IOException
    {
        final int value = input.read();
        if( -1 == value )
        {
            throw new EOFException( "Unexpected EOF reached" );
        }
        return value;
    }
}