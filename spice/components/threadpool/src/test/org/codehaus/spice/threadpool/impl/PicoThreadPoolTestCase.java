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
 * @author Peter Donald
 * @author Mauro Talevi
 * @version $Revision: 1.3 $ $Date: 2004-03-21 23:42:58 $
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
