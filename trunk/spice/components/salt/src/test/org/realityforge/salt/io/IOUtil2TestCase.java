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
 * @version $Revision: 1.1 $ $Date: 2003-06-13 04:21:53 $
 */
public class IOUtil2TestCase
    extends TestCase
{
    private final static byte[] DATA_6_ELEMENTS = new byte[]
    {
        (byte)'a', (byte)'b', (byte)'c', (byte)'d', (byte)'e', (byte)'f'
    };

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
