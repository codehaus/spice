/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.salt.io;

import java.io.File;
import junit.framework.TestCase;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-06-13 05:04:10 $
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
        catch( final IllegalArgumentException iae )
        {
        }
    }
}
