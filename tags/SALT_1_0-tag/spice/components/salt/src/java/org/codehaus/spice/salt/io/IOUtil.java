/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 * This product includes software developed by the
�*�Apache Software Foundation (http://www.apache.org/).
 */
package org.codehaus.spice.salt.io;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * General IO Stream manipulation. <p> This class provides static utility
 * methods for input/output operations, particularly buffered copying between
 * sources (<code>InputStream</code>, <code>Reader</code>, <code>String</code>
 * and <code>byte[]</code>) and destinations (<code>OutputStream</code>,
 * <code>Writer</code>, <code>String</code> and <code>byte[]</code>). </p>
 *
 * <p>Unless otherwise noted, these <code>copy</code> methods do <em>not</em>
 * flush or close the streams. Often, doing so would require making non-portable
 * assumptions about the streams' origin and further use. This means that both
 * streams' <code>close()</code> methods must be called after copying. if one
 * omits this step, then the stream resources (sockets, file descriptors) are
 * released when the associated Stream is garbage-collected. It is not a good
 * idea to rely on this mechanism. For a good overview of the distinction
 * between "memory management" and "resource management", see <a
 * href="http://www.unixreview.com/articles/1998/9804/9804ja/ja.htm">this
 * UnixReview article</a></p>
 *
 * <p>For each <code>copy</code> method, a variant is provided that allows the
 * caller to specify the buffer size (the default is 4k). As the buffer size can
 * have a fairly large impact on speed, this may be worth tweaking. Often "large
 * buffer -&gt; faster" does not hold, even for large data transfers.</p>
 *
 * <p>For byte-to-char methods, a <code>copy</code> variant allows the encoding
 * to be selected (otherwise the platform default is used).</p>
 *
 * <p>The <code>copy</code> methods use an internal buffer when copying. It is
 * therefore advisable <em>not</em> to deliberately wrap the stream arguments to
 * the <code>copy</code> methods in <code>Buffered*</code> streams. For example,
 * don't do the following:</p>
 *
 * <code>copy( new BufferedInputStream( in ), new BufferedOutputStream( out )
 * );</code>
 *
 * <p>The rationale is as follows:</p>
 *
 * <p>Imagine that an InputStream's read() is a very expensive operation, which
 * would usually suggest wrapping in a BufferedInputStream. The
 * BufferedInputStream works by issuing infrequent {@link
 * InputStream#read(byte[] b, int off, int len)} requests on the underlying
 * InputStream, to fill an internal buffer, from which further <code>read</code>
 * requests can inexpensively get their data (until the buffer runs out).</p>
 * <p>However, the <code>copy</code> methods do the same thing, keeping an
 * internal buffer, populated by {@link InputStream#read(byte[] b, int off, int
    * len)} requests. Having two buffers (or three if the destination stream is
 * also buffered) is pointless, and the unnecessary buffer management hurts
 * performance slightly (about 3%, according to some simple experiments).</p>
 *
 *
 * Behold, intrepid explorers; a map of this class:
 *
 * Method      Input               Output          Dependency ------      -----
 *              ------          ------- 1     copy        InputStream
 * OutputStream    (primitive) 2     copy        Reader              Writer
 * (primitive)
 *
 * 3     copy        InputStream         Writer          2 4     toString
 * InputStream         String          3 5     toByteArray InputStream
 * byte[]          1
 *
 * 6     copy        Reader              OutputStream    2 7     toString
 * Reader              String          2 8     toByteArray Reader
 * byte[]          6
 *
 * 9     copy        String              OutputStream    2 10    copy
 * String              Writer          (trivial) 11    toByteArray String
 * byte[]          9
 *
 * 12    copy        byte[]              Writer          3 13    toString
 * byte[]              String          12 14    copy        byte[]
 * OutputStream    (trivial)
 *
 *
 * Note that only the first two methods shuffle bytes; the rest use these two,
 * or (if possible) copy using native Java copy methods. As there are method
 * variants to specify buffer size and encoding, each row may correspond to up
 * to 4 methods.
 *
 * @author Peter Donald
 * @author <a href="mailto:jefft@apache.org">Jeff Turner</a>
 * @version $Revision: 1.1 $ $Date: 2003-12-02 02:15:04 $
 */
public final class IOUtil
{
    /** Constant defining max buffer size (4k). */
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    /**
     * Unconditionally close an <code>Reader</code>. Equivalent to {@link
     * java.io.Reader#close()}, except any exceptions will be ignored.
     *
     * @param input A (possibly null) Reader
     */
    public static void shutdownReader( final Reader input )
    {
        if( null == input )
        {
            return;
        }

        try
        {
            input.close();
        }
        catch( final IOException ioe )
        {
        }
    }

    /**
     * Unconditionally close an <code>Writer</code>. Equivalent to {@link
     * java.io.Writer#close()}, except any exceptions will be ignored.
     *
     * @param output A (possibly null) Writer
     */
    public static void shutdownWriter( final Writer output )
    {
        if( null == output )
        {
            return;
        }

        try
        {
            output.close();
        }
        catch( final IOException ioe )
        {
        }
    }

    /**
     * Unconditionally close an <code>OutputStream</code>. Equivalent to {@link
     * java.io.OutputStream#close()}, except any exceptions will be ignored.
     *
     * @param output A (possibly null) OutputStream
     */
    public static void shutdownStream( final OutputStream output )
    {
        if( null == output )
        {
            return;
        }

        try
        {
            output.close();
        }
        catch( final IOException ioe )
        {
        }
    }

    /**
     * Unconditionally close an <code>InputStream</code>. Equivalent to {@link
     * java.io.InputStream#close()}, except any exceptions will be ignored.
     *
     * @param input A (possibly null) InputStream
     */
    public static void shutdownStream( final InputStream input )
    {
        if( null == input )
        {
            return;
        }

        try
        {
            input.close();
        }
        catch( final IOException ioe )
        {
        }
    }

    ///////////////////////////////////////////////////////////////
    // Core copy methods
    ///////////////////////////////////////////////////////////////

    /**
     * Copy bytes from an <code>InputStream</code> to an
     * <code>OutputStream</code>.
     */
    public static void copy( final InputStream input,
                             final OutputStream output )
        throws IOException
    {
        copy( input, output, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Copy bytes from an <code>InputStream</code> to an
     * <code>OutputStream</code>.
     *
     * @param bufferSize Size of internal buffer to use.
     */
    public static void copy( final InputStream input,
                             final OutputStream output,
                             final int bufferSize )
        throws IOException
    {
        final byte[] buffer = new byte[ bufferSize ];
        int n = input.read( buffer );
        while( -1 != n )
        {
            output.write( buffer, 0, n );
            n = input.read( buffer );
        }
    }

    /**
     * Copy chars from a <code>Reader</code> to a <code>Writer</code>.
     */
    public static void copy( final Reader input, final Writer output )
        throws IOException
    {
        copy( input, output, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Copy chars from a <code>Reader</code> to a <code>Writer</code>.
     *
     * @param bufferSize Size of internal buffer to use.
     */
    public static void copy( final Reader input,
                             final Writer output,
                             final int bufferSize )
        throws IOException
    {
        final char[] buffer = new char[ bufferSize ];
        int n = input.read( buffer );
        while( -1 != n )
        {
            output.write( buffer, 0, n );
            n = input.read( buffer );
        }
    }

    ///////////////////////////////////////////////////////////////
    // Derived copy methods
    // InputStream -> *
    ///////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////
    // InputStream -> Writer

    /**
     * Copy and convert bytes from an <code>InputStream</code> to chars on a
     * <code>Writer</code>. The platform's default encoding is used for the
     * byte-to-char conversion.
     */
    public static void copy( final InputStream input, final Writer output )
        throws IOException
    {
        copy( input, output, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Copy and convert bytes from an <code>InputStream</code> to chars on a
     * <code>Writer</code>. The platform's default encoding is used for the
     * byte-to-char conversion.
     *
     * @param bufferSize Size of internal buffer to use.
     */
    public static void copy( final InputStream input,
                             final Writer output,
                             final int bufferSize )
        throws IOException
    {
        final InputStreamReader in = new InputStreamReader( input );
        copy( in, output, bufferSize );
    }

    /**
     * Copy and convert bytes from an <code>InputStream</code> to chars on a
     * <code>Writer</code>, using the specified encoding.
     *
     * @param encoding The name of a supported character encoding. See the <a
     * href="http://www.iana.org/assignments/character-sets">IANA Charset
     * Registry</a> for a list of valid encoding types.
     */
    public static void copy( final InputStream input,
                             final Writer output,
                             final String encoding )
        throws IOException
    {
        final InputStreamReader in = new InputStreamReader( input, encoding );
        copy( in, output );
    }

    /**
     * Copy and convert bytes from an <code>InputStream</code> to chars on a
     * <code>Writer</code>, using the specified encoding.
     *
     * @param encoding The name of a supported character encoding. See the <a
     * href="http://www.iana.org/assignments/character-sets">IANA Charset
     * Registry</a> for a list of valid encoding types.
     * @param bufferSize Size of internal buffer to use.
     */
    public static void copy( final InputStream input,
                             final Writer output,
                             final String encoding,
                             final int bufferSize )
        throws IOException
    {
        final InputStreamReader in = new InputStreamReader( input, encoding );
        copy( in, output, bufferSize );
    }


    ///////////////////////////////////////////////////////////////
    // InputStream -> String

    /**
     * Get the contents of an <code>InputStream</code> as a String. The
     * platform's default encoding is used for the byte-to-char conversion.
     */
    public static String toString( final InputStream input )
        throws IOException
    {
        return toString( input, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Get the contents of an <code>InputStream</code> as a String. The
     * platform's default encoding is used for the byte-to-char conversion.
     *
     * @param bufferSize Size of internal buffer to use.
     */
    public static String toString( final InputStream input,
                                   final int bufferSize )
        throws IOException
    {
        final StringWriter sw = new StringWriter();
        copy( input, sw, bufferSize );
        return sw.toString();
    }

    /**
     * Get the contents of an <code>InputStream</code> as a String.
     *
     * @param encoding The name of a supported character encoding. See the <a
     * href="http://www.iana.org/assignments/character-sets">IANA Charset
     * Registry</a> for a list of valid encoding types.
     */
    public static String toString( final InputStream input,
                                   final String encoding )
        throws IOException
    {
        return toString( input, encoding, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Get the contents of an <code>InputStream</code> as a String.
     *
     * @param encoding The name of a supported character encoding. See the <a
     * href="http://www.iana.org/assignments/character-sets">IANA Charset
     * Registry</a> for a list of valid encoding types.
     * @param bufferSize Size of internal buffer to use.
     */
    public static String toString( final InputStream input,
                                   final String encoding,
                                   final int bufferSize )
        throws IOException
    {
        final StringWriter sw = new StringWriter();
        copy( input, sw, encoding, bufferSize );
        return sw.toString();
    }

    ///////////////////////////////////////////////////////////////
    // InputStream -> byte[]

    /**
     * Get the contents of an <code>InputStream</code> as a
     * <code>byte[]</code>.
     */
    public static byte[] toByteArray( final InputStream input )
        throws IOException
    {
        return toByteArray( input, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Get the contents of an <code>InputStream</code> as a
     * <code>byte[]</code>.
     *
     * @param bufferSize Size of internal buffer to use.
     */
    public static byte[] toByteArray( final InputStream input,
                                      final int bufferSize )
        throws IOException
    {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy( input, output, bufferSize );
        return output.toByteArray();
    }


    ///////////////////////////////////////////////////////////////
    // Derived copy methods
    // Reader -> *
    ///////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////
    // Reader -> OutputStream
    /**
     * Serialize chars from a <code>Reader</code> to bytes on an
     * <code>OutputStream</code>, and flush the <code>OutputStream</code>.
     */
    public static void copy( final Reader input, final OutputStream output )
        throws IOException
    {
        copy( input, output, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Serialize chars from a <code>Reader</code> to bytes on an
     * <code>OutputStream</code>, and flush the <code>OutputStream</code>.
     *
     * @param bufferSize Size of internal buffer to use.
     */
    public static void copy( final Reader input,
                             final OutputStream output,
                             final int bufferSize )
        throws IOException
    {
        final OutputStreamWriter out = new OutputStreamWriter( output );
        copy( input, out, bufferSize );
        // NOTE: Unless anyone is planning on rewriting OutputStreamWriter, we have to flush
        // here.
        out.flush();
    }

    ///////////////////////////////////////////////////////////////
    // Reader -> String
    /**
     * Get the contents of a <code>Reader</code> as a String.
     */
    public static String toString( final Reader input )
        throws IOException
    {
        return toString( input, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Get the contents of a <code>Reader</code> as a String.
     *
     * @param bufferSize Size of internal buffer to use.
     */
    public static String toString( final Reader input, final int bufferSize )
        throws IOException
    {
        final StringWriter sw = new StringWriter();
        copy( input, sw, bufferSize );
        return sw.toString();
    }


    ///////////////////////////////////////////////////////////////
    // Reader -> byte[]
    /**
     * Get the contents of a <code>Reader</code> as a <code>byte[]</code>.
     */
    public static byte[] toByteArray( final Reader input )
        throws IOException
    {
        return toByteArray( input, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Get the contents of a <code>Reader</code> as a <code>byte[]</code>.
     *
     * @param bufferSize Size of internal buffer to use.
     */
    public static byte[] toByteArray( final Reader input,
                                      final int bufferSize )
        throws IOException
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy( input, output, bufferSize );
        return output.toByteArray();
    }


    ///////////////////////////////////////////////////////////////
    // Derived copy methods
    // String -> *
    ///////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////
    // String -> OutputStream

    /**
     * Serialize chars from a <code>String</code> to bytes on an
     * <code>OutputStream</code>, and flush the <code>OutputStream</code>.
     */
    public static void copy( final String input, final OutputStream output )
        throws IOException
    {
        copy( input, output, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Serialize chars from a <code>String</code> to bytes on an
     * <code>OutputStream</code>, and flush the <code>OutputStream</code>.
     *
     * @param bufferSize Size of internal buffer to use.
     */
    public static void copy( final String input,
                             final OutputStream output,
                             final int bufferSize )
        throws IOException
    {
        final StringReader in = new StringReader( input );
        final OutputStreamWriter out = new OutputStreamWriter( output );
        copy( in, out, bufferSize );
        // NOTE: Unless anyone is planning on rewriting OutputStreamWriter, we have to flush
        // here.
        out.flush();
    }



    ///////////////////////////////////////////////////////////////
    // String -> Writer

    /**
     * Copy chars from a <code>String</code> to a <code>Writer</code>.
     */
    public static void copy( final String input, final Writer output )
        throws IOException
    {
        output.write( input );
    }

    ///////////////////////////////////////////////////////////////
    // String -> byte[]
    /**
     * Get the contents of a <code>String</code> as a <code>byte[]</code>.
     */
    public static byte[] toByteArray( final String input )
        throws IOException
    {
        return toByteArray( input, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Get the contents of a <code>String</code> as a <code>byte[]</code>.
     *
     * @param bufferSize Size of internal buffer to use.
     */
    public static byte[] toByteArray( final String input,
                                      final int bufferSize )
        throws IOException
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy( input, output, bufferSize );
        return output.toByteArray();
    }



    ///////////////////////////////////////////////////////////////
    // Derived copy methods
    // byte[] -> *
    ///////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////
    // byte[] -> Writer

    /**
     * Copy and convert bytes from a <code>byte[]</code> to chars on a
     * <code>Writer</code>. The platform's default encoding is used for the
     * byte-to-char conversion.
     */
    public static void copy( final byte[] input, final Writer output )
        throws IOException
    {
        copy( input, output, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Copy and convert bytes from a <code>byte[]</code> to chars on a
     * <code>Writer</code>. The platform's default encoding is used for the
     * byte-to-char conversion.
     *
     * @param bufferSize Size of internal buffer to use.
     */
    public static void copy( final byte[] input,
                             final Writer output,
                             final int bufferSize )
        throws IOException
    {
        final ByteArrayInputStream in = new ByteArrayInputStream( input );
        copy( in, output, bufferSize );
    }

    /**
     * Copy and convert bytes from a <code>byte[]</code> to chars on a
     * <code>Writer</code>, using the specified encoding.
     *
     * @param encoding The name of a supported character encoding. See the <a
     * href="http://www.iana.org/assignments/character-sets">IANA Charset
     * Registry</a> for a list of valid encoding types.
     */
    public static void copy( final byte[] input,
                             final Writer output,
                             final String encoding )
        throws IOException
    {
        final ByteArrayInputStream in = new ByteArrayInputStream( input );
        copy( in, output, encoding );
    }

    /**
     * Copy and convert bytes from a <code>byte[]</code> to chars on a
     * <code>Writer</code>, using the specified encoding.
     *
     * @param encoding The name of a supported character encoding. See the <a
     * href="http://www.iana.org/assignments/character-sets">IANA Charset
     * Registry</a> for a list of valid encoding types.
     * @param bufferSize Size of internal buffer to use.
     */
    public static void copy( final byte[] input,
                             final Writer output,
                             final String encoding,
                             final int bufferSize )
        throws IOException
    {
        final ByteArrayInputStream in = new ByteArrayInputStream( input );
        copy( in, output, encoding, bufferSize );
    }


    ///////////////////////////////////////////////////////////////
    // byte[] -> String

    /**
     * Get the contents of a <code>byte[]</code> as a String. The platform's
     * default encoding is used for the byte-to-char conversion.
     */
    public static String toString( final byte[] input )
        throws IOException
    {
        return toString( input, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Get the contents of a <code>byte[]</code> as a String. The platform's
     * default encoding is used for the byte-to-char conversion.
     *
     * @param bufferSize Size of internal buffer to use.
     */
    public static String toString( final byte[] input, final int bufferSize )
        throws IOException
    {
        final StringWriter sw = new StringWriter();
        copy( input, sw, bufferSize );
        return sw.toString();
    }

    /**
     * Get the contents of a <code>byte[]</code> as a String.
     *
     * @param encoding The name of a supported character encoding. See the <a
     * href="http://www.iana.org/assignments/character-sets">IANA Charset
     * Registry</a> for a list of valid encoding types.
     */
    public static String toString( final byte[] input, final String encoding )
        throws IOException
    {
        return toString( input, encoding, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Get the contents of a <code>byte[]</code> as a String.
     *
     * @param encoding The name of a supported character encoding. See the <a
     * href="http://www.iana.org/assignments/character-sets">IANA Charset
     * Registry</a> for a list of valid encoding types.
     * @param bufferSize Size of internal buffer to use.
     */
    public static String toString( final byte[] input,
                                   final String encoding,
                                   final int bufferSize )
        throws IOException
    {
        final StringWriter sw = new StringWriter();
        copy( input, sw, encoding, bufferSize );
        return sw.toString();
    }


    ///////////////////////////////////////////////////////////////
    // byte[] -> OutputStream

    /**
     * Copy bytes from a <code>byte[]</code> to an <code>OutputStream</code>.
     */
    public static void copy( final byte[] input, final OutputStream output )
        throws IOException
    {
        output.write( input );
    }

    /**
     * Compare the contents of two Streams to determine if they are equal or
     * not.
     *
     * @param input1 the first stream
     * @param input2 the second stream
     * @return true if the content of the streams are equal or they both don't
     *         exist, false otherwise
     */
    public static boolean contentEquals( final InputStream input1,
                                         final InputStream input2 )
        throws IOException
    {
        final InputStream bufferedInput1 = new BufferedInputStream( input1 );
        final InputStream bufferedInput2 = new BufferedInputStream( input2 );

        int ch = bufferedInput1.read();
        while( -1 != ch )
        {
            final int ch2 = bufferedInput2.read();
            if( ch != ch2 )
            {
                return false;
            }
            ch = bufferedInput1.read();
        }

        final int ch2 = bufferedInput2.read();
        if( -1 != ch2 )
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}