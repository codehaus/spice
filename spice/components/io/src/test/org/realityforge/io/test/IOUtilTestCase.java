/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 * This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/).
 */
package org.realityforge.io.test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.io.InputStream;
import java.io.Reader;
import java.util.Arrays;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.realityforge.io.FileUtil;
import org.realityforge.io.IOUtil;

/**
 * This is used to test IOUtil for correctness. The following checks are performed:
 * <ul>
 *   <li>The return must not be null, must be the same type and equals() to the method's second arg</li>
 *   <li>All bytes must have been read from the source (available() == 0)</li>
 *   <li>The source and destination content must be identical (byte-wise comparison check)</li>
 *   <li>The output stream must not have been closed (a byte/char is written to test this, and
 *   subsequent size checked)</li>
 * </ul>
 * Due to interdependencies in IOUtils and IOUtilsTestlet, one bug may cause
 * multiple tests to fail.
 *
 * @author <a href="mailto:jefft@apache.org">Jeff Turner</a>
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 */
public final class IOUtilTestCase
    extends TestCase
{
    /*
     * Note: this is not particularly beautiful code. A better way to check for
     * flush and close status would be to implement "trojan horse" wrapper
     * implementations of the various stream classes, which set a flag when
     * relevant methods are called. (JT)
     */
    private final int FILE_SIZE = 1024 * 4 + 1;

    private File m_testDirectory;
    private File m_testFile;

    public void setUp()
    {
        try
        {
            m_testDirectory = ( new File( "test/io/" ) ).getAbsoluteFile();
            if( !m_testDirectory.exists() )
            {
                m_testDirectory.mkdirs();
            }

            m_testFile = new File( m_testDirectory, "file2-test.txt" );

            createFile( m_testFile, FILE_SIZE );
        }
        catch( IOException ioe )
        {
            throw new RuntimeException( "Can't run this test because environment could not be built" );
        }
    }

    public void tearDown()
    {
        try
        {
            FileUtil.deleteDirectory( "test" );
        }
        catch( IOException ioe )
        {
            // Ignore, because by this time, it is too late.
        }
    }

    public IOUtilTestCase( String name )
    {
        super( name );
    }

    public void testShutdownReader()
        throws Exception
    {
        final File file = newFile( "reader1.txt" );
        createFile( file, 100 );
        IOUtil.shutdownReader( new FileReader( file ) );
    }

    public void testShutdownNullReader()
    {
        IOUtil.shutdownReader( null );
    }

    public void testShutdownWriter()
        throws Exception
    {
        final File file = newFile( "reader2.txt" );
        IOUtil.shutdownWriter( new FileWriter( file ) );
    }

    public void testShutdownNullWriter()
        throws Exception
    {
        IOUtil.shutdownWriter( null );
    }

    public void testShutdownNullStream()
    {
        IOUtil.shutdownStream( (OutputStream)null );
    }

    public void testShutdownNullInputStream()
    {
        IOUtil.shutdownStream( (InputStream)null );
    }

    public void testInputStreamToOutputStream()
        throws Exception
    {
        final File destination = newFile( "copy1.txt" );
        final FileInputStream fin = new FileInputStream( m_testFile );
        final FileOutputStream fout = new FileOutputStream( destination );

        IOUtil.copy( fin, fout );
        assertTrue( "Not all bytes were read", fin.available() == 0 );
        fout.flush();

        checkFile( destination );
        checkWrite( fout );
        fout.close();
        fin.close();
        deleteFile( destination );
    }

    public void testInputStreamToWriter()
        throws Exception
    {
        final File destination = newFile( "copy2.txt" );
        final FileInputStream fin = new FileInputStream( m_testFile );
        final FileWriter fout = new FileWriter( destination );

        IOUtil.copy( fin, fout );

        assertTrue( "Not all bytes were read", fin.available() == 0 );
        fout.flush();

        checkFile( destination );
        checkWrite( fout );
        fout.close();
        fin.close();
        deleteFile( destination );
    }

    public void testInputStreamToWriterWithEncoding()
        throws Exception
    {
        final File destination = newFile( "copy2.txt" );
        final FileInputStream fin = new FileInputStream( m_testFile );
        final FileWriter fout = new FileWriter( destination );

        IOUtil.copy( fin, fout, "US-ASCII" );

        assertTrue( "Not all bytes were read", fin.available() == 0 );
        fout.flush();

        checkFile( destination );
        checkWrite( fout );
        fout.close();
        fin.close();
        deleteFile( destination );
    }

    public void testInputStreamToWriterWithEncodingAndBuffering()
        throws Exception
    {
        final File destination = newFile( "copy2.txt" );
        final FileInputStream fin = new FileInputStream( m_testFile );
        final FileWriter fout = new FileWriter( destination );

        IOUtil.copy( fin, fout, "US-ASCII", 100 );

        assertTrue( "Not all bytes were read", fin.available() == 0 );
        fout.flush();

        checkFile( destination );
        checkWrite( fout );
        fout.close();
        fin.close();
        deleteFile( destination );
    }

    public void testInputStreamToString()
        throws Exception
    {
        final FileInputStream fin = new FileInputStream( m_testFile );
        final String out = IOUtil.toString( fin );
        assertNotNull( out );
        assertTrue( "Not all bytes were read", fin.available() == 0 );
        assertTrue( "Wrong output size: out.length()=" + out.length() +
                    "!=" + FILE_SIZE, out.length() == FILE_SIZE );
        fin.close();
    }

    public void testInputStreamToStringWithEncoding()
        throws Exception
    {
        final FileInputStream fin = new FileInputStream( m_testFile );
        final String out = IOUtil.toString( fin, "US-ASCII" );
        assertNotNull( out );
        assertTrue( "Not all bytes were read", fin.available() == 0 );
        assertTrue( "Wrong output size: out.length()=" + out.length() +
                    "!=" + FILE_SIZE, out.length() == FILE_SIZE );
        fin.close();
    }

    public void testInputStreamToStringWithEncodingAndBuffer()
        throws Exception
    {
        final FileInputStream fin = new FileInputStream( m_testFile );
        final String out = IOUtil.toString( fin, "US-ASCII", 100 );
        assertNotNull( out );
        assertTrue( "Not all bytes were read", fin.available() == 0 );
        assertTrue( "Wrong output size: out.length()=" + out.length() +
                    "!=" + FILE_SIZE, out.length() == FILE_SIZE );
        fin.close();
    }

    public void testReaderToOutputStream()
        throws Exception
    {
        final File destination = newFile( "copy3.txt" );
        final FileReader fin = new FileReader( m_testFile );
        final FileOutputStream fout = new FileOutputStream( destination );
        IOUtil.copy( fin, fout );
        //Note: this method *does* flush. It is equivalent to:
        //  final OutputStreamWriter _out = new OutputStreamWriter(fout);
        //  IOUtil.copy( fin, _out, 4096 ); // copy( Reader, Writer, int );
        //  _out.flush();
        //  out = fout;

        // Note: rely on the method to flush
        checkFile( destination );
        checkWrite( fout );
        fout.close();
        fin.close();
        deleteFile( destination );
    }

    public void testReaderToWriter()
        throws Exception
    {
        final File destination = newFile( "copy4.txt" );
        final FileReader fin = new FileReader( m_testFile );
        final FileWriter fout = new FileWriter( destination );
        IOUtil.copy( fin, fout );

        fout.flush();
        checkFile( destination );
        checkWrite( fout );
        fout.close();
        fin.close();
        deleteFile( destination );
    }

    public void testReaderToString()
        throws Exception
    {
        final FileReader fin = new FileReader( m_testFile );
        final String out = IOUtil.toString( fin );
        assertNotNull( out );
        assertTrue( "Wrong output size: out.length()=" +
                    out.length() + "!=" + FILE_SIZE,
                    out.length() == FILE_SIZE );
        fin.close();
    }

    public void testStringToOutputStream()
        throws Exception
    {
        final File destination = newFile( "copy5.txt" );
        final FileReader fin = new FileReader( m_testFile );
        // Create our String. Rely on testReaderToString() to make sure this is valid.
        final String str = IOUtil.toString( fin );
        final FileOutputStream fout = new FileOutputStream( destination );
        IOUtil.copy( str, fout );
        //Note: this method *does* flush. It is equivalent to:
        //  final OutputStreamWriter _out = new OutputStreamWriter(fout);
        //  IOUtil.copy( str, _out, 4096 ); // copy( Reader, Writer, int );
        //  _out.flush();
        //  out = fout;
        // note: we don't flush here; this IOUtils method does it for us

        checkFile( destination );
        checkWrite( fout );
        fout.close();
        fin.close();
        deleteFile( destination );
    }

    public void testStringToWriter()
        throws Exception
    {
        final File destination = newFile( "copy6.txt" );
        FileReader fin = new FileReader( m_testFile );
        // Create our String. Rely on testReaderToString() to make sure this is valid.
        final String str = IOUtil.toString( fin );
        final FileWriter fout = new FileWriter( destination );
        IOUtil.copy( str, fout );
        fout.flush();

        checkFile( destination );
        checkWrite( fout );
        fout.close();
        fin.close();

        deleteFile( destination );
    }

    public void testInputStreamToByteArray()
        throws Exception
    {
        final FileInputStream fin = new FileInputStream( m_testFile );
        final byte[] out = IOUtil.toByteArray( fin );
        assertNotNull( out );
        assertTrue( "Not all bytes were read", fin.available() == 0 );
        assertTrue( "Wrong output size: out.length=" + out.length +
                    "!=" + FILE_SIZE, out.length == FILE_SIZE );
        assertEqualContent( out, m_testFile );
        fin.close();
    }

    public void testReaderToByteArray()
        throws Exception
    {
        final Reader reader = new FileReader( m_testFile );
        final byte[] out = IOUtil.toByteArray( reader );

        assertNotNull( out );
        assertTrue( "Wrong output size: out.length=" + out.length +
                    "!=" + FILE_SIZE, out.length == FILE_SIZE );
        assertEqualContent( out, m_testFile );
        reader.close();
    }

    public void testReaderToByteArrayWithBuffering()
        throws Exception
    {
        final Reader reader = new FileReader( m_testFile );
        final byte[] out = IOUtil.toByteArray( reader, 100 );

        assertNotNull( out );
        assertTrue( "Wrong output size: out.length=" + out.length +
                    "!=" + FILE_SIZE, out.length == FILE_SIZE );
        assertEqualContent( out, m_testFile );
        reader.close();
    }

    public void testStringToByteArray()
        throws Exception
    {
        final FileReader fin = new FileReader( m_testFile );

        // Create our String. Rely on testReaderToString() to make sure this is valid.
        final String str = IOUtil.toString( fin );

        final byte[] out = IOUtil.toByteArray( str );
        assertEqualContent( str.getBytes(), out );
        fin.close();
    }

    public void testByteArrayToWriter()
        throws Exception
    {
        final File destination = newFile( "copy7.txt" );
        final FileWriter fout = new FileWriter( destination );
        final FileInputStream fin = new FileInputStream( m_testFile );

        // Create our byte[]. Rely on testInputStreamToByteArray() to make sure this is valid.
        final byte[] in = IOUtil.toByteArray( fin );
        IOUtil.copy( in, fout );
        fout.flush();
        checkFile( destination );
        checkWrite( fout );
        fout.close();
        fin.close();
        deleteFile( destination );
    }

    public void testByteArrayToWriterWithEncoding()
        throws Exception
    {
        final File destination = newFile( "copy7.txt" );
        final FileWriter fout = new FileWriter( destination );
        final FileInputStream fin = new FileInputStream( m_testFile );

        // Create our byte[]. Rely on testInputStreamToByteArray() to make sure this is valid.
        final byte[] in = IOUtil.toByteArray( fin );
        IOUtil.copy( in, fout, "US-ASCII" );
        fout.flush();
        checkFile( destination );
        checkWrite( fout );
        fout.close();
        fin.close();
        deleteFile( destination );
    }

    public void testByteArrayToWriterWithEncodingNBuffering()
        throws Exception
    {
        final File destination = newFile( "copy7.txt" );
        final FileWriter fout = new FileWriter( destination );
        final FileInputStream fin = new FileInputStream( m_testFile );

        // Create our byte[]. Rely on testInputStreamToByteArray() to make sure this is valid.
        final byte[] in = IOUtil.toByteArray( fin );
        IOUtil.copy( in, fout, "US-ASCII", 10 );
        fout.flush();
        checkFile( destination );
        checkWrite( fout );
        fout.close();
        fin.close();
        deleteFile( destination );
    }

    public void testByteArrayToString()
        throws Exception
    {
        final FileInputStream fin = new FileInputStream( m_testFile );
        final byte[] in = IOUtil.toByteArray( fin );
        // Create our byte[]. Rely on testInputStreamToByteArray() to make sure this is valid.
        String str = IOUtil.toString( in );
        assertEqualContent( in, str.getBytes() );
        fin.close();
    }

    public void testByteArrayToStringWithEncoding()
        throws Exception
    {
        final FileInputStream fin = new FileInputStream( m_testFile );
        final byte[] in = IOUtil.toByteArray( fin );
        // Create our byte[]. Rely on testInputStreamToByteArray() to make sure this is valid.
        String str = IOUtil.toString( in, "US-ASCII" );
        assertEqualContent( in, str.getBytes() );
        fin.close();
    }

    public void testByteArrayToStringWithEncodingNBUffering()
        throws Exception
    {
        final FileInputStream fin = new FileInputStream( m_testFile );
        final byte[] in = IOUtil.toByteArray( fin );
        // Create our byte[]. Rely on testInputStreamToByteArray() to make sure this is valid.
        String str = IOUtil.toString( in, "US-ASCII", 12 );
        assertEqualContent( in, str.getBytes() );
        fin.close();
    }

    public void testByteArrayToOutputStream()
        throws Exception
    {
        final File destination = newFile( "copy8.txt" );
        final FileOutputStream fout = new FileOutputStream( destination );
        final FileInputStream fin = new FileInputStream( m_testFile );

        // Create our byte[]. Rely on testInputStreamToByteArray() to make sure this is valid.
        final byte[] in = IOUtil.toByteArray( fin );

        IOUtil.copy( in, fout );

        fout.flush();

        checkFile( destination );
        checkWrite( fout );
        fout.close();
        fin.close();
        deleteFile( destination );
    }

    public void testContentEquals()
        throws Exception
    {
        final FileInputStream fin1 = new FileInputStream( m_testFile );
        final FileInputStream fin2 = new FileInputStream( m_testFile );

        assertTrue( "Equal content", IOUtil.contentEquals( fin1, fin2 ) );
    }

    public void testContentNotEquals()
        throws Exception
    {
        final FileInputStream fin1 = new FileInputStream( m_testFile );
        final File file = newFile( "Blah.txt" );
        createFile( file, 123 );
        final FileInputStream fin2 = new FileInputStream( file );

        assertTrue( "Equal not content", !IOUtil.contentEquals( fin1, fin2 ) );
    }

    public void testContentNotEqualsAtEnd()
        throws Exception
    {
        final File file1 = newFile( "Blah.txt" );
        final FileOutputStream out1 = new FileOutputStream( file1 );
        out1.write( 'a' );
        out1.close();

        final File file2 = newFile( "Blah2.txt" );
        final FileOutputStream out2 = new FileOutputStream( file2 );
        out2.write( 'a' );
        out2.write( 'b' );
        out2.close();

        final FileInputStream fin1 = new FileInputStream( file1 );
        final FileInputStream fin2 = new FileInputStream( file2 );

        assertTrue( "Equal not content", !IOUtil.contentEquals( fin1, fin2 ) );
    }

    //////////////////////////////////////////////////////
    // xxxxxxxxx


    private File newFile( String filename )
        throws Exception
    {
        final File destination = new File( m_testDirectory, filename );
        assertTrue( filename + "Test output data file shouldn't previously exist",
                    !destination.exists() );

        return destination;
    }

    private void checkFile( final File file )
        throws Exception
    {
        assertTrue( "Check existence of output file", file.exists() );
        assertEqualContent( m_testFile, file );
    }

    private void checkWrite( final OutputStream output )
        throws Exception
    {
        try
        {
            new PrintStream( output ).write( 0 );
        }
        catch( final Throwable t )
        {
            throw new AssertionFailedError( "The copy() method closed the stream " +
                                            "when it shouldn't have. " + t.getMessage() );
        }
    }

    private void checkWrite( final Writer output )
        throws Exception
    {
        try
        {
            new PrintWriter( output ).write( 'a' );
        }
        catch( final Throwable t )
        {
            throw new AssertionFailedError( "The copy() method closed the stream " +
                                            "when it shouldn't have. " + t.getMessage() );
        }
    }

    private void deleteFile( final File file )
        throws Exception
    {
        assertTrue( "Wrong output size: file.length()=" +
                    file.length() + "!=" + FILE_SIZE + 1,
                    file.length() == FILE_SIZE + 1 );

        //assertTrue( "File would not delete", (file.delete() || ( !file.exists() )));
    }

    private void createFile( final File file, final long size )
        throws IOException
    {
        final BufferedOutputStream output =
            new BufferedOutputStream( new FileOutputStream( file ) );

        for( int i = 0; i < size; i++ )
        {
            output.write( (byte)( i % 128 ) ); // nice varied byte pattern compatible with Readers and Writers
        }

        output.close();
    }

    /** Assert that the contents of two byte arrays are the same. */
    private void assertEqualContent( final byte[] b0, final byte[] b1 )
    {
        assertTrue( "Content not equal according to java.util.Arrays#equals()", Arrays.equals( b0, b1 ) );
    }

    /** Assert that the content of two files is the same. */
    private void assertEqualContent( final File f0, final File f1 )
        throws IOException
    {
        final FileInputStream is0 = new FileInputStream( f0 );
        final FileInputStream is1 = new FileInputStream( f1 );
        final byte[] buf0 = new byte[ FILE_SIZE ];
        final byte[] buf1 = new byte[ FILE_SIZE ];
        int n0 = 0;
        int n1 = 0;

        while( -1 != n0 )
        {
            n0 = is0.read( buf0 );
            n1 = is1.read( buf1 );
            assertTrue( "The files " + f0 + " and " + f1 +
                        " have differing number of bytes available (" + n0 +
                        " vs " + n1 + ")", ( n0 == n1 ) );

            assertTrue( "The files " + f0 + " and " + f1 +
                        " have different content", Arrays.equals( buf0, buf1 ) );
        }
    }

    /** Assert that the content of a file is equal to that in a byte[]. */
    private void assertEqualContent( final byte[] b0, final File file )
        throws IOException
    {
        final FileInputStream is = new FileInputStream( file );
        byte[] b1 = new byte[ b0.length ];
        int numRead = is.read( b1 );
        assertTrue( "Different number of bytes", numRead == b0.length && is.available() == 0 );
        for( int i = 0;
             i < numRead;
             assertTrue( "Byte " + i + " differs (" + b0[ i ] + " != " + b1[ i ] + ")", b0[ i ] == b1[ i ] ), i++
            )
            ;
    }
}
