/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.nativekit.impl.launchers;

import java.io.IOException;
import org.realityforge.nativekit.ExecException;
import org.realityforge.nativekit.ExecMetaData;

/**
 * This is the interface implemented by objects which are capable of
 * lauching a native command. Each different implementation is likely
 * to have a different strategy or be restricted to specific platform.
 *
 * <p>It is expected that the user will get a reference to the
 * <code>CommandLauncher</code> most appropriate for their environment.</p>
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-16 10:31:49 $
 */
public interface CommandLauncher
{
    /**
     * Execute the specified native command.
     *
     * @param metaData the native command to execute
     * @return the Process launched by the CommandLauncher
     * @throws IOException is thrown when the native code can not
     *            launch the application for some reason. Usually due
     *            to the command not being fully specified and not in
     *            the PATH env var.
     * @throws ExecException if the command launcher detects that
     *            it can not execute the native command for some reason.
     */
    Process exec( ExecMetaData metaData )
        throws IOException, ExecException;
}
