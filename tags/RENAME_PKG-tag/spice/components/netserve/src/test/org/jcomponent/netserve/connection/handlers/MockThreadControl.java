/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.handlers;

import org.jcomponent.threadpool.ThreadControl;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-29 03:51:59 $
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
