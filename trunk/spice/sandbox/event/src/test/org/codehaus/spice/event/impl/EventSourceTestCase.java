package org.codehaus.spice.event.impl;

import junit.framework.TestCase;
import org.codehaus.spice.event.EventJoin;
import org.jmock.C;
import org.jmock.Mock;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-05-12 08:04:09 $
 */
public class EventSourceTestCase
    extends TestCase
{
    public void testNull_buffer_PassedIntoCtor()
        throws Exception
    {
        try
        {
            new DummyEventSource( null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "join", npe.getMessage() );
            return;
        }
        fail( "Expected a NPE when passing join into Ctor" );
    }

    public void testAddEventSuccessfully()
        throws Exception
    {
        final Object lock = new Object();
        final Object event = new Object();
        final Object[] events = new Object[]{event};

        final Mock mockBuffer = new Mock( EventJoin.class );
        mockBuffer.expectAndReturn( "getEvent", event );
        mockBuffer.expectAndReturn( "getEvents", C.ANY_ARGS, events );
        mockBuffer.expectAndReturn( "getSourceLock", C.NO_ARGS, lock );

        final EventJoin join = (EventJoin) mockBuffer.proxy();
        final DummyEventSource source = new DummyEventSource( join );

        assertEquals( "PreOpen: isOpenCalled()", false, source.isOpenCall() );
        source.open();
        assertEquals( "PostOpen: isOpenCalled()", true, source.isOpenCall() );

        assertEquals( "PreRefresh: isRefreshCalled()", false, source.isRefreshCall() );
        source.refresh();
        assertEquals( "PostRefresh: isRefreshCalled()", true, source.isRefreshCall() );

        assertEquals( "PreClose: isCloseCalled()", false, source.isCloseCall() );
        source.close();
        assertEquals( "PostClose: isCloseCalled()", true, source.isCloseCall() );

        assertEquals( "getSourceLock()", lock, source.getSourceLock() );

        assertEquals( "getJoin()", join, source.getJoin() );

        assertEquals( "getEvent()", event, source.getEvent() );

        assertEquals( "getEvents()", events, source.getEvents( 1 ) );

        mockBuffer.verify();
    }
}
