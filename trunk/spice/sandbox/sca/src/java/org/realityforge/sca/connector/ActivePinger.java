/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.sca.connector;

/**
 * A simple Runnable that checks ping for connector in a separate thread.
 *
 * @author Peter Donald
 * @version $Revision: 1.7 $ $Date: 2003-12-05 05:39:33 $
 */
public class ActivePinger
    implements Runnable
{
    /** The associated connector. */
    private final Connector m_connector;

    /** The thread that pinger is running in. */
    private Thread m_thread;

    /** Flag indicating whether the pinger has started. */
    private boolean m_started;

    /** Flag indicating whether the pinger has ended. */
    private boolean m_ended;

    /**
     * Create pinger for specified connector.
     *
     * @param connector the connector
     */
    public ActivePinger( final Connector connector )
    {
        if( null == connector )
        {
            throw new NullPointerException( "connector" );
        }
        m_connector = connector;
    }

    /**
     * Deactivate the pinger and wait till it has stopped pinging.
     */
    public synchronized void deactivate()
    {
        final Thread thread = m_thread;
        m_thread = null;
        while( !m_ended )
        {
            thread.interrupt();
            try
            {
                wait( 200 );
            }
            catch( final InterruptedException ie )
            {
                //ignore
            }
        }
    }

    /**
     * Return true if pinger has started.
     *
     * @return true if pinger has started.
     */
    public synchronized boolean hasStarted()
    {
        return m_started;
    }

    /**
     * Main pinging loop.
     */
    public void run()
    {
        synchronized( this )
        {
            m_started = true;
            m_thread = Thread.currentThread();
        }

        while( null != m_thread )
        {
            final long then = m_connector.checkPing();
            synchronized( this )
            {
                if( null == m_thread )
                {
                    break;
                }
            }
            try
            {
                final long diff = then - System.currentTimeMillis();
                if( diff > 0 )
                {
                    Thread.sleep( diff );
                }
                else
                {
                    Thread.sleep( 1000 );
                }
            }
            catch( final InterruptedException ie )
            {
                //Ignore and fall to through to isActive
            }
        }

        synchronized( this )
        {
            m_ended = true;
            notifyAll();
        }
    }
}
