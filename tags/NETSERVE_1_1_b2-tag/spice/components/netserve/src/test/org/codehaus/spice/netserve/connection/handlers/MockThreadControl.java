/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netserve.connection.handlers;

import org.codehaus.spice.threadpool.ThreadControl;

/**
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-03-21 23:42:59 $
 */
class MockThreadControl
    implements ThreadControl
{
    private final Thread m_thread;

    public MockThreadControl( Thread thread )
    {
        m_thread = thread;
    }

    public void join( long milliSeconds )
        throws InterruptedException
    {
        m_thread.join( milliSeconds );
    }

    public void interrupt()
        throws IllegalStateException, SecurityException
    {
        m_thread.interrupt();
    }

    public boolean isFinished()
    {
        return !m_thread.isAlive();
    }

    public Throwable getThrowable()
    {
        return null;
    }
}
