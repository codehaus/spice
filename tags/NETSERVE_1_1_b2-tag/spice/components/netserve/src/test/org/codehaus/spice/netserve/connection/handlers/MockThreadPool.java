/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netserve.connection.handlers;

import org.codehaus.spice.threadpool.ThreadPool;
import org.codehaus.spice.threadpool.ThreadControl;
import org.codehaus.spice.threadpool.Executable;

/**
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-03-21 23:42:59 $
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
