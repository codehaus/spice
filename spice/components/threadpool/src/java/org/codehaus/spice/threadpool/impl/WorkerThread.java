/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.

 * Portions of this software are based upon software originally
 * developed as part of the Apache Avalon project under
 * the Apache 1.1 License.
 */
package org.codehaus.spice.threadpool.impl;

import org.codehaus.spice.threadpool.Executable;
import org.codehaus.spice.threadpool.ThreadControl;

/**
 * This class extends the Thread class to add recyclable functionalities.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-11-20 00:23:44 $
 */
public class WorkerThread
    extends Thread
{
    /**
     * The work currentlyy associated with worker (May be null).
     */
    private Executable m_work;

    /**
     * The thread control associated with current work.
     * Should be null if work is null.
     */
    private DefaultThreadControl m_threadControl;

    /**
     * True if this thread is alive and not scheduled for shutdown.
     */
    private boolean m_alive;

    /**
     * The name of thread.
     */
    private final String m_name;

    /**
     * The thread pool this thread is associated with.
     */
    private final AbstractThreadPool m_pool;

    /**
     * Allocates a new <code>Worker</code> object.
     */
    protected WorkerThread( final AbstractThreadPool pool,
                            final ThreadGroup group,
                            final String name )
    {
        super( group, "" );
        if( null == name )
        {
            throw new NullPointerException( "name" );
        }
        if( null == pool )
        {
            throw new NullPointerException( "pool" );
        }
        setName( name );
        m_name = name;
        m_work = null;
        m_alive = true;
        m_pool = pool;

        setDaemon( false );
    }

    /**
     * The main execution loop.
     */
    public final void run()
    {
        debug( "starting." );

        // Notify the pool this worker started running.
        //notifyAll();

        while( m_alive )
        {
            waitUntilCondition( true );

            debug( "running." );

            Throwable throwable = null;
            try
            {
                preExecute();
                m_work.execute();
            }
            catch( final ThreadDeath threadDeath )
            {
                debug( "thread has died." );
                throwable = threadDeath;
                // This is to let the thread death propagate to the runtime
                // enviroment to let it know it must kill this worker
                throw threadDeath;
            }
            catch( final Throwable t )
            {
                // Error thrown while working.
                debug( "error caught: " + t );
                throwable = t;
            }
            finally
            {
                debug( "done." );
                m_work = null;
                m_threadControl.finish( throwable );
                m_threadControl = null;
                postExecute();
            }

            synchronized( this )
            {
                //should this be just notify or notifyAll ???
                //It seems to resource intensive option to use notify()
                //notifyAll();
                notifyAll();
            }

            // recycle ourselves
            recycleThread();
        }
    }

    /**
     * Implement this method to replace thread back into pool.
     */
    protected void recycleThread()
    {
        if( m_alive )
        {
            m_pool.threadCompleted( this );
        }
    }

    /**
     * Overide this method to execute something after
     * each bit of "work".
     */
    protected void postExecute()
    {
    }

    /**
     * Overide this method to execute something before
     * each bit of "work".
     */
    protected void preExecute()
    {
        //TODO: Thread name setting should reuse the
        //ThreadContext code if ThreadContext used.
        Thread.currentThread().setName( m_name );
    }

    /**
     * Set the <tt>alive</tt> variable to false causing the worker to die.
     * If the worker is stalled and a timeout generated this call, this method
     * does not change the state of the worker (that must be destroyed in other
     * ways).
     */
    void dispose( final int maxWait )
    {
        debug( "destroying." );
        m_alive = false;
        interrupt();
        synchronized( this )
        {
            final long start = System.currentTimeMillis();
            while( null != m_work )
            {
                final long now = System.currentTimeMillis();
                final long diff = now - start;
                if( diff >= maxWait )
                {
                    return;
                }
                try
                {
                    final long timeout = maxWait - diff;
                    debug( "waiting timeout=" + timeout + "." );
                    wait( timeout );
                    debug( "notified." );
                }
                catch( final InterruptedException ie )
                {
                }
            }
        }
    }

    /**
     * Set the <tt>Work</tt> code this <tt>Worker</tt> must
     * execute and <i>notifies</i> its thread to do it.
     */
    protected synchronized ThreadControl execute( final Executable work )
    {
        m_work = work;
        m_threadControl = new DefaultThreadControl( this );

        debug( "notifying this worker." );
        notify();

        return m_threadControl;
    }

    /**
     * Set the <tt>Work</tt> code this <tt>Worker</tt> must
     * execute and <i>notifies</i> its thread to do it. Wait
     * until the executable has finished before returning.
     */
/*
    protected synchronized void executeAndWait( final Executable work )
    {
        execute( work );
        waitUntilCondition( false );
    }
*/

    /**
     * Wait until the worker either has work or doesn't have work.
     *
     * @param hasWork true if waiting till work is present, false otherwise
     */
    private synchronized void waitUntilCondition( final boolean hasWork )
    {
        while( hasWork == ( null == m_work ) )
        {
            try
            {
                debug( "waiting." );
                wait();
                debug( "notified." );
            }
            catch( final InterruptedException ie )
            {
            }
        }
    }

    /**
     * Write a debug message.
     * A Noop oin this implementation. Subclasses can overide
     * to actually do some logging.
     *
     * @param message the message to write out
     */
    protected void debug( final String message )
    {
        //System.out.println( getName() + "::" + message );
    }
}
