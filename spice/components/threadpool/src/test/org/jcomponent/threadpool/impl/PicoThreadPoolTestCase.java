/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.threadpool.impl;

import org.jcomponent.threadpool.ThreadPool;

/**
 *  A TestCase for the PicoCommonsThreadPool.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @version $Revision: 1.4 $ $Date: 2003-08-30 09:26:20 $
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
        picoThreadPool.dispose();
    }

}
