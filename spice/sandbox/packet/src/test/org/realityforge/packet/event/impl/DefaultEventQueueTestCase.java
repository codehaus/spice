package org.realityforge.packet.event.impl;

import junit.framework.TestCase;
import org.jmock.C;
import org.jmock.Mock;
import org.realityforge.packet.event.impl.collections.Buffer;

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2003-12-09 04:10:31 $
 */
public class DefaultEventQueueTestCase
    extends TestCase
{
    public void testNull_buffer_PassedIntoCtor()
        throws Exception
    {
        try
        {
            new DefaultEventQueue( null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "buffer", npe.getMessage() );
            return;
        }
        fail( "Expected a NPE when passing buffer into Ctor" );
    }

    public void testCreation()
        throws Exception
    {
        final Mock mockBuffer = new Mock( Buffer.class );
        final Buffer buffer = (Buffer)mockBuffer.proxy();
        final DefaultEventQueue queue = new DefaultEventQueue( buffer );
        assertEquals( "buffer", buffer, queue.getBuffer() );
    }

    public void testAddEventSuccessfully()
        throws Exception
    {
        final Object object = new Object();

        final Mock mockBuffer = new Mock( Buffer.class );
        mockBuffer.expectAndReturn( "add", C.args( C.eq( object ) ), true );
        final Buffer buffer = (Buffer)mockBuffer.proxy();
        final DefaultEventQueue queue = new DefaultEventQueue( buffer );

        final LockChecker checker = new LockChecker( queue.getSourceLock() );
        assertEquals( "Lock Notified pre-add", false, checker.isUnlocked() );
        assertEquals( "queue.addEvent( object )", true,
                      queue.addEvent( object ) );
        assertEquals( "Lock Notified  post-add", true, checker.isUnlocked() );

        mockBuffer.verify();
    }

    public void testAddEventUnsuccessfully()
        throws Exception
    {
        final Object object = new Object();

        final Mock mockBuffer = new Mock( Buffer.class );
        mockBuffer.expectAndReturn( "add", C.args( C.eq( object ) ), false );
        final Buffer buffer = (Buffer)mockBuffer.proxy();
        final DefaultEventQueue queue = new DefaultEventQueue( buffer );

        final LockChecker checker = new LockChecker( queue.getSourceLock() );
        assertEquals( "Lock Notified pre-add", false, checker.isUnlocked() );
        assertEquals( "queue.addEvent( object )", false,
                      queue.addEvent( object ) );
        assertEquals( "Lock Notified  post-add", false, checker.isUnlocked() );

        mockBuffer.verify();
    }

    public void testAddEventsSuccessfully()
        throws Exception
    {
        final Object[] objects = new Object[ 0 ];

        final Mock mockBuffer = new Mock( Buffer.class );
        mockBuffer.expectAndReturn( "addAll", C.args( C.eq( objects ) ), true );
        final Buffer buffer = (Buffer)mockBuffer.proxy();
        final DefaultEventQueue queue = new DefaultEventQueue( buffer );

        final LockChecker checker = new LockChecker( queue.getSourceLock() );
        assertEquals( "Lock Notified pre-add", false, checker.isUnlocked() );
        assertEquals( "queue.addEvents( objects )", true,
                      queue.addEvents( objects ) );
        assertEquals( "Lock Notified  post-add", true, checker.isUnlocked() );

        mockBuffer.verify();
    }

    public void testAddEventsUnsuccessfully()
        throws Exception
    {
        final Object[] objects = new Object[ 0 ];

        final Mock mockBuffer = new Mock( Buffer.class );
        mockBuffer.expectAndReturn( "addAll", C.args( C.eq( objects ) ), false );
        final Buffer buffer = (Buffer)mockBuffer.proxy();
        final DefaultEventQueue queue = new DefaultEventQueue( buffer );

        final LockChecker checker = new LockChecker( queue.getSourceLock() );
        assertEquals( "Lock Notified pre-add", false, checker.isUnlocked() );
        assertEquals( "queue.addEvents( objects )", false,
                      queue.addEvents( objects ) );
        assertEquals( "Lock Notified  post-add", false, checker.isUnlocked() );

        mockBuffer.verify();
    }

    public void testGetEventSuccessfully()
        throws Exception
    {
        final Object object = new Object();

        final Mock mockBuffer = new Mock( Buffer.class );
        mockBuffer.expectAndReturn( "size", 1 );
        mockBuffer.expectAndReturn( "pop", object );
        final Buffer buffer = (Buffer)mockBuffer.proxy();

        final DefaultEventQueue queue = new DefaultEventQueue( buffer );
        final LockChecker checker = new LockChecker( queue.getSinkLock() );
        assertEquals( "Lock Notified pre-add", false, checker.isUnlocked() );
        assertEquals( "queue.getEvent()", object, queue.getEvent() );
        assertEquals( "Lock Notified  post-add", true, checker.isUnlocked() );

        mockBuffer.verify();
    }

    public void testGetEventUnsuccessfully()
        throws Exception
    {
        final Mock mockBuffer = new Mock( Buffer.class );
        mockBuffer.expectAndReturn( "size", 0 );
        final Buffer buffer = (Buffer)mockBuffer.proxy();

        final DefaultEventQueue queue = new DefaultEventQueue( buffer );
        final LockChecker checker = new LockChecker( queue.getSinkLock() );
        assertEquals( "Lock Notified pre-add", false, checker.isUnlocked() );
        assertEquals( "queue.getEvent()", null, queue.getEvent() );
        assertEquals( "Lock Notified  post-add", false, checker.isUnlocked() );

        mockBuffer.verify();
    }

    public void testGetEventsSuccessfully()
        throws Exception
    {
        final Object object = new Object();

        final Mock mockBuffer = new Mock( Buffer.class );
        mockBuffer.expectAndReturn( "size", 1 );
        mockBuffer.expectAndReturn( "pop", object );
        final Buffer buffer = (Buffer)mockBuffer.proxy();

        final DefaultEventQueue queue = new DefaultEventQueue( buffer );
        final LockChecker checker = new LockChecker( queue.getSinkLock() );
        assertEquals( "Lock Notified pre-add", false, checker.isUnlocked() );
        final Object[] events = queue.getEvents( 20 );
        assertEquals( "Lock Notified  post-add", true, checker.isUnlocked() );

        assertEquals( "events.length", 1, events.length );
        assertEquals( "events[ 0 ]", object, events[ 0 ] );

        mockBuffer.verify();
    }

    public void testGetEventsUnsuccessfully()
        throws Exception
    {
        final Mock mockBuffer = new Mock( Buffer.class );
        mockBuffer.expectAndReturn( "size", 0 );
        final Buffer buffer = (Buffer)mockBuffer.proxy();

        final DefaultEventQueue queue = new DefaultEventQueue( buffer );
        final LockChecker checker = new LockChecker( queue.getSinkLock() );
        assertEquals( "Lock Notified pre-add", false, checker.isUnlocked() );
        final Object[] events = queue.getEvents( 20 );
        assertEquals( "Lock Notified  post-add", false, checker.isUnlocked() );

        assertEquals( "events.length", 0, events.length );

        mockBuffer.verify();
    }
}
