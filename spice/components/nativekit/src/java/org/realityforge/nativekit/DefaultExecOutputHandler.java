/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.nativekit;

import org.apache.avalon.framework.logger.AbstractLogEnabled;

/**
 * This class is used to receive notifications of what the native
 * process outputs to standard output and standard error.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-01-29 00:14:35 $
 */
public final class DefaultExecOutputHandler
    extends AbstractLogEnabled
    implements ExecOutputHandler
{
    /**
     * Receive notification about the process writing
     * to standard output.
     */
    public void stdout( final String line )
    {
        getLogger().info( line );
    }

    /**
     * Receive notification about the process writing
     * to standard error.
     */
    public void stderr( final String line )
    {
        getLogger().warn( line );
    }
}
