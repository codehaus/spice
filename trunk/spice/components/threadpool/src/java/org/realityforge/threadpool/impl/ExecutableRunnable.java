/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.threadpool.impl;

import org.realityforge.threadpool.Executable;

/**
 * Class to adapt a {@link Runnable} object in
 * an {@link Executable} object.
 *
 * @author <a href="mailto:peter at apache.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-03-01 03:08:15 $
 */
final class ExecutableRunnable
    implements Executable
{
    ///The runnable instance being wrapped
    private Runnable m_runnable;

    /**
     * Create adapter using specified runnable.
     *
     * @param runnable the runnable to adapt to
     */
    protected ExecutableRunnable( final Runnable runnable )
    {
        if( null == runnable )
        {
            throw new NullPointerException( "runnable" );
        }
        m_runnable = runnable;
    }

    /**
     * Execute the underlying {@link Runnable} object.
     *
     * @throws Exception if an error occurs
     */
    public void execute()
        throws Exception
    {
        m_runnable.run();
    }
}
