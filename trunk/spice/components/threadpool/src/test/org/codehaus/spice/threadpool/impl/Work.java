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
 * A bit of work used during testing.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-11-20 00:23:45 $
 */
class Work
    implements Runnable, Executable
{
    private boolean m_locked;
    private int m_sleep;
    private final Throwable m_exception;
    private boolean m_interupted;
    private boolean m_done;

    public Work( boolean locked, int sleep, Throwable exception )
    {
        m_locked = locked;
        m_sleep = sleep;
        m_exception = exception;
    }

    public void run()
    {
        try
        {
            doWork();
        }
        finally
        {
            m_done = true;
        }
    }

    public void execute()
        throws Exception
    {
        try
        {
            doWork();
            if( null != m_exception )
            {
                m_exception.fillInStackTrace();
                if( m_exception instanceof Exception )
                {
                    throw (Exception)m_exception;
                }
                else
                {
                    throw (Error)m_exception;
                }
            }
        }
        finally
        {
            m_done = true;
        }
    }

    public Throwable getException()
    {
        return m_exception;
    }

    boolean isDone()
    {
        return m_done;
    }

    boolean isInterupted()
    {
        return m_interupted;
    }

    synchronized void unlock()
    {
        m_locked = false;
        notifyAll();
    }

    private void doWork()
    {
        if( 0 != m_sleep )
        {
            try
            {
                Thread.sleep( m_sleep );
            }
            catch( InterruptedException e )
            {
                m_interupted = true;
            }
        }
        synchronized( this )
        {
            while( m_locked )
            {
                try
                {
                    wait( 100 );
                }
                catch( InterruptedException e )
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
