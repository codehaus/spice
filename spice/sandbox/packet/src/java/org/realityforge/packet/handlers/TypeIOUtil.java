package org.realityforge.packet.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-13 06:57:38 $
 */
public class TypeIOUtil
{
    public static short readShort( final InputStream inputStream )
        throws IOException
    {
        return (short)((inputStream.read() << 8) +
                       inputStream.read());
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
        output.write( (byte)((value >> 56) & 0xff) );
        output.write( (byte)((value >> 48) & 0xff) );
        output.write( (byte)((value >> 40) & 0xff) );
        output.write( (byte)((value >> 32) & 0xff) );
        output.write( (byte)((value >> 24) & 0xff) );
        output.write( (byte)((value >> 16) & 0xff) );
        output.write( (byte)((value >> 8) & 0xff) );
        output.write( (byte)((value >> 0) & 0xff) );
    }

    public static long readLong( final InputStream input )
        throws IOException
    {
        final long value1 = input.read();
        final long value2 = input.read();
        final long value3 = input.read();
        final long value4 = input.read();
        final long value5 = input.read();
        final long value6 = input.read();
        final long value7 = input.read();
        final long value8 = input.read();

        return ((value8 << 0) +
                (value7 << 8) +
                (value6 << 16) +
                (value5 << 24) +
                (value4 << 32) +
                (value3 << 40) +
                (value2 << 48) +
                (value1 << 56));
    }
}
