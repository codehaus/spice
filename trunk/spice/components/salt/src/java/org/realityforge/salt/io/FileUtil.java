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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * This class provides basic facilities for manipulating files and file paths.
 *
 * <h3>Path-related methods</h3>
 *
 * <p>Methods exist to retrieve the components of a typical file path. For example
 * <code>/www/hosted/mysite/index.html</code>, can be broken into:
 * <ul>
 *   <li><code>/www/hosted/mysite/</code> -- retrievable through {@link #getPath}</li>
 *   <li><code>index.html</code> -- retrievable through {@link #removePath}</li>
 *   <li><code>/www/hosted/mysite/index</code> -- retrievable through {@link #removeExtension}</li>
 *   <li><code>html</code> -- retrievable through {@link #getExtension}</li>
 * </ul>
 * There are also methods to {@link #catPath concatenate two paths}, {@link #resolveFile resolve a
 * path relative to a File} and {@link #normalize} a path.
 * </p>
 *
 * <h3>File-related methods</h3>
 * <p>
 * There are methods to  create a {@link #toFile File from a URL}, copy a
 * {@link #copyFileToDirectory File to a directory},
 * copy a {@link #copyFile File to another File},
 * copy a {@link #copyURLToFile URL's contents to a File},
 * as well as methods to {@link #deleteDirectory(java.io.File) delete} and {@link #cleanDirectory(java.io.File)
 * clean} a directory.
 * </p>
 *
 * @author <a href="mailto:peter at apache.org">Peter Donald</a>
 * @author <a href="mailto:jefft@apache.org">Jeff Turner</a>
 * @author <a href="mailto:nl@novadeck.com">Nicolas Leclerc</a>
 * @version CVS $Revision: 1.1 $ $Date: 2003-05-27 13:07:18 $
 * @since 4.0
 */
public final class FileUtil
{
    /**
     * Private constructor to prevent instantiation.
     *
     */
    private FileUtil()
    {
    }

    /**
     * Compare the contents of two files to determine if they are equal or not.
     *
     * @param file1 the first file
     * @param file2 the second file
     * @return true if the content of the files are equal or they both don't exist, false otherwise
     */
    public static boolean contentEquals( final File file1, final File file2 )
        throws IOException
    {
        final boolean file1Exists = file1.exists();
        if( file1Exists != file2.exists() )
        {
            return false;
        }

        if( !file1Exists )
        {
            // two not existing files are equal
            return true;
        }

        if( file1.isDirectory() || file2.isDirectory() )
        {
            // don't want to compare directory contents
            return false;
        }

        InputStream input1 = null;
        InputStream input2 = null;
        try
        {
            input1 = new FileInputStream( file1 );
            input2 = new FileInputStream( file2 );
            return IOUtil.contentEquals( input1, input2 );

        }
        finally
        {
            IOUtil.shutdownStream( input1 );
            IOUtil.shutdownStream( input2 );
        }
    }

    /**
     * Convert from a <code>URL</code> to a <code>File</code>.
     * @param url File URL.
     * @return The equivalent <code>File</code> object, or <code>null</code> if the URL's protocol
     * is not <code>file</code>
     */
    public static File toFile( final URL url )
    {
        if( url.getProtocol().equals( "file" ) == false )
        {
            return null;
        }
        else
        {
            final String filename = url.getFile().replace( '/', File.separatorChar );
            return new File( filename );
        }
    }

    /**
     * Convert the array of Files into a list of URLs.
     *
     * @param files the array of files
     * @return the array of URLs
     * @throws java.io.IOException if an error occurs
     */
    public static URL[] toURLs( final File[] files )
        throws IOException
    {
        final URL[] urls = new URL[ files.length ];

        for( int i = 0; i < urls.length; i++ )
        {
            urls[ i ] = files[ i ].toURL();
        }

        return urls;
    }

    /**
     * Remove extension from filename.
     * ie
     * <pre>
     * foo.txt    --> foo
     * a\b\c.jpg --> a\b\c
     * a\b\c     --> a\b\c
     * </pre>
     *
     * @param filename the filename
     * @return the filename minus extension
     */
    public static String removeExtension( final String filename )
    {
        final int index = filename.lastIndexOf( '.' );

        if( -1 == index )
        {
            return filename;
        }
        else
        {
            return filename.substring( 0, index );
        }
    }

    /**
     * Get extension from filename.
     * ie
     * <pre>
     * foo.txt    --> "txt"
     * a\b\c.jpg --> "jpg"
     * a\b\c     --> ""
     * </pre>
     *
     * @param filename the filename
     * @return the extension of filename or "" if none
     */
    public static String getExtension( final String filename )
    {
        final int index = filename.lastIndexOf( '.' );

        if( -1 == index )
        {
            return "";
        }
        else
        {
            return filename.substring( index + 1 );
        }
    }

    /**
     * Remove path from filename. Equivalent to the unix command <code>basename</code>
     * ie.
     * <pre>
     * a/b/c.txt --> c.txt
     * a.txt     --> a.txt
     * </pre>
     *
     * @param filepath the filepath
     * @return the filename minus path
     */
    public static String removePath( final String filepath )
    {
        return removePath( filepath, File.separatorChar );
    }

    /**
     * Remove path from filename.
     * ie.
     * <pre>
     * a/b/c.txt --> c.txt
     * a.txt     --> a.txt
     * </pre>
     *
     * @param filepath the filepath
     * @return the filename minus path
     */
    public static String removePath( final String filepath, final char fileSeparatorChar )
    {
        final int index = filepath.lastIndexOf( fileSeparatorChar );

        if( -1 == index )
        {
            return filepath;
        }
        else
        {
            return filepath.substring( index + 1 );
        }
    }

    /**
     * Get path from filename. Roughly equivalent to the unix command <code>dirname</code>.
     * ie.
     * <pre>
     * a/b/c.txt --> a/b
     * a.txt     --> ""
     * </pre>
     *
     * @param filepath the filepath
     * @return the filename minus path
     */
    public static String getPath( final String filepath )
    {
        return getPath( filepath, File.separatorChar );
    }

    /**
     * Get path from filename.
     * ie.
     * <pre>
     * a/b/c.txt --> a/b
     * a.txt     --> ""
     * </pre>
     *
     * @param filepath the filepath
     * @return the filename minus path
     */
    public static String getPath( final String filepath, final char fileSeparatorChar )
    {
        final int index = filepath.lastIndexOf( fileSeparatorChar );
        if( -1 == index )
        {
            return "";
        }
        else
        {
            return filepath.substring( 0, index );
        }
    }

    /**
     * Copy file from source to destination. If <code>destinationDirectory</code> does not exist, it
     * (and any parent directories) will be created. If a file <code>source</code> in
     * <code>destinationDirectory</code> exists, it will be overwritten.
     *
     * @param source An existing <code>File</code> to copy.
     * @param destinationDirectory A directory to copy <code>source</code> into.
     *
     * @throws java.io.FileNotFoundException if <code>source</code> isn't a normal file.
     * @throws java.lang.IllegalArgumentException if <code>destinationDirectory</code> isn't a directory.
     * @throws java.io.IOException if <code>source</code> does not exist, the file in
     * <code>destinationDirectory</code> cannot be written to, or an IO error occurs during copying.
     */
    public static void copyFileToDirectory( final String source,
                                            final String destinationDirectory )
        throws IOException
    {
        copyFileToDirectory( new File( source ),
                             new File( destinationDirectory ) );
    }

    /**
     * Copy file from source to destination. If <code>destinationDirectory</code> does not exist, it
     * (and any parent directories) will be created. If a file <code>source</code> in
     * <code>destinationDirectory</code> exists, it will be overwritten.
     *
     * @param source An existing <code>File</code> to copy.
     * @param destinationDirectory A directory to copy <code>source</code> into.
     *
     * @throws java.io.FileNotFoundException if <code>source</code> isn't a normal file.
     * @throws java.lang.IllegalArgumentException if <code>destinationDirectory</code> isn't a directory.
     * @throws java.io.IOException if <code>source</code> does not exist, the file in
     * <code>destinationDirectory</code> cannot be written to, or an IO error occurs during copying.
     */
    public static void copyFileToDirectory( final File source,
                                            final File destinationDirectory )
        throws IOException
    {
        if( destinationDirectory.exists() && !destinationDirectory.isDirectory() )
        {
            throw new IllegalArgumentException( "Destination is not a directory" );
        }

        copyFile( source, new File( destinationDirectory, source.getName() ) );
    }

    /**
     * Copy file from source to destination. The directories up to <code>destination</code> will be
     * created if they don't already exist. <code>destination</code> will be overwritten if it
     * already exists.
     *
     * @param source An existing non-directory <code>File</code> to copy bytes from.
     * @param destination A non-directory <code>File</code> to write bytes to (possibly
     * overwriting).
     * @throws java.io.IOException if <code>source</code> does not exist, <code>destination</code> cannot be
     * written to, or an IO error occurs during copying.
     * @throws java.io.FileNotFoundException if <code>destination</code> is a directory
     * (use {@link #copyFileToDirectory}).
     */
    public static void copyFile( final File source, final File destination )
        throws IOException
    {
        //check source exists
        if( !source.exists() )
        {
            final String message = "File " + source + " does not exist";
            throw new IOException( message );
        }

        //does destinations directory exist ?
        if( destination.getParentFile() != null &&
            !destination.getParentFile().exists() )
        {
            destination.getParentFile().mkdirs();
        }

        //make sure we can write to destination
        if( destination.exists() && !destination.canWrite() )
        {
            final String message = "Unable to open file " +
                destination + " for writing.";
            throw new IOException( message );
        }

        final FileInputStream input = new FileInputStream( source );
        final FileOutputStream output = new FileOutputStream( destination );
        IOUtil.copy( input, output );
        IOUtil.shutdownStream( input );
        IOUtil.shutdownStream( output );

        if( source.length() != destination.length() )
        {
            final String message = "Failed to copy full contents from " + source +
                " to " + destination;
            throw new IOException( message );
        }
    }

    /**
     * Copies bytes from the URL <code>source</code> to a file <code>destination</code>.
     * The directories up to <code>destination</code> will be created if they don't already exist.
     * <code>destination</code> will be overwritten if it already exists.
     *
     * @param source A <code>URL</code> to copy bytes from.
     * @param destination A non-directory <code>File</code> to write bytes to (possibly
     * overwriting).
     *
     * @throws java.io.IOException if
     * <ul>
     *  <li><code>source</code> URL cannot be opened</li>
     *  <li><code>destination</code> cannot be written to</li>
     *  <li>an IO error occurs during copying</li>
     * </ul>
     */
    public static void copyURLToFile( final URL source, final File destination )
        throws IOException
    {
        //does destination directory exist ?
        if( destination.getParentFile() != null &&
            !destination.getParentFile().exists() )
        {
            destination.getParentFile().mkdirs();
        }

        //make sure we can write to destination
        if( destination.exists() && !destination.canWrite() )
        {
            final String message = "Unable to open file " +
                destination + " for writing.";
            throw new IOException( message );
        }

        final InputStream input = source.openStream();
        final FileOutputStream output = new FileOutputStream( destination );
        IOUtil.copy( input, output );
        IOUtil.shutdownStream( input );
        IOUtil.shutdownStream( output );
    }

    /**
     * Normalize a path. That means:
     * <ul>
     *   <li>changes to unix style if under windows</li>
     *   <li>eliminates "/../" and "/./"</li>
     *   <li>if path is absolute (starts with '/') and there are
     *   too many occurences of "../" (would then have some kind
     *   of 'negative' path) returns null.</li>
     *   <li>If path is relative, the exceeding ../ are kept at
     *   the begining of the path.</li>
     * </ul>
     * <br><br>
     *
     * <b>Note:</b> note that this method has been tested with unix and windows only.
     *
     * <p>Eg:</p>
     * <pre>
     * /foo//               -->     /foo/
     * /foo/./              -->     /foo/
     * /foo/../bar          -->     /bar
     * /foo/../bar/         -->     /bar/
     * /foo/../bar/../baz   -->     /baz
     * //foo//./bar         -->     /foo/bar
     * /../                 -->     null
     * </pre>
     *
     * @param path the path to be normalized.
     * @return the normalized path or null.
     * @throws java.lang.NullPointerException if path is null.
     */
    public static final String normalize( String path )
    {
        if( path.length() < 2 )
        {
            return path;
        }

        StringBuffer buff = new StringBuffer( path );

        int length = path.length();

        // this whole prefix thing is for windows compatibility only.
        String prefix = null;

        if( length > 2 && buff.charAt( 1 ) == ':' )
        {
            prefix = path.substring( 0, 2 );
            buff.delete( 0, 2 );
            path = path.substring( 2 );
            length -= 2;
        }

        boolean startsWithSlash = length > 0 && ( buff.charAt( 0 ) == '/' || buff.charAt( 0 ) == '\\' );

        boolean expStart = true;
        int ptCount = 0;
        int lastSlash = length + 1;
        int upLevel = 0;

        for( int i = length - 1; i >= 0; i-- )
            switch( path.charAt( i ) )
            {
                case '\\':
                    buff.setCharAt( i, '/' );
                case '/':
                    if( lastSlash == i + 1 )
                    {
                        buff.deleteCharAt( i );
                    }

                    switch( ptCount )
                    {
                        case 1:
                            buff.delete( i, lastSlash );
                            break;

                        case 2:
                            upLevel++;
                            break;

                        default:
                            if( upLevel > 0 && lastSlash != i + 1 )
                            {
                                buff.delete( i, lastSlash + 3 );
                                upLevel--;
                            }
                            break;
                    }

                    ptCount = 0;
                    expStart = true;
                    lastSlash = i;
                    break;

                case '.':
                    if( expStart )
                    {
                        ptCount++;
                    }
                    break;

                default:
                    ptCount = 0;
                    expStart = false;
                    break;
            }

        switch( ptCount )
        {
            case 1:
                buff.delete( 0, lastSlash );
                break;

            case 2:
                break;

            default:
                if( upLevel > 0 )
                {
                    if( startsWithSlash )
                    {
                        return null;
                    }
                    else
                    {
                        upLevel = 1;
                    }
                }

                while( upLevel > 0 )
                {
                    buff.delete( 0, lastSlash + 3 );
                    upLevel--;
                }
                break;
        }

        length = buff.length();
        boolean isLengthNull = length == 0;
        char firstChar = isLengthNull ? (char)0 : buff.charAt( 0 );

        if( !startsWithSlash && !isLengthNull && firstChar == '/' )
        {
            buff.deleteCharAt( 0 );
        }
        else if( startsWithSlash &&
            ( isLengthNull || ( !isLengthNull && firstChar != '/' ) ) )
        {
            buff.insert( 0, '/' );
        }

        if( prefix != null )
        {
            buff.insert( 0, prefix );
        }

        return buff.toString();
    }

    /**
     * Will concatenate 2 paths.  Paths with <code>..</code> will be
     * properly handled.
     * <p>Eg.,<br />
     * <code>/a/b/c</code> + <code>d</code> = <code>/a/b/d</code><br />
     * <code>/a/b/c</code> + <code>../d</code> = <code>/a/d</code><br />
     * </p>
     *
     * <p>Note: this method handles java/unix style path only (separator is '/').
     * <ul>
     * <li>it handles absolute and relative path.</li>
     * <li>returned path is normalized (as with method <code>normalize<code>).</li>
     * </ul>
     *
     * @return The concatenated paths, or null if error occurs
     *
     * @see #normalize
     * @throws java.lang.NullPointerException if any parameter is null.
     */
    public static String catPath( String lookupPath,
                                  final String path )
    {
        if( path == null )
        {
            throw new NullPointerException( "path" );
        }

        if( !lookupPath.endsWith( "/" ) )
        {
            final int index = lookupPath.lastIndexOf( "/" );
            if( index < 0 )
            {
                lookupPath = "";
            }
            else
            {
                lookupPath = lookupPath.substring( 0, index + 1 );
            }
        }

        return normalize( lookupPath + path );
    }

    /**
     * Resolve a file <code>filename</code> to it's canonical form. If <code>filename</code> is
     * relative (doesn't start with <code>/</code>), it will be resolved relative to
     * <code>baseFile</code>, otherwise it is treated as a normal root-relative path.
     *
     * @param baseFile Where to resolve <code>filename</code> from, if <code>filename</code> is
     * relative.
     * @param filename Absolute or relative file path to resolve.
     * @return The canonical <code>File</code> of <code>filename</code>.
     */
    public static File resolveFile( final File baseFile, String filename )
    {
        String filenm = filename;
        if( '/' != File.separatorChar )
        {
            filenm = filename.replace( '/', File.separatorChar );
        }

        if( '\\' != File.separatorChar )
        {
            filenm = filename.replace( '\\', File.separatorChar );
        }

        // deal with absolute files
        if( filenm.startsWith( File.separator ) )
        {
            File file = new File( filenm );

            try
            {
                file = file.getCanonicalFile();
            }
            catch( final IOException ioe )
            {
            }

            return file;
        }
        // FIXME: I'm almost certain this // removal is unnecessary, as getAbsoluteFile() strips
        // them. However, I'm not sure about this UNC stuff. (JT)
        final char[] chars = filename.toCharArray();
        final StringBuffer sb = new StringBuffer();

        //remove duplicate file separators in succession - except
        //on win32 at start of filename as UNC filenames can
        //be \\AComputer\AShare\myfile.txt
        int start = 0;
        if( '\\' == File.separatorChar )
        {
            sb.append( filenm.charAt( 0 ) );
            start++;
        }

        for( int i = start; i < chars.length; i++ )
        {
            final boolean doubleSeparator =
                File.separatorChar == chars[ i ] && File.separatorChar == chars[ i - 1 ];

            if( !doubleSeparator )
            {
                sb.append( chars[ i ] );
            }
        }

        filenm = sb.toString();

        //must be relative
        File file = ( new File( baseFile, filenm ) ).getAbsoluteFile();

        try
        {
            file = file.getCanonicalFile();
        }
        catch( final IOException ioe )
        {
        }

        return file;
    }

    /**
     * Delete a file. If file is directory delete it and all sub-directories.
     */
    public static void forceDelete( final String file )
        throws IOException
    {
        forceDelete( new File( file ) );
    }

    /**
     * Delete a file. If file is directory delete it and all sub-directories.
     */
    public static void forceDelete( final File file )
        throws IOException
    {
        if( file.isDirectory() )
        {
            deleteDirectory( file );
        }
        else
        {
            if( !file.delete() )
            {
                final String message =
                    "File " + file + " unable to be deleted.";
                throw new IOException( message );
            }
        }
    }

    /**
     * Schedule a file to be deleted when JVM exits.
     * If file is directory delete it and all sub-directories.
     */
    public static void forceDeleteOnExit( final File file )
        throws IOException
    {
        if( file.isDirectory() )
        {
            deleteDirectoryOnExit( file );
        }
        else
        {
            file.deleteOnExit();
        }
    }

    /**
     * Recursively schedule directory for deletion on JVM exit.
     */
    private static void deleteDirectoryOnExit( final File directory )
        throws IOException
    {
        if( !directory.exists() )
        {
            return;
        }

        cleanDirectoryOnExit( directory );
        directory.deleteOnExit();
    }

    /**
     * Clean a directory without deleting it.
     */
    private static void cleanDirectoryOnExit( final File directory )
        throws IOException
    {
        if( !directory.exists() )
        {
            final String message = directory + " does not exist";
            throw new IllegalArgumentException( message );
        }

        if( !directory.isDirectory() )
        {
            final String message = directory + " is not a directory";
            throw new IllegalArgumentException( message );
        }

        IOException exception = null;

        final File[] files = directory.listFiles();
        for( int i = 0; i < files.length; i++ )
        {
            final File file = files[ i ];
            try
            {
                FileUtil.forceDeleteOnExit( file );
            }
            catch( final IOException ioe )
            {
                exception = ioe;
            }
        }

        if( null != exception )
        {
            throw exception;
        }
    }

    /**
     * Make a directory. If there already exists a file with specified name or
     * the directory is unable to be created then an exception is thrown.
     */
    public static void forceMkdir( final File file )
        throws IOException
    {
        if( file.exists() )
        {
            if( file.isFile() )
            {
                final String message = "File " + file + " exists and is " +
                    "not a directory. Unable to create directory.";
                throw new IOException( message );
            }
        }
        else
        {
            if( false == file.mkdirs() )
            {
                final String message = "Unable to create directory " + file;
                throw new IOException( message );
            }
        }
    }

    /**
     * Recursively delete a directory.
     */
    public static void deleteDirectory( final String directory )
        throws IOException
    {
        deleteDirectory( new File( directory ) );
    }

    /**
     * Recursively delete a directory.
     */
    public static void deleteDirectory( final File directory )
        throws IOException
    {
        if( !directory.exists() )
        {
            return;
        }

        cleanDirectory( directory );
        if( !directory.delete() )
        {
            final String message =
                "Directory " + directory + " unable to be deleted.";
            throw new IOException( message );
        }
    }

    /**
     * Clean a directory without deleting it.
     */
    public static void cleanDirectory( final String directory )
        throws IOException
    {
        cleanDirectory( new File( directory ) );
    }

    /**
     * Clean a directory without deleting it.
     */
    public static void cleanDirectory( final File directory )
        throws IOException
    {
        if( !directory.exists() )
        {
            final String message = directory + " does not exist";
            throw new IllegalArgumentException( message );
        }

        if( !directory.isDirectory() )
        {
            final String message = directory + " is not a directory";
            throw new IllegalArgumentException( message );
        }

        IOException exception = null;

        final File[] files = directory.listFiles();
        for( int i = 0; i < files.length; i++ )
        {
            final File file = files[ i ];
            try
            {
                FileUtil.forceDelete( file );
            }
            catch( final IOException ioe )
            {
                exception = ioe;
            }
        }

        if( null != exception )
        {
            throw exception;
        }
    }

    /**
     * Recursively count size of a directory.
     *
     * @return size of directory in bytes.
     */
    public static long sizeOfDirectory( final String directory )
    {
        return sizeOfDirectory( new File( directory ) );
    }

    /**
     * Recursively count size of a directory.
     *
     * @return size of directory in bytes.
     */
    public static long sizeOfDirectory( final File directory )
    {
        if( !directory.exists() )
        {
            final String message = directory + " does not exist";
            throw new IllegalArgumentException( message );
        }

        if( !directory.isDirectory() )
        {
            final String message = directory + " is not a directory";
            throw new IllegalArgumentException( message );
        }

        long size = 0;

        final File[] files = directory.listFiles();
        for( int i = 0; i < files.length; i++ )
        {
            final File file = files[ i ];

            if( file.isDirectory() )
            {
                size += sizeOfDirectory( file );
            }
            else
            {
                size += file.length();
            }
        }

        return size;
    }
}
