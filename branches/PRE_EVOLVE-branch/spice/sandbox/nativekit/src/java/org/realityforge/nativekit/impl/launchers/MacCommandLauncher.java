/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.nativekit.impl.launchers;

import java.io.File;
import java.io.IOException;
import org.realityforge.nativekit.ExecException;
import org.realityforge.nativekit.ExecMetaData;

/**
 * A command launcher for Mac that uses a dodgy mechanism to change working
 * directory before launching commands. This class changes the value of the
 * System property "user.dir" before the command is executed and then resets
 * it after the command is executed. This can have really unhealthy side-effects
 * if there are multiple threads in JVM and should be used with extreme caution.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @author <a href="mailto:thomas.haas@softwired-inc.com">Thomas Haas</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-16 10:31:49 $
 */
public final class MacCommandLauncher
    implements CommandLauncher
{
    /**
     * Execute the specified native command.
     */
    public Process exec( final ExecMetaData metaData )
        throws IOException, ExecException
    {
        final File directory = metaData.getWorkingDirectory().getCanonicalFile();
        if( ExecUtil.isCwd( directory ) )
        {
            final String[] env = ExecUtil.getEnvironmentSpec( metaData );
            return Runtime.getRuntime().exec( metaData.getCommand(), env );
        }

        //WARNING: This is an ugly hack and not thread safe in the slightest way
        //It can have really really undersirable side-effects if multiple threads
        //are running in the JVM
        try
        {
            System.setProperty( "user.dir", directory.toString() );
            final String[] env = ExecUtil.getEnvironmentSpec( metaData );
            return Runtime.getRuntime().exec( metaData.getCommand(), env );
        }
        finally
        {
            System.setProperty( "user.dir", ExecUtil.getCwd().toString() );
        }
    }
}