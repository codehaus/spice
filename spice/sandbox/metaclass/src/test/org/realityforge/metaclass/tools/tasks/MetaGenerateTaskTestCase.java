/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

import java.io.File;
import junit.framework.TestCase;
import org.apache.tools.ant.BuildException;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-08-23 23:51:26 $
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
