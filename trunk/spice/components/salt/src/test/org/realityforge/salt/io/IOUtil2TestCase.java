/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.salt.io;

import junit.framework.TestCase;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.7 $ $Date: 2003-06-13 04:46:30 $
 */
public class IOUtil2TestCase
    extends TestCase
{
    private final static byte[] DATA_5_ELEMENTS = new byte[]
    {
        (byte)'a', (byte)'b', (byte)'c', (byte)'d', (byte)'e'
    };

    private final static byte[] DATA_4_ELEMENTS = new byte[]
    {
        (byte)'a', (byte)'b', (byte)'c', (byte)'d'
    };

    private final static byte[] DATA_4_ELEMENTS_CM = new byte[]
    {
        (byte)'a', (byte)'b', (byte)'j', (byte)'d'
    };

    private final static byte[] DATA_4_ELEMENTS_CE = new byte[]
    {
        (byte)'a', (byte)'b', (byte)'c', (byte)'j'
    };

    public IOUtil2TestCase( final String name )
    {
        super( name );
    }

    public void testShutdownReader()
        throws Exception
    {
        final MockInputStream stream = new MockInputStream();
        IOUtil.shutdownReader( new InputStreamReader( stream ) );
        assertTrue( stream.isClosed() );
    }

    public void testShutdownNullReader()
        throws Exception
    {
        IOUtil.shutdownReader( null );
    }

    public void testShutdownInput()
        throws Exception
    {
        final MockInputStream stream = new MockInputStream();
        IOUtil.shutdownStream( stream );
        assertTrue( stream.isClosed() );
    }

    public void testShutdownNullInput()
        throws Exception
    {
        IOUtil.shutdownStream( (InputStream)null );
    }

    public void testShutdownWriter()
    {
        final MockOutputStream stream = new MockOutputStream();
        IOUtil.shutdownWriter( new OutputStreamWriter( stream ) );
        assertTrue( stream.isClosed() );
    }

    public void testShutdownNullWriter()
        throws Exception
    {
        IOUtil.shutdownWriter( null );
    }

    public void testShutdownOutput()
    {
        final MockOutputStream stream = new MockOutputStream();
        IOUtil.shutdownStream( stream );
        assertTrue( stream.isClosed() );
    }

    public void testShutdownNullOutput()
        throws Exception
    {
        IOUtil.shutdownStream( (OutputStream)null );
    }

    public void testCopyInputToOutputStreamWithDefaultBuffer()
        throws Exception
    {
        final MockInputStream input = new MockInputStream( DATA_4_ELEMENTS );
        final MockOutputStream output = new MockOutputStream();
        IOUtil.copy( input, output );
        final byte[] bytes = output.toByteArray();
        assertEqualArrays( DATA_4_ELEMENTS, bytes );
    }

    public void testCopyInputToOutputStreamWithLesserBuffer()
        throws Exception
    {
        final MockInputStream input = new MockInputStream( DATA_4_ELEMENTS );
        final MockOutputStream output = new MockOutputStream();
        IOUtil.copy( input, output, 2 );
        final byte[] bytes = output.toByteArray();
        assertEqualArrays( DATA_4_ELEMENTS, bytes );
    }

    public void testCopyInputToOutputStreamWithGreaterBuffer()
        throws Exception
    {
        final MockInputStream input = new MockInputStream( DATA_4_ELEMENTS );
        final MockOutputStream output = new MockOutputStream();
        IOUtil.copy( input, output, 50 );
        final byte[] bytes = output.toByteArray();
        assertEqualArrays( DATA_4_ELEMENTS, bytes );
    }

    public void testCopyInputToWriterWithDefaultBuffer()
        throws Exception
    {
        final MockInputStream input = new MockInputStream( DATA_4_ELEMENTS );
        final MockOutputStream output = new MockOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter( output );
        IOUtil.copy( input, writer );
        writer.flush();
        final byte[] bytes = output.toByteArray();
        assertEqualArrays( DATA_4_ELEMENTS, bytes );
    }

    public void testCopyInputToWriterWithLesserBuffer()
        throws Exception
    {
        final MockInputStream input = new MockInputStream( DATA_4_ELEMENTS );
        final MockOutputStream output = new MockOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter( output );
        IOUtil.copy( input, writer, 2 );
        writer.flush();
        final byte[] bytes = output.toByteArray();
        assertEqualArrays( DATA_4_ELEMENTS, bytes );
    }

    public void testCopyInputToWriterWithGreaterBuffer()
        throws Exception
    {
        final MockInputStream input = new MockInputStream( DATA_4_ELEMENTS );
        final MockOutputStream output = new MockOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter( output );
        IOUtil.copy( input, writer, 50 );
        writer.flush();
        final byte[] bytes = output.toByteArray();
        assertEqualArrays( DATA_4_ELEMENTS, bytes );
    }

    public void testCopyEncodedInputToWriterWithDefaultBuffer()
        throws Exception
    {
        final MockInputStream input = new MockInputStream( DATA_4_ELEMENTS );
        final MockOutputStream output = new MockOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter( output );
        IOUtil.copy( input, writer, "US-ASCII" );
        writer.flush();
        final byte[] bytes = output.toByteArray();
        assertEqualArrays( DATA_4_ELEMENTS, bytes );
    }

    public void testCopyEncodedInputToWriterWithLesserBuffer()
        throws Exception
    {
        final MockInputStream input = new MockInputStream( DATA_4_ELEMENTS );
        final MockOutputStream output = new MockOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter( output );
        IOUtil.copy( input, writer, "US-ASCII", 2 );
        writer.flush();
        final byte[] bytes = output.toByteArray();
        assertEqualArrays( DATA_4_ELEMENTS, bytes );
    }

    public void testCopyEncodedInputToWriterWithGreaterBuffer()
        throws Exception
    {
        final MockInputStream input = new MockInputStream( DATA_4_ELEMENTS );
        final MockOutputStream output = new MockOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter( output );
        IOUtil.copy( input, writer, "US-ASCII", 50 );
        writer.flush();
        final byte[] bytes = output.toByteArray();
        assertEqualArrays( DATA_4_ELEMENTS, bytes );
    }

    public void testCopyBytesToWriterWithDefaultBuffer()
        throws Exception
    {
        final MockOutputStream output = new MockOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter( output );
        IOUtil.copy( DATA_4_ELEMENTS, writer );
        writer.flush();
        final byte[] bytes = output.toByteArray();
        assertEqualArrays( DATA_4_ELEMENTS, bytes );
    }

    public void testCopyBytesToWriterWithLesserBuffer()
        throws Exception
    {
        final MockOutputStream output = new MockOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter( output );
        IOUtil.copy( DATA_4_ELEMENTS, writer, 2 );
        writer.flush();
        final byte[] bytes = output.toByteArray();
        assertEqualArrays( DATA_4_ELEMENTS, bytes );
    }

    public void testCopyBytesToWriterWithGreaterBuffer()
        throws Exception
    {
        final MockOutputStream output = new MockOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter( output );
        IOUtil.copy( DATA_4_ELEMENTS, writer, 50 );
        writer.flush();
        final byte[] bytes = output.toByteArray();
        assertEqualArrays( DATA_4_ELEMENTS, bytes );
    }

    public void testCopyEncodedBytesToWriterWithDefaultBuffer()
        throws Exception
    {
        final MockOutputStream output = new MockOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter( output );
        IOUtil.copy( DATA_4_ELEMENTS, writer, "US-ASCII" );
        writer.flush();
        final byte[] bytes = output.toByteArray();
        assertEqualArrays( DATA_4_ELEMENTS, bytes );
    }

    public void testCopyEncodedBytesToWriterWithLesserBuffer()
        throws Exception
    {
        final MockOutputStream output = new MockOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter( output );
        IOUtil.copy( DATA_4_ELEMENTS, writer, "US-ASCII", 2 );
        writer.flush();
        final byte[] bytes = output.toByteArray();
        assertEqualArrays( DATA_4_ELEMENTS, bytes );
    }

    public void testCopyEncodedBytesToWriterWithGreaterBuffer()
        throws Exception
    {
        final MockOutputStream output = new MockOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter( output );
        IOUtil.copy( DATA_4_ELEMENTS, writer, "US-ASCII", 50 );
        writer.flush();
        final byte[] bytes = output.toByteArray();
        assertEqualArrays( DATA_4_ELEMENTS, bytes );
    }

    public void testCopyBytesToOutput()
        throws Exception
    {
        final MockOutputStream output = new MockOutputStream();
        IOUtil.copy( DATA_4_ELEMENTS, output );
        final byte[] bytes = output.toByteArray();
        assertEqualArrays( DATA_4_ELEMENTS, bytes );
    }

    public void testCopyReaderToWriterWithDefaultBuffer()
        throws Exception
    {
        final MockInputStream input = new MockInputStream( DATA_4_ELEMENTS );
        final InputStreamReader reader = new InputStreamReader( input );
        final MockOutputStream output = new MockOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter( output );
        IOUtil.copy( reader, writer );
        writer.flush();
        final byte[] bytes = output.toByteArray();
        assertEqualArrays( DATA_4_ELEMENTS, bytes );
    }

    public void testCopyReaderToWriterWithLesserBuffer()
        throws Exception
    {
        final MockInputStream input = new MockInputStream( DATA_4_ELEMENTS );
        final InputStreamReader reader = new InputStreamReader( input );
        final MockOutputStream output = new MockOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter( output );
        IOUtil.copy( reader, writer, 2 );
        writer.flush();
        final byte[] bytes = output.toByteArray();
        assertEqualArrays( DATA_4_ELEMENTS, bytes );
    }

    public void testCopyReaderToWriterWithGreaterBuffer()
        throws Exception
    {
        final MockInputStream input = new MockInputStream( DATA_4_ELEMENTS );
        final InputStreamReader reader = new InputStreamReader( input );
        final MockOutputStream output = new MockOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter( output );
        IOUtil.copy( reader, writer, 50 );
        writer.flush();
        final byte[] bytes = output.toByteArray();
        assertEqualArrays( DATA_4_ELEMENTS, bytes );
    }

    public void testCopyReaderToOutputWithDefaultBuffer()
        throws Exception
    {
        final MockInputStream input = new MockInputStream( DATA_4_ELEMENTS );
        final InputStreamReader reader = new InputStreamReader( input );
        final MockOutputStream output = new MockOutputStream();
        IOUtil.copy( reader, output );
        final byte[] bytes = output.toByteArray();
        assertEqualArrays( DATA_4_ELEMENTS, bytes );
    }

    public void testCopyReaderToOutputWithLesserBuffer()
        throws Exception
    {
        final MockInputStream input = new MockInputStream( DATA_4_ELEMENTS );
        final InputStreamReader reader = new InputStreamReader( input );
        final MockOutputStream output = new MockOutputStream();
        IOUtil.copy( reader, output );
        final byte[] bytes = output.toByteArray();
        assertEqualArrays( DATA_4_ELEMENTS, bytes );
    }

    public void testCopyReaderToOutputWithGreaterBuffer()
        throws Exception
    {
        final MockInputStream input = new MockInputStream( DATA_4_ELEMENTS );
        final InputStreamReader reader = new InputStreamReader( input );
        final MockOutputStream output = new MockOutputStream();
        IOUtil.copy( reader, output );
        final byte[] bytes = output.toByteArray();
        assertEqualArrays( DATA_4_ELEMENTS, bytes );
    }

    public void testCopyStringToOutputWithDefaultBuffer()
        throws Exception
    {
        final MockOutputStream output = new MockOutputStream();
        final String input = new String( DATA_4_ELEMENTS );
        IOUtil.copy( input, output );
        final byte[] bytes = output.toByteArray();
        assertEqualArrays( DATA_4_ELEMENTS, bytes );
    }

    public void testCopyStringToOutputWithLesserBuffer()
        throws Exception
    {
        final MockOutputStream output = new MockOutputStream();
        final String input = new String( DATA_4_ELEMENTS );
        IOUtil.copy( input, output );
        final byte[] bytes = output.toByteArray();
        assertEqualArrays( DATA_4_ELEMENTS, bytes );
    }

    public void testCopyStringToOutputWithGreaterBuffer()
        throws Exception
    {
        final MockOutputStream output = new MockOutputStream();
        final String input = new String( DATA_4_ELEMENTS );
        IOUtil.copy( input, output );
        final byte[] bytes = output.toByteArray();
        assertEqualArrays( DATA_4_ELEMENTS, bytes );
    }

    public void testCopyStringToWriter()
        throws Exception
    {
        final String input = new String( DATA_4_ELEMENTS );
        final MockOutputStream output = new MockOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter( output );
        IOUtil.copy( input, writer );
        writer.flush();
        final byte[] bytes = output.toByteArray();
        assertEqualArrays( DATA_4_ELEMENTS, bytes );
    }

    public void testContentEqualsWithEqualContent()
        throws Exception
    {
        final MockInputStream input1 = new MockInputStream( DATA_4_ELEMENTS );
        final MockInputStream input2 = new MockInputStream( DATA_4_ELEMENTS );
        final boolean contentsEqual = IOUtil.contentEquals( input1, input2 );
        assertTrue( contentsEqual );
    }

    public void testContentEqualsWithFirstLonger()
        throws Exception
    {
        final MockInputStream input1 = new MockInputStream( DATA_5_ELEMENTS );
        final MockInputStream input2 = new MockInputStream( DATA_4_ELEMENTS );
        final boolean contentsEqual = IOUtil.contentEquals( input1, input2 );
        assertTrue( !contentsEqual );
    }

    public void testContentEqualsWithSecondLonger()
        throws Exception
    {
        final MockInputStream input1 = new MockInputStream( DATA_4_ELEMENTS );
        final MockInputStream input2 = new MockInputStream( DATA_5_ELEMENTS );
        final boolean contentsEqual = IOUtil.contentEquals( input1, input2 );
        assertTrue( !contentsEqual );
    }

    public void testContentEqualsWithFirstChangedInMiddle()
        throws Exception
    {
        final MockInputStream input1 = new MockInputStream( DATA_4_ELEMENTS_CM );
        final MockInputStream input2 = new MockInputStream( DATA_4_ELEMENTS );
        final boolean contentsEqual = IOUtil.contentEquals( input1, input2 );
        assertTrue( !contentsEqual );
    }

    public void testContentEqualsWithFirstChangedAtEnd()
        throws Exception
    {
        final MockInputStream input1 = new MockInputStream( DATA_4_ELEMENTS_CE );
        final MockInputStream input2 = new MockInputStream( DATA_4_ELEMENTS );
        final boolean contentsEqual = IOUtil.contentEquals( input1, input2 );
        assertTrue( !contentsEqual );
    }

    public void testContentEqualsWithSecondChangedInMiddle()
        throws Exception
    {
        final MockInputStream input1 = new MockInputStream( DATA_4_ELEMENTS );
        final MockInputStream input2 = new MockInputStream( DATA_4_ELEMENTS_CM );
        final boolean contentsEqual = IOUtil.contentEquals( input1, input2 );
        assertTrue( !contentsEqual );
    }

    public void testContentEqualsWithSecondChangedAtEnd()
        throws Exception
    {
        final MockInputStream input1 = new MockInputStream( DATA_4_ELEMENTS );
        final MockInputStream input2 = new MockInputStream( DATA_4_ELEMENTS_CE );
        final boolean contentsEqual = IOUtil.contentEquals( input1, input2 );
        assertTrue( !contentsEqual );
    }

    public void testInputToStringWithDefaultBuffer()
        throws Exception
    {
        final MockInputStream input1 = new MockInputStream( DATA_4_ELEMENTS );
        final String string = IOUtil.toString( input1 );
        assertEquals( new String( DATA_4_ELEMENTS ), string );
    }

    public void testInputToStringWithLesserBuffer()
        throws Exception
    {
        final MockInputStream input1 = new MockInputStream( DATA_4_ELEMENTS );
        final String string = IOUtil.toString( input1, 2 );
        assertEquals( new String( DATA_4_ELEMENTS ), string );
    }

    public void testInputToStringWithGreaterBuffer()
        throws Exception
    {
        final MockInputStream input1 = new MockInputStream( DATA_4_ELEMENTS );
        final String string = IOUtil.toString( input1, 20 );
        assertEquals( new String( DATA_4_ELEMENTS ), string );
    }

    public void testEncodedInputToStringWithDefaultBuffer()
        throws Exception
    {
        final MockInputStream input1 = new MockInputStream( DATA_4_ELEMENTS );
        final String string = IOUtil.toString( input1, "US_ASCII" );
        assertEquals( new String( DATA_4_ELEMENTS ), string );
    }

    public void testEncodedInputToStringWithLesserBuffer()
        throws Exception
    {
        final MockInputStream input1 = new MockInputStream( DATA_4_ELEMENTS );
        final String string = IOUtil.toString( input1, "US_ASCII", 2 );
        assertEquals( new String( DATA_4_ELEMENTS ), string );
    }

    public void testEncodedInputToStringWithGreaterBuffer()
        throws Exception
    {
        final MockInputStream input1 = new MockInputStream( DATA_4_ELEMENTS );
        final String string = IOUtil.toString( input1, "US_ASCII", 20 );
        assertEquals( new String( DATA_4_ELEMENTS ), string );
    }

    public void testReaderToStringWithDefaultBuffer()
        throws Exception
    {
        final MockInputStream input1 = new MockInputStream( DATA_4_ELEMENTS );
        final String string = IOUtil.toString( new InputStreamReader(input1)  );
        assertEquals( new String( DATA_4_ELEMENTS ), string );
    }

    public void testReaderToStringWithLesserBuffer()
        throws Exception
    {
        final MockInputStream input1 = new MockInputStream( DATA_4_ELEMENTS );
        final String string = IOUtil.toString( new InputStreamReader( input1 ), 2 );
        assertEquals( new String( DATA_4_ELEMENTS ), string );
    }

    public void testReaderToStringWithGreaterBuffer()
        throws Exception
    {
        final MockInputStream input1 = new MockInputStream( DATA_4_ELEMENTS );
        final String string = IOUtil.toString( new InputStreamReader( input1 ), 20 );
        assertEquals( new String( DATA_4_ELEMENTS ), string );
    }

    private void assertEqualArrays( final byte[] expected,
                                    final byte[] actual )
    {
        assertEquals( "expected.length == actual.length", expected.length, actual.length );
        for( int i = 0; i < expected.length; i++ )
        {
            assertEquals( "expected[" + i + "] == actual[" + i + "]",
                          expected[ i ], actual[ i ] );
        }
    }
}
