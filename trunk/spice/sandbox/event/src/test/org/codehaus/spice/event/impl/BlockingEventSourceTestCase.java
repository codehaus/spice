package org.codehaus.spice.event.impl;

import junit.framework.TestCase;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-05-17 03:52:23 $
 */
public class BlockingEventSourceTestCase
    extends TestCase
{
    private Object m_result;
    private boolean m_started;
    private boolean m_returned;

    public void testNull_buffer_PassedIntoCtor()
        throws Exception
    {
        try
        {
            new BlockingEventSource( null, 0 );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "source", npe.getMessage() );
            return;
        }
        fail( "Expected a NPE when passing source into Ctor" );
    }

    public void testEternallyBlocked()
        throws Exception
    {
        final BlockingTestEventSource base = new BlockingTestEventSource();
        final BlockingEventSource source = new BlockingEventSource( base, 0 );

        final Runnable runnable = new Runnable()
        {
            public void run()
            {
                m_result = source.getEvent();
            }
        };

        final Thread thread = startThread( runnable );
        Thread.sleep( 30 );
        assertEquals( "Eternally Blocking - Locked - has returned?",
                      false,
                      m_returned );
        assertEquals( "Eternally Blocking - Locked",
                      null,
                      m_result );
        base.unlock();
        Thread.sleep( 2 );
        assertEquals( "Eternally Blocking - UnLocked - has returned?",
                      true,
                      m_returned );
        assertEquals( "Eternally Blocking - UnLocked",
                      BlockingTestEventSource.EVENT,
                      m_result );
        destroy( thread );
    }

    private void destroy( final Thread thread )
        throws InterruptedException
    {
        thread.interrupt();
        thread.join();
    }

    public void testTimeBlocked()
        throws Exception
    {
        final BlockingTestEventSource base = new BlockingTestEventSource();
        final BlockingEventSource source = new BlockingEventSource( base, 200 );

        final Runnable runnable = new Runnable()
        {
            public void run()
            {
                m_result = source.getEvent();
            }
        };

        final Thread thread = startThread( runnable );
        Thread.sleep( 30 );
        assertEquals( "Pre-Timeout Blocking - Locked - has returned?",
                      false,
                      m_returned );
        assertEquals( "Pre-Timeout Blocking - Locked",
                      null,
                      m_result );
        Thread.sleep( 300 );
        assertEquals( "Post-Timeout Blocking has returned?",
                      true,
                      m_returned );
        assertEquals( "Post-Timeout Blocking",
                      null,
                      m_result );
        destroy( thread );

        final Thread thread2 = startThread( runnable );
        base.unlock();
        assertEquals( "UnLocked - has returned?",
                      true,
                      m_returned );
        assertEquals( "UnLocked result?",
                      BlockingTestEventSource.EVENT,
                      m_result );
        destroy( thread2 );
    }

    private Thread startThread( final Runnable runnable )
        throws InterruptedException
    {
        final Runnable work = new Runnable()
        {
            public void run()
            {
                m_started = true;
                runnable.run();
                m_returned = true;
            }
        };

        m_result = null;
        m_returned = false;
        m_started = false;
        final Thread thread = new Thread( work );
        thread.start();
        while( false == m_started )
        {
            Thread.sleep( 2 );
        }

        return thread;
    }
}
