/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.netserve.connection.impl;

import org.realityforge.threadpool.ThreadPool;
import org.realityforge.threadpool.ThreadControl;
import org.realityforge.threadpool.Executable;

/**
 * test thread pool/.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-23 07:24:47 $
 */
class TestThreadPool
    implements ThreadPool
{
    public ThreadControl execute( Runnable work )
    {
        final Thread thread = new Thread( work );
        thread.start();
        return null;
    }

    public ThreadControl execute( Executable work )
    {
        throw new UnsupportedOperationException();
    }
}
