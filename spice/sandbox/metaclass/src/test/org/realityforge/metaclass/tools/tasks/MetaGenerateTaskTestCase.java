/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

import java.io.File;
import java.io.IOException;
import junit.framework.TestCase;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-08-24 01:23:16 $
 */
public class MetaGenerateTaskTestCase
    extends TestCase
{
    public void testNullDestDir()
        throws Exception
    {
        final MockMetaGenerateTask task = new MockMetaGenerateTask();
        try
        {
            task.execute();
        }
        catch( final BuildException e )
        {
            assertEquals( "DestDir not specified", e.getMessage() );
            return;
        }
        fail( "Expected execute to fail as no destdir specified." );
    }

    public void testFileAsDestDir()
        throws Exception
    {
        final MockMetaGenerateTask task = new MockMetaGenerateTask();
        final File baseDirectory = getBaseDirectory();
        final File destDir =
            File.createTempFile( "mgtest", ".tmp", baseDirectory ).getCanonicalFile();
        task.setDestDir( destDir );
        try
        {
            task.execute();
        }
        catch( final BuildException e )
        {
            assertEquals( "DestDir (" + destDir + ") is not a directory.", e.getMessage() );
            return;
        }
        fail( "Expected execute to fail as destdir specified by file." );
    }

    public void testDestDirNoExistAndNoCreate()
        throws Exception
    {
        final MockMetaGenerateTask task = new MockMetaGenerateTask();
        final File baseDirectory = getBaseDirectory();
        final File secondBaseDir = new File( baseDirectory, "subDir1" );
        secondBaseDir.mkdirs();
        secondBaseDir.setReadOnly();
        final File destDir = new File( secondBaseDir, "subDir" );
        assertFalse( "destDir.exists()", destDir.exists() );

        task.setDestDir( destDir );
        try
        {
            task.execute();
        }
        catch( final BuildException e )
        {
            assertEquals( "DestDir (" + destDir + ") could not be created.", e.getMessage() );
            return;
        }
        fail( "Expected execute to fail as destdir could not be created." );
    }

    public void testNoSourceFiles()
        throws Exception
    {
        final File sourceDirectory = generateDirectory();
        final File destDirectory = generateDirectory();
        final FileSet fileSet = new FileSet();
        fileSet.setDir( sourceDirectory );

        final MockMetaGenerateTask task = new MockMetaGenerateTask();
        final Project project = new Project();
        project.setBaseDir( getBaseDirectory() );
        task.setProject( project );
        task.setDestDir( destDirectory );
        task.addFileset( fileSet );
        task.execute();

        assertEquals( "generated dirs", 0, destDirectory.listFiles().length );
    }

    private static final File generateDirectory()
        throws IOException
    {
        final File baseDirectory = getBaseDirectory();
        final File dir =
            File.createTempFile( "mgtest", ".tmp", baseDirectory ).getCanonicalFile();
        dir.delete();
        dir.mkdirs();
        assertTrue( "dir.exists()", dir.exists() );
        return dir;
    }

    private static final File getBaseDirectory()
    {
        final String tempDir = System.getProperty( "java.io.tmpdir" );
        final String baseDir = System.getProperty( "basedir", tempDir );

        final File base = new File( baseDir ).getAbsoluteFile();
        final String pathname =
            base + File.separator + "target" + File.separator + "test-data";
        final File dir = new File( pathname );
        dir.mkdirs();
        return dir;
    }
}
