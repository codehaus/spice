/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.impl;

import org.jcomponent.threadpool.Executable;
import org.jcomponent.threadpool.ThreadControl;
import org.jcomponent.threadpool.ThreadPool;

/**
 * test thread pool/.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-08-31 02:27:03 $
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
