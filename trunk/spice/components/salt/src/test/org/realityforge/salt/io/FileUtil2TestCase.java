/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.salt.io;

import java.io.File;
import java.io.IOException;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.net.URL;

import junit.framework.TestCase;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.4 $ $Date: 2003-09-13 09:42:05 $
 */
public class FileUtil2TestCase
   extends TestCase
{
   public FileUtil2TestCase( String name )
   {
      super( name );
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
      catch ( final IllegalArgumentException iae )
      {
      }
   }

   public void testNormalize()
      throws Exception
   {
      doTestNormalize( "", "" );
      doTestNormalize( "/", "/" );
      doTestNormalize( "///", "/" );
      doTestNormalize( "/foo", "/foo" );
      doTestNormalize( "/foo//", "/foo/" );
      doTestNormalize( "/./", "/" );
      doTestNormalize( "/foo/./", "/foo/" );
      doTestNormalize( "/foo/./bar", "/foo/bar" );
      doTestNormalize( "/foo/../bar", "/bar" );
      doTestNormalize( "/foo/../bar/../baz", "/baz" );
      doTestNormalize( "/foo/bar/../../baz", "/baz" );
      doTestNormalize( "/././", "/" );
      doTestNormalize( "/foo/./../bar", "/bar" );
      doTestNormalize( "/foo/.././bar/", "/bar/" );
      doTestNormalize( "/../", null );
      doTestNormalize( "/foo/../../", null );
   }

   private void doTestNormalize( final String input, final String expected )
   {
      assertEquals( "Check if '" + input + "' normalized to '" + expected + "'",
                    expected, FileUtil.normalize( input ) );
   }

   public void testCatPath()
   {
      doCatPathTest( "/a/b/c", "../d", "/a/d" );
      doCatPathTest( "a", "b", "b" );
      doCatPathTest( "/a/b/c", "d", "/a/b/d" );
      doCatPathTest( "a/b/c", "d", "a/b/d" );
      doCatPathTest( "a/b/c", "../d", "a/d" );
   }

   private void doCatPathTest( final String lhs,
                               final String rhs,
                               final String result )
   {
      final String message = "CatPath: " + lhs + " + " + rhs + " == " + result;
      assertEquals( message, result, FileUtil.catPath( lhs, rhs ) );
   }

   public void testResolveFileDotDot()
      throws Exception
   {
      final File dir = genTestDirectory();
      final File file = FileUtil.resolveFile( dir, ".." );
      assertEquals( "Check .. operator", file, dir.getParentFile() );
   }

   public void testResolveFileDot()
      throws Exception
   {
      final File dir = genTestDirectory();
      final File file = FileUtil.resolveFile( dir, "." );
      assertEquals( "Check . operator", file, dir );
   }

   public void testCopyFileToFile()
      throws Exception
   {
      final File dir = genTestDirectory();
      final File source = genFile( dir, 2 );
      final File destination = new File( dir, "destination.txt" );
      FileUtil.copyFile( source, destination );
      assertTrue( "destination.exists()", destination.exists() );
      assertEquals( "destination.length", 2, destination.length() );
   }

   public void testCopyFileToDir()
      throws Exception
   {
      final File dir = genTestDirectory();
      final File dest = genTestDirectory();
      final File source = genFile( dir, 2 );
      final File destination = new File( dest, source.getName() );
      FileUtil.copyFileToDirectory( source, dest );
      assertTrue( "destination.exists()", destination.exists() );
      assertEquals( "destination.length", 2, destination.length() );
   }

   public void testForceDeleteAFile()
      throws Exception
   {
      final File dir = genTestDirectory();
      final File file = genFile( dir, 33 );
      FileUtil.forceDelete( file );
      assertTrue( "Check No Exist", !file.exists() );
   }

   public void testForceDeleteADir()
      throws Exception
   {
      final File dir = genTestDirectory();
      final File file = genFile( dir, 33 );
      FileUtil.forceDelete( dir );
      assertTrue( "!dir.exists()", !dir.exists() );
      assertTrue( "!file.exists()", !file.exists() );
   }

   public void testFileEquals()
      throws Exception
   {
      final File dir = genTestDirectory();
      final File source = genFile( dir, 2 );
      assertTrue( "File equals", FileUtil.contentEquals( source, source ) );
   }

   public void testURLToFile()
      throws Exception
   {
      final File dir = genTestDirectory();
      final File file = genFile( dir, 1 );
      assertEquals( "URL to File", file, FileUtil.toFile( file.toURL() ));
      assertNull( "URL to File", FileUtil.toFile( new URL("http://somedomain.tld/path") ));
   }

   public void testFilesToURLs()
      throws Exception
   {
      final File dir = genTestDirectory();
      final File file1 = genFile( dir, 1 );
      final File file2 = genFile( dir, 2 );
      final File[] files = new File[] { file1, file2 };
      final URL[] urls = FileUtil.toURLs( files );
      for ( int i = 0; i < files.length; i++ )
      {
          assertEquals( "File to URL: " + i, files[i].toURL(), urls[i] );
      }  
   }
   
   public void testRemoveExtension()
      throws Exception
   {
       assertEquals( "Remove extension", "foo", FileUtil.removeExtension( "foo.txt") );
       assertEquals( "Remove extension", "a/b/c", FileUtil.removeExtension( "a/b/c.jpg") );
       assertEquals( "Remove extension", "a/b/c", FileUtil.removeExtension( "a/b/c") );
   }

   public void testGetExtension()
      throws Exception
   {
       assertEquals( "Get extension", "txt", FileUtil.getExtension( "foo.txt") );
       assertEquals( "Get extension", "jpg", FileUtil.getExtension( "a/b/c.jpg") );
       assertEquals( "Get extension", "", FileUtil.getExtension( "a/b/c") );
   }

   public void testRemovePath()
      throws Exception
   {
       assertEquals( "Remove path", "foo.txt", FileUtil.removePath( "foo.txt") );
       assertEquals( "Remove path", "c.jpg", FileUtil.removePath( "a/b/c.jpg") );
   }

   public void testGetPath()
      throws Exception
   {
       assertEquals( "Get path", "", FileUtil.getPath( "foo.txt") );
       assertEquals( "Get path", "a/b", FileUtil.getPath( "a/b/c.jpg") );
   }


   private File genTestDirectory()
      throws IOException
   {
      final File testDirectory = getBaseDirectory();
      final File dir =
         File.createTempFile( "FileUtilTest", ".tmp", testDirectory );
      dir.delete();
      dir.mkdirs();
      assertTrue( "dir.exists()", dir.exists() );
      return dir;
   }

   private File getBaseDirectory()
      throws IOException
   {
      final String tmpDirName = System.getProperty( "java.io.tmpdir" );
      final String baseDirName = System.getProperty( "basedir", tmpDirName );
      final File baseDir = new File( baseDirName ).getCanonicalFile();
      final String testDirName =
         baseDir + File.separator + "target" + File.separator + "test-data";
      final File testDirectory = new File( testDirName ).getCanonicalFile();
      if ( !testDirectory.exists() )
      {
         testDirectory.mkdirs();
      }
      return testDirectory;
   }

   private File genFile( final File dir, final long size )
      throws IOException
   {
      final File file =
         File.createTempFile( "FileUtilTest", ".tmp", dir );
      populateFile( file, size );
      assertTrue( "file.exists()", file.exists() );
      return file;
   }

   private void populateFile( final File file, final long size )
      throws IOException
   {
      final BufferedOutputStream output =
         new BufferedOutputStream( new FileOutputStream( file ) );

      for ( int i = 0; i < size; i++ )
      {
         output.write( (byte) 'X' );
      }

      output.close();
      assertEquals( "file.size(" + file + ")", size, file.length() );
   }
}
