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
package org.realityforge.salt.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This is used to test FileUtil for correctness.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 */
public final class FileUtilTestCase
    extends TestCase
{
    private final int FILE1_SIZE = 1;
    private final int FILE2_SIZE = 1024 * 4 + 1;

    private final File m_testDirectory;
    private final File m_testFile1;
    private final File m_testFile2;

    public FileUtilTestCase( final String name )
        throws IOException
    {
        super( name );

        m_testDirectory = ( new File( "test/io/" ) ).getAbsoluteFile();
        if( !m_testDirectory.exists() )
        {
            m_testDirectory.mkdirs();
        }

        m_testFile1 = new File( m_testDirectory, "file1-test.txt" );
        m_testFile2 = new File( m_testDirectory, "file2-test.txt" );

        createFile( m_testFile1, FILE1_SIZE );
        createFile( m_testFile2, FILE2_SIZE );
    }

    private void createFile( final File file, final long size )
        throws IOException
    {
        final BufferedOutputStream output =
            new BufferedOutputStream( new FileOutputStream( file ) );

        for( int i = 0; i < size; i++ )
        {
            output.write( (byte)'X' );
        }

        output.close();
    }

    public static Test suite()
        throws IOException
    {
        final TestSuite suite = new TestSuite();
        suite.addTest( new FileUtilTestCase( "testCopyFile1" ) );
        suite.addTest( new FileUtilTestCase( "testCopyFile2" ) );
        suite.addTest( new FileUtilTestCase( "testForceDeleteAFile1" ) );
        suite.addTest( new FileUtilTestCase( "testForceDeleteAFile2" ) );
        suite.addTest( new FileUtilTestCase( "testCopyFile1ToDir" ) );
        suite.addTest( new FileUtilTestCase( "testCopyFile2ToDir" ) );
        suite.addTest( new FileUtilTestCase( "testForceDeleteDir" ) );
        suite.addTest( new FileUtilTestCase( "testResolveFileDotDot" ) );
        suite.addTest( new FileUtilTestCase( "testResolveFileDot" ) );
        suite.addTest( new FileUtilTestCase( "testNormalize" ) );
        suite.addTest( new FileUtilTestCase( "testCatPath" ) );
        return suite;
    }

    class MockFile extends File
    {
        boolean _isDirectory;
        File[] children;

        public MockFile( String pathname )
        {
            super( pathname );
            _isDirectory = false;
        }

        public MockFile( String pathname, boolean isDirectory )
        {
            super( pathname );
            _isDirectory = isDirectory;
        }

        public boolean isDirectory()
        {
            return _isDirectory;
        }

        public File[] listFiles()
        {
            return children;
        }
    }

    public void testResolveFileSet()
        throws Exception
    {
        final MockFile f1 = new MockFile( "file1.txt" );
        final MockFile f2 = new MockFile( "file2.txt" );
        final MockFile f3 = new MockFile( "file3.dat" );
        final MockFile f4 = new MockFile( "file4.dbg" );
        final MockFile d1 = new MockFile( "dir", true );
        final MockFile d2 = new MockFile( "dir2", true );
        d1.children = new File[]{f2, f3, d2};
        d2.children = new File[]{f1, f4};
        final PathMatcher matcher = new PathMatcher( new String[]{"**/*.txt"}, new String[ 0 ] );
        final File[] files = FileUtil.resolveFileSet( d1, matcher );
        assertEquals( "Files.length", 2, files.length );
        assertEquals( "Files[0]", f2, files[ 0 ] );
        assertEquals( "Files[1]", f1, files[ 1 ] );
    }

    public void testResolveFileSetWithBadParam()
        throws Exception
    {
        final MockFile f1 = new MockFile( "file1.txt" );
        final PathMatcher matcher = new PathMatcher( new String[]{"**/*.txt"}, new String[ 0 ] );
        try
        {
            FileUtil.resolveFileSet( f1, matcher );
            fail( "Tried to scan a non-dir" );
        }
        catch( final IllegalArgumentException iae )
        {
        }
    }

    public void testCopyFile1()
        throws Exception
    {
        final File destination = new File( m_testDirectory, "copy1.txt" );
        FileUtil.copyFile( m_testFile1, destination );
        assertTrue( "Check Exist", destination.exists() );
        assertTrue( "Check Full copy", destination.length() == FILE1_SIZE );
    }

    public void testCopyFile2()
        throws Exception
    {
        final File destination = new File( m_testDirectory, "copy2.txt" );
        FileUtil.copyFile( m_testFile2, destination );
        assertTrue( "Check Exist", destination.exists() );
        assertTrue( "Check Full copy", destination.length() == FILE2_SIZE );
    }

    public void testForceDeleteAFile1()
        throws Exception
    {
        final File destination = new File( m_testDirectory, "copy1.txt" );
        destination.createNewFile();
        assertTrue( "Copy1.txt doesn't exist to delete", destination.exists() );
        FileUtil.forceDelete( destination );
        assertTrue( "Check No Exist", !destination.exists() );
    }

    public void testForceDeleteAFile2()
        throws Exception
    {
        final File destination = new File( m_testDirectory, "copy2.txt" );
        destination.createNewFile();
        assertTrue( "Copy2.txt doesn't exist to delete", destination.exists() );
        FileUtil.forceDelete( destination );
        assertTrue( "Check No Exist", !destination.exists() );
    }

    public void testCopyFile1ToDir()
        throws Exception
    {
        final File directory = new File( m_testDirectory, "subdir" );
        if( !directory.exists() ) directory.mkdirs();
        final File destination = new File( directory, "file1-test.txt" );
        FileUtil.copyFileToDirectory( m_testFile1, directory );
        assertTrue( "Check Exist", destination.exists() );
        assertTrue( "Check Full copy", destination.length() == FILE1_SIZE );
    }

    public void testCopyFile2ToDir()
        throws Exception
    {
        final File directory = new File( m_testDirectory, "subdir" );
        if( !directory.exists() ) directory.mkdirs();
        final File destination = new File( directory, "file2-test.txt" );
        FileUtil.copyFileToDirectory( m_testFile2, directory );
        assertTrue( "Check Exist", destination.exists() );
        assertTrue( "Check Full copy", destination.length() == FILE2_SIZE );
    }

    public void testForceDeleteDir()
        throws Exception
    {
        FileUtil.forceDelete( m_testDirectory.getParentFile() );
        assertTrue( "Check No Exist", !m_testDirectory.getParentFile().exists() );
    }

    public void testResolveFileDotDot()
        throws Exception
    {
        final File file = FileUtil.resolveFile( m_testDirectory, ".." );
        assertEquals( "Check .. operator", file, m_testDirectory.getParentFile() );
    }

    public void testResolveFileDot()
        throws Exception
    {
        final File file = FileUtil.resolveFile( m_testDirectory, "." );
        assertEquals( "Check . operator", file, m_testDirectory );
    }

    public void testNormalize()
        throws Exception
    {
        final String[] src =
            {
                "", "/", "///", "/foo", "/foo//", "/./", "/foo/./", "/foo/./bar",
                "/foo/../bar", "/foo/../bar/../baz", "/foo/bar/../../baz", "/././",
                "/foo/./../bar", "/foo/.././bar/", "//foo//./bar", "/../",
                "/foo/../../"
            };

        final String[] dest =
            {
                "", "/", "/", "/foo", "/foo/", "/", "/foo/", "/foo/bar", "/bar",
                "/baz", "/baz", "/", "/bar", "/bar/", "/foo/bar", null, null
            };

        assertEquals( "Oops, test writer goofed", src.length, dest.length );

        for( int i = 0; i < src.length; i++ )
        {
            assertEquals( "Check if '" + src[ i ] + "' normalized to '" + dest[ i ] + "'",
                          dest[ i ], FileUtil.normalize( src[ i ] ) );
        }
    }

    public void testCatPath()
    {
        assertEquals( "/a/b/d", FileUtil.catPath( "/a/b/c", "d" ) );
        assertEquals( "/a/d", FileUtil.catPath( "/a/b/c", "../d" ) );

        assertEquals( "a/b/d", FileUtil.catPath( "a/b/c", "d" ) );
        assertEquals( "a/d", FileUtil.catPath( "a/b/c", "../d" ) );

        assertEquals( "b", FileUtil.catPath( "a", "b" ) );
    }
}
