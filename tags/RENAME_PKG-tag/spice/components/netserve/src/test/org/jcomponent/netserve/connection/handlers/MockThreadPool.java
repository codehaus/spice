/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.handlers;

import org.jcomponent.threadpool.ThreadPool;
import org.jcomponent.threadpool.ThreadControl;
import org.jcomponent.threadpool.Executable;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-29 03:51:59 $
 */
class MockThreadPool
    implements ThreadPool
{
    public ThreadControl execute( Runnable work )
    {
        final Thread thread = new Thread( work );
        thread.start();
        return new MockThreadControl( thread );
    }

    public ThreadControl execute( Executable work )
    {
        return null;
    }
}
