package org.realityforge.packet.handlers;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-03-18 03:38:13 $
 */
public class HexDump
{
    private static final String EOL =
        System.getProperty( "line.separator" );

    private static final char[] HEXCODES = new char[]
    {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
        'E', 'F'
    };

    private static final int[] SHIFTS = new int[]
    {
        28, 24, 20, 16, 12, 8, 4, 0
    };

    public static void hexDump( final byte[] data,
                                final int offset,
                                final OutputStream output )
        throws IOException
    {
        if( null == output )
        {
            throw new NullPointerException( "output" );
        }
        if( offset < 0 )
        {
            throw new IllegalArgumentException( "offset < 0" );
        }
        if( offset >= data.length )
        {
            throw new IllegalArgumentException( "offset >= data.length" );
        }
        long viewOffset = offset;
        final StringBuffer buffer = new StringBuffer( 74 );

        for( int j = offset; j < data.length; j += 16 )
        {
            final int readCount = Math.min( 16, data.length - j );

            dumpLong( buffer, viewOffset );

            buffer.append( ' ' );
            for( int k = 0; k < 16; k++ )
            {
                if( k < readCount )
                {
                    dumpByte( buffer, data[ k + j ] );
                }
                else
                {
                    buffer.append( "  " );
                }
                buffer.append( ' ' );
            }
            for( int k = 0; k < readCount; k++ )
            {
                if( (data[ k + j ] >= ' ') && (data[ k + j ] < 127) )
                {
                    buffer.append( (char)data[ k + j ] );
                }
                else
                {
                    buffer.append( '.' );
                }
            }
            buffer.append( EOL );
            output.write( buffer.toString().getBytes() );
            output.flush();
            buffer.setLength( 0 );
            viewOffset += readCount;
        }
    }

    private static void dumpLong( final StringBuffer sb, final long value )
    {
        for( int j = 0; j < 8; j++ )
        {
            sb.append( HEXCODES[ ((int)(value >> SHIFTS[ j ])) & 15 ] );
        }
    }

    private static void dumpByte( final StringBuffer sb, final byte value )
    {
        for( int j = 0; j < 2; j++ )
        {
            sb.append( HEXCODES[ (value >> SHIFTS[ j + 6 ]) & 15 ] );
        }
    }
}
