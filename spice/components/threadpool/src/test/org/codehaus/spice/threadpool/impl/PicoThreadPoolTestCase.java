/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.threadpool.impl;

import org.codehaus.spice.threadpool.ThreadPool;

/**
 *  A TestCase for the PicoCommonsThreadPool.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @author Mauro Talevi
 * @version $Revision: 1.2 $ $Date: 2004-02-28 21:13:22 $
 */
public class PicoThreadPoolTestCase
    extends AbstractThreadPoolTestCase
{
    public PicoThreadPoolTestCase( final String name )
    {
        super( name );
    }

    protected AbstractThreadPool createThreadPool() throws Exception
    {
       return new PicoCommonsThreadPool( new NullThreadPoolMonitor(), "testThreadPool", 5, false, false, 3, 1 );
    }

    protected AbstractThreadPool createThreadPoolWithDebug() throws Exception
    {
       return new PicoCommonsThreadPool( new NullThreadPoolMonitor(), "testThreadPool", 5, false, false, 3, 1 );
    }

    protected void destroyThreadPool( final ThreadPool threadPool ) throws Exception
    {
        final PicoCommonsThreadPool picoThreadPool = (PicoCommonsThreadPool)threadPool;
        picoThreadPool.shutdown();
    }
}
