/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.salt.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import junit.framework.TestCase;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-05-27 13:07:19 $
 */
public class SwappedDataInputTestCase
    extends TestCase
{
    private static final short SHORT_VALUE = (short)57;
    private static final int US_SHORT_VALUE = (int)Short.MAX_VALUE + 1;
    private static final short CHAR_VALUE = (short)'a';
    private static final int INTEGER_VALUE = 33333333;
    private static final long LONG_VALUE = 3333333333L;
    private static final float FLOAT_VALUE = 55.32f;
    private static final double DOUBLE_VALUE = 545.33453452432;
    private static final byte[] BYTES = new byte[]{1, 2, 3};
    private static final short US_BYTE_VALUE = Byte.MAX_VALUE + 1;

    public SwappedDataInputTestCase( final String name )
    {
        super( name );
    }

    public void testReadInput()
        throws Exception
    {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        //False for boolean value
        out.write( 0 );
        //True for boolean value
        out.write( 1 );

        //Byte value
        out.write( 22 );
        out.write( US_BYTE_VALUE );

        EndianUtil.writeSwappedShort( out, SHORT_VALUE );
        EndianUtil.writeSwappedShort( out, CHAR_VALUE );
        EndianUtil.writeSwappedUnsignedShort( out, US_SHORT_VALUE );
        EndianUtil.writeSwappedInteger( out, INTEGER_VALUE );
        EndianUtil.writeSwappedLong( out, LONG_VALUE );
        EndianUtil.writeSwappedFloat( out, FLOAT_VALUE );
        EndianUtil.writeSwappedDouble( out, DOUBLE_VALUE );
        out.write( BYTES );
        out.write( 2 );
        out.flush();

        final byte[] bytes = out.toByteArray();

        final ByteArrayInputStream in = new ByteArrayInputStream( bytes );
        final SwappedDataInputStream input = new SwappedDataInputStream( in );

        assertEquals( "Boolean false", false, input.readBoolean() );
        assertEquals( "Boolean true", true, input.readBoolean() );
        assertEquals( "BYTE", 22, input.readByte() );
        assertEquals( "US_BYTE", US_BYTE_VALUE, input.readUnsignedByte() );
        assertEquals( "SHORT", SHORT_VALUE, input.readShort() );
        assertEquals( "CHAR", CHAR_VALUE, input.readChar() );
        assertEquals( "US_SHORT", US_SHORT_VALUE, input.readUnsignedShort() );
        assertEquals( "INTEGER", INTEGER_VALUE, input.readInt() );
        assertEquals( "LONG", LONG_VALUE, input.readLong() );
        assertTrue( "FLOAT", FLOAT_VALUE == input.readFloat() );
        assertTrue( "DOUBLE", DOUBLE_VALUE == input.readDouble() );

        final byte[] byteData = new byte[ 3 ];
        input.readFully( byteData );
        assertEquals( "BYTES[0]", 1, byteData[ 0 ] );
        assertEquals( "BYTES[1]", 2, byteData[ 1 ] );
        assertEquals( "BYTES[2]", 3, byteData[ 2 ] );

        assertEquals( "available", 1, input.available() );

        assertEquals( "byte read", 2, input.read() );

        assertEquals( "read count on empty", -1, input.read( new byte[ 100 ] ) );
        input.close();
    }

    public void testreadUTF()
    {
        final ByteArrayInputStream in = new ByteArrayInputStream( new byte[ 10 ] );
        final SwappedDataInputStream input = new SwappedDataInputStream( in );

        try
        {
            input.readUTF();
            fail( "Expected that readUTF operation would not be supported" );
        }
        catch( IOException e )
        {
        }
    }

    public void testReadLine()
    {
        final ByteArrayInputStream in = new ByteArrayInputStream( new byte[ 10 ] );
        final SwappedDataInputStream input = new SwappedDataInputStream( in );

        try
        {
            input.readLine();
            fail( "Expected that readLine operation would not be supported" );
        }
        catch( IOException e )
        {
        }
    }

    public void testOverread()
        throws Exception
    {
        final ByteArrayInputStream in = new ByteArrayInputStream( new byte[ 0 ] );
        final SwappedDataInputStream input = new SwappedDataInputStream( in );

        try
        {
            input.readFully( new byte[ 3 ] );
            fail( "Expected that readFully would overrun" );
        }
        catch( IOException e )
        {
        }
    }

    public void testMarking()
        throws Exception
    {
        final ByteArrayInputStream in = new ByteArrayInputStream( new byte[ 10 ] );
        final SwappedDataInputStream input = new SwappedDataInputStream( in );

        assertEquals( "byte skipped", 2, input.skipBytes( 2 ) );
        assertEquals( "bytes available", 8, input.available() );
        assertEquals( "mark supported", true, input.markSupported() );

        input.mark( 3 );
        input.readFully( new byte[ 3 ] );
        assertEquals( "bytes available post mark", 5, input.available() );
        input.reset();
        assertEquals( "bytes available post reset", 8, input.available() );
    }
}
