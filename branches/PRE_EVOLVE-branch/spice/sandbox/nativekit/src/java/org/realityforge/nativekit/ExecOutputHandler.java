/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.nativekit;

/**
 * This class is used to receive notifications of what the native
 * process outputs to standard output and standard error.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-16 10:31:48 $
 */
public interface ExecOutputHandler
{
    /**
     * Receive notification about the process writing
     * to standard output.
     */
    void stdout( String line );

    /**
     * Receive notification about the process writing
     * to standard error.
     */
    void stderr( String line );
}
