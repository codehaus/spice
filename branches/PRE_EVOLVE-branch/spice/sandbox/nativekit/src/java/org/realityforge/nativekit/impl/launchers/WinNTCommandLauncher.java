/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 * Portions of this software are based upon software originally
 * developed as part of the Apache Myrmidon project under
 * the Apache 1.1 License.
 */
package org.realityforge.nativekit.impl.launchers;

import java.io.IOException;
import org.realityforge.nativekit.ExecException;
import org.realityforge.nativekit.ExecMetaData;

/**
 * A command launcher for Windows 2000/NT that uses 'cmd.exe' when launching
 * commands in directories other than the current working directory.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @author <a href="mailto:thomas.haas@softwired-inc.com">Thomas Haas</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-16 10:31:49 $
 */
public final class WinNTCommandLauncher
    implements CommandLauncher
{
    /**
     * Launches the given command in a new process using cmd.exe to
     * set the working directory.
     */
    public Process exec( final ExecMetaData metaData )
        throws IOException, ExecException
    {
        // Use cmd.exe to change to the specified directory before running
        // the command
        final String[] prefix = new String[ 6 ];
        prefix[ 0 ] = "cmd";
        prefix[ 1 ] = "/c";
        prefix[ 2 ] = "cd";
        prefix[ 3 ] = "/d";
        prefix[ 4 ] = metaData.getWorkingDirectory().getCanonicalPath();
        prefix[ 5 ] = "&&";

        final ExecMetaData newMetaData = ExecUtil.prepend( metaData, prefix );
        final String[] env = ExecUtil.getEnvironmentSpec( metaData );
        return Runtime.getRuntime().exec( newMetaData.getCommand(), env );
    }
}
