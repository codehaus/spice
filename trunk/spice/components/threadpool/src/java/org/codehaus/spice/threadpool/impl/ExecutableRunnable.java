/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.threadpool.impl;

import org.codehaus.spice.threadpool.Executable;

/**
 * Class to adapt a {@link Runnable} object in
 * an {@link Executable} object.
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-03-21 23:42:58 $
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
