/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.salt.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import junit.framework.TestCase;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-02 02:15:05 $
 */
public class EndianUtilTestCase
    extends TestCase
{
    private static final short SHORT_VALUE = (short)57;
    private static final int US_SHORT_VALUE = (int)Short.MAX_VALUE + 1;
    private static final int INTEGER_VALUE = 33333333;
    private static final long US_INTEGER_VALUE = (long)Integer.MAX_VALUE + 1;
    private static final long LONG_VALUE = 3333333333L;
    private static final float FLOAT_VALUE = 55.32f;
    private static final double DOUBLE_VALUE = 545.33453452432;

    private static final short SWAPPED_SHORT_VALUE = (short)14592;
    private static final int SWAPPED_INTEGER_VALUE = 1436613633;
    private static final long SWAPPED_LONG_VALUE = 6170405129901047808L;
    private static final float SWAPPED_FLOAT_VALUE = -4.533019E-11f;
    private static final double SWAPPED_DOUBLE_VALUE = -3.289995210423809E-59;

    private static final byte[] SHORT_BYTES = new byte[]{57, 0};
    private static final byte[] US_SHORT_BYTES = new byte[]{0, -128};
    private static final byte[] INTEGER_BYTES = new byte[]{85, -96, -4, 1};
    private static final byte[] US_INTEGER_BYTES = new byte[]{0, 0, 0, -128};
    private static final byte[] LONG_BYTES = new byte[]{85,
                                                        -95,
                                                        -82,
                                                        -58,
                                                        0,
                                                        0,
                                                        0,
                                                        0};
    private static final byte[] FLOAT_BYTES = new byte[]{-82, 71, 93, 66};
    private static final byte[] DOUBLE_BYTES = new byte[]{-77,
                                                          -54,
                                                          111,
                                                          32,
                                                          -83,
                                                          10,
                                                          -127,
                                                          64};

    private static final byte[] FULL_BYTES = new byte[]{1,
                                                        2,
                                                        3,
                                                        4,
                                                        5,
                                                        6,
                                                        7,
                                                        8};

    public EndianUtilTestCase( final String name )
    {
        super( name );
    }

    public void testSingleSwaps()
    {
        assertEquals( "Single swap SHORT_VALUE",
                      SWAPPED_SHORT_VALUE,
                      EndianUtil.swapShort( SHORT_VALUE ) );
        assertEquals( "Single swap LONG_VALUE",
                      SWAPPED_LONG_VALUE,
                      EndianUtil.swapLong( LONG_VALUE ) );
        assertEquals( "Single swap INTEGER_VALUE",
                      SWAPPED_INTEGER_VALUE,
                      EndianUtil.swapInteger( INTEGER_VALUE ) );
        assertTrue( "Single swap FLOAT_VALUE",
                    SWAPPED_FLOAT_VALUE == EndianUtil.swapFloat( FLOAT_VALUE ) );
        assertTrue( "Single swap DOUBLE_VALUE",
                    SWAPPED_DOUBLE_VALUE ==
                    EndianUtil.swapDouble( DOUBLE_VALUE ) );
    }

    public void testDoubleSwaps()
    {
        assertEquals( "Double swap SHORT_VALUE",
                      SHORT_VALUE,
                      EndianUtil.swapShort(
                          EndianUtil.swapShort( SHORT_VALUE ) ) );
        assertEquals( "Double swap LONG_VALUE",
                      LONG_VALUE,
                      EndianUtil.swapLong( EndianUtil.swapLong( LONG_VALUE ) ) );
        assertEquals( "Double swap INTEGER_VALUE",
                      INTEGER_VALUE,
                      EndianUtil.swapInteger(
                          EndianUtil.swapInteger( INTEGER_VALUE ) ) );
        assertTrue( "Double swap FLOAT_VALUE",
                    FLOAT_VALUE ==
                    EndianUtil.swapFloat( EndianUtil.swapFloat( FLOAT_VALUE ) ) );
        assertTrue( "Double swap DOUBLE_VALUE",
                    DOUBLE_VALUE ==
                    EndianUtil.swapDouble(
                        EndianUtil.swapDouble( DOUBLE_VALUE ) ) );
    }

    public void testWriteLongBytes()
    {
        final byte[] data = new byte[ 8 ];

        EndianUtil.writeSwappedShort( data,
                                      0,
                                      EndianUtil.readSwappedShort( FULL_BYTES,
                                                                   0 ) );
        assertEqualArray( "SWAPPED_SHORT", FULL_BYTES, data, 2 );
        EndianUtil.writeSwappedUnsignedShort( data,
                                              0,
                                              EndianUtil.readSwappedUnsignedShort(
                                                  FULL_BYTES, 0 ) );
        assertEqualArray( "Unsigned SWAPPED_SHORT", FULL_BYTES, data, 2 );
        EndianUtil.writeSwappedInteger( data,
                                        0,
                                        EndianUtil.readSwappedInteger(
                                            FULL_BYTES, 0 ) );
        assertEqualArray( "SWAPPED_INTEGER", FULL_BYTES, data, 4 );
        EndianUtil.writeSwappedUnsignedInteger( data,
                                                0,
                                                EndianUtil.readSwappedUnsignedInteger(
                                                    FULL_BYTES, 0 ) );
        assertEqualArray( "Unsigned SWAPPED_INTEGER", FULL_BYTES, data, 4 );
        EndianUtil.writeSwappedLong( data,
                                     0,
                                     EndianUtil.readSwappedLong( FULL_BYTES,
                                                                 0 ) );
        assertEqualArray( "SWAPPED_LONG", FULL_BYTES, data, 8 );
        EndianUtil.writeSwappedFloat( data,
                                      0,
                                      EndianUtil.readSwappedFloat( FULL_BYTES,
                                                                   0 ) );
        assertEqualArray( "SWAPPED_FLOAT", FULL_BYTES, data, 4 );
        EndianUtil.writeSwappedDouble( data,
                                       0,
                                       EndianUtil.readSwappedDouble(
                                           FULL_BYTES, 0 ) );
        assertEqualArray( "SWAPPED_DOUBLE", FULL_BYTES, data, 8 );
    }

    public void testReadEOF()
        throws Exception
    {
        try
        {
            final ByteArrayInputStream data =
                new ByteArrayInputStream( new byte[ 0 ] );
            EndianUtil.readSwappedShort( data );
            fail( "Expected to get an EOF exception as no data in stream" );
        }
        catch( EOFException eof )
        {

        }
    }

    public void testReadWriteBytes()
    {
        final byte[] data = new byte[ 8 ];
        EndianUtil.writeSwappedShort( data, 0, SHORT_VALUE );
        assertEquals( "Read/Write swaped SHORT_VALUE",
                      SHORT_VALUE,
                      EndianUtil.readSwappedShort( data, 0 ) );
        EndianUtil.writeSwappedInteger( data, 0, INTEGER_VALUE );
        EndianUtil.writeSwappedUnsignedShort( data, 0, US_SHORT_VALUE );
        assertEquals( "Read/Write swaped US_SHORT_VALUE",
                      US_SHORT_VALUE,
                      EndianUtil.readSwappedUnsignedShort( data, 0 ) );
        EndianUtil.writeSwappedInteger( data, 0, INTEGER_VALUE );
        assertEquals( "Read/Write swaped INTEGER_VALUE",
                      INTEGER_VALUE,
                      EndianUtil.readSwappedInteger( data, 0 ) );
        EndianUtil.writeSwappedUnsignedInteger( data, 0, US_INTEGER_VALUE );
        assertEquals( "Read/Write swaped US_INTEGER_VALUE",
                      US_INTEGER_VALUE,
                      EndianUtil.readSwappedUnsignedInteger( data, 0 ) );
        EndianUtil.writeSwappedLong( data, 0, LONG_VALUE );
        assertEquals( "Read/Write swaped LONG_VALUE",
                      LONG_VALUE,
                      EndianUtil.readSwappedLong( data, 0 ) );
        EndianUtil.writeSwappedFloat( data, 0, FLOAT_VALUE );
        assertTrue( "Read/Write swaped FLOAT_VALUE",
                    FLOAT_VALUE == EndianUtil.readSwappedFloat( data, 0 ) );
        EndianUtil.writeSwappedDouble( data, 0, DOUBLE_VALUE );
        assertTrue( "Read/Write swaped DOUBLE_VALUE",
                    DOUBLE_VALUE == EndianUtil.readSwappedDouble( data, 0 ) );
    }

    public void testWriteBytes()
    {
        final byte[] data = new byte[ 8 ];

        EndianUtil.writeSwappedShort( data, 0, SHORT_VALUE );
        assertEqualArray( "SHORT_VALUE", SHORT_BYTES, data );
        EndianUtil.writeSwappedUnsignedShort( data, 0, US_SHORT_VALUE );
        assertEqualArray( "US_SHORT_VALUE", US_SHORT_BYTES, data );
        EndianUtil.writeSwappedInteger( data, 0, INTEGER_VALUE );
        assertEqualArray( "INTEGER_VALUE", INTEGER_BYTES, data );
        EndianUtil.writeSwappedUnsignedInteger( data, 0, US_INTEGER_VALUE );
        assertEqualArray( "US_INTEGER_VALUE", US_INTEGER_BYTES, data );
        EndianUtil.writeSwappedLong( data, 0, LONG_VALUE );
        assertEqualArray( "LONG_VALUE", LONG_BYTES, data );
        EndianUtil.writeSwappedFloat( data, 0, FLOAT_VALUE );
        assertEqualArray( "FLOAT_VALUE", FLOAT_BYTES, data );
        EndianUtil.writeSwappedDouble( data, 0, DOUBLE_VALUE );
        assertEqualArray( "DOUBLE_VALUE", DOUBLE_BYTES, data );
    }

    public void testWriteInputStreams()
        throws Exception
    {
        ByteArrayInputStream input = null;

        input = new ByteArrayInputStream( SHORT_BYTES );
        assertEquals( "Stream reading SHORT",
                      SHORT_VALUE,
                      EndianUtil.readSwappedShort( input ) );

        input = new ByteArrayInputStream( US_SHORT_BYTES );
        assertEquals( "Stream reading US_SHORT",
                      US_SHORT_VALUE,
                      EndianUtil.readSwappedUnsignedShort( input ) );

        input = new ByteArrayInputStream( INTEGER_BYTES );
        assertEquals( "Stream reading INTEGER",
                      INTEGER_VALUE,
                      EndianUtil.readSwappedInteger( input ) );

        input = new ByteArrayInputStream( US_INTEGER_BYTES );
        assertEquals( "Stream reading US_INTEGER",
                      US_INTEGER_VALUE,
                      EndianUtil.readSwappedUnsignedInteger( input ) );

        input = new ByteArrayInputStream( LONG_BYTES );
        assertEquals( "Stream reading LONG",
                      LONG_VALUE,
                      EndianUtil.readSwappedLong( input ) );

        input = new ByteArrayInputStream( FLOAT_BYTES );
        assertTrue( "Stream reading FLOAT",
                    FLOAT_VALUE == EndianUtil.readSwappedFloat( input ) );

        input = new ByteArrayInputStream( DOUBLE_BYTES );
        assertTrue( "Stream reading FLOAT",
                    DOUBLE_VALUE == EndianUtil.readSwappedDouble( input ) );
    }

    public void testWriteOutputStreams()
        throws Exception
    {
        final ByteArrayOutputStream output = new ByteArrayOutputStream( 8 );

        EndianUtil.writeSwappedShort( output, SHORT_VALUE );
        assertEqualArray( "SHORT_VALUE", SHORT_BYTES, output.toByteArray() );
        output.reset();

        EndianUtil.writeSwappedUnsignedShort( output, US_SHORT_VALUE );
        assertEqualArray( "US_SHORT_VALUE",
                          US_SHORT_BYTES,
                          output.toByteArray() );
        output.reset();

        EndianUtil.writeSwappedInteger( output, INTEGER_VALUE );
        assertEqualArray( "INTEGER_VALUE",
                          INTEGER_BYTES,
                          output.toByteArray() );
        output.reset();

        EndianUtil.writeSwappedUnsignedInteger( output, US_INTEGER_VALUE );
        assertEqualArray( "US_INTEGER_VALUE",
                          US_INTEGER_BYTES,
                          output.toByteArray() );
        output.reset();

        EndianUtil.writeSwappedLong( output, LONG_VALUE );
        assertEqualArray( "LONG_VALUE", LONG_BYTES, output.toByteArray() );
        output.reset();

        EndianUtil.writeSwappedFloat( output, FLOAT_VALUE );
        assertEqualArray( "FLOAT_VALUE", FLOAT_BYTES, output.toByteArray() );
        output.reset();

        EndianUtil.writeSwappedDouble( output, DOUBLE_VALUE );
        assertEqualArray( "DOUBLE_VALUE", DOUBLE_BYTES, output.toByteArray() );
        output.reset();
    }

    private void assertEqualArray( final String array,
                                   final byte[] expected,
                                   final byte[] data )
    {
        assertEqualArray( array, expected, data, expected.length );
    }

    private void assertEqualArray( final String array,
                                   final byte[] expected,
                                   final byte[] data,
                                   final int length )
    {
        for( int i = 0; i < length; i++ )
        {
            assertEquals( array + "[" + i + "]", expected[ i ], data[ i ] );
        }
    }

    protected void displayByteData( String label,
                                    int size,
                                    final byte[] data )
    {
        System.out.print( label + "_VALUE=" );
        for( int i = 0; i < size; i++ )
        {
            byte b = data[ i ];
            System.out.print( b );
            System.out.print( " " );
        }
        System.out.println();
    }
}
