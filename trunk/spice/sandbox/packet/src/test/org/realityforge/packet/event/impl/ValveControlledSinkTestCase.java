package org.realityforge.packet.event.impl;

import junit.framework.TestCase;
import org.jmock.C;
import org.jmock.Mock;
import org.realityforge.packet.event.EventSink;
import org.realityforge.packet.event.EventValve;

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2003-12-09 03:11:47 $
 */
public class ValveControlledSinkTestCase
    extends TestCase
{
    public void testNullSinkPassedIntoCtor()
        throws Exception
    {
        final Mock mockValve = new Mock( EventValve.class );
        final EventValve valve = (EventValve)mockValve.proxy();
        try
        {
            new ValveControlledSink( null, valve );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "sink", npe.getMessage() );
            return;
        }
        fail( "Expected a NPE when passing sink into Ctor" );
    }

    public void testNullValvePassedIntoCtor()
        throws Exception
    {
        final Mock mockSink = new Mock( EventSink.class );
        final EventSink sink = (EventSink)mockSink.proxy();
        try
        {
            new ValveControlledSink( sink, null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "valve", npe.getMessage() );
            return;
        }
        fail( "Expected a NPE when passing valve into Ctor" );
    }

    public void testCreation()
        throws Exception
    {
        final Mock mockValve = new Mock( EventValve.class );
        final EventValve valve = (EventValve)mockValve.proxy();

        final Mock mockSink = new Mock( EventSink.class );
        final EventSink sink = (EventSink)mockSink.proxy();
        final ValveControlledSink vcs = new ValveControlledSink( sink, valve );
        assertEquals( "vcs.m_sink", sink, vcs.m_sink );
        assertEquals( "vcs.m_valve", valve, vcs.m_valve );

        mockSink.verify();
        mockValve.verify();
    }

    public void testGetSyncLock()
        throws Exception
    {
        final Mock mockValve = new Mock( EventValve.class );
        final EventValve valve = (EventValve)mockValve.proxy();

        final Mock mockSink = new Mock( EventSink.class );
        final Object lock = new Object();
        mockSink.expectAndReturn( "getSinkLock", lock );
        final EventSink sink = (EventSink)mockSink.proxy();
        final ValveControlledSink vcs = new ValveControlledSink( sink, valve );

        assertEquals( "vcs.getSinkLock()", lock, vcs.getSinkLock() );

        mockSink.verify();
        mockValve.verify();
    }

    public void testAddEventsThatAccepted()
        throws Exception
    {
        final Object[] events = new Object[ 0 ];
        final Mock mockValve = new Mock( EventValve.class );
        mockValve.expectAndReturn( "acceptEvents",
                                   C.args( C.eq( events ) ),
                                   true );
        final EventValve valve = (EventValve)mockValve.proxy();

        final Mock mockSink = new Mock( EventSink.class );
        mockSink.expectAndReturn( "addEvents",
                                  C.args( C.eq( events ) ),
                                  true );
        final EventSink sink = (EventSink)mockSink.proxy();
        final ValveControlledSink vcs = new ValveControlledSink( sink, valve );

        assertEquals( "vcs.addEvents( events )", true, vcs.addEvents( events ) );

        mockSink.verify();
        mockValve.verify();
    }

    public void testAddEventsThatNotAccepted()
        throws Exception
    {
        final Object[] events = new Object[ 0 ];
        final Mock mockValve = new Mock( EventValve.class );
        mockValve.expectAndReturn( "acceptEvents",
                                   C.args( C.eq( events ) ),
                                   false );
        final EventValve valve = (EventValve)mockValve.proxy();

        final Mock mockSink = new Mock( EventSink.class );
        final EventSink sink = (EventSink)mockSink.proxy();
        final ValveControlledSink vcs = new ValveControlledSink( sink, valve );

        assertEquals( "vcs.addEvents( events )", false,
                      vcs.addEvents( events ) );

        mockSink.verify();
        mockValve.verify();
    }

    public void testAddEventThatAccepted()
        throws Exception
    {
        final Object event = new Object();
        final Mock mockValve = new Mock( EventValve.class );
        mockValve.expectAndReturn( "acceptEvent",
                                   C.args( C.eq( event ) ),
                                   true );
        final EventValve valve = (EventValve)mockValve.proxy();

        final Mock mockSink = new Mock( EventSink.class );
        mockSink.expectAndReturn( "addEvent",
                                  C.args( C.eq( event ) ),
                                  true );
        final EventSink sink = (EventSink)mockSink.proxy();
        final ValveControlledSink vcs = new ValveControlledSink( sink, valve );

        assertEquals( "vcs.addEvent( event )", true, vcs.addEvent( event ) );

        mockSink.verify();
        mockValve.verify();
    }

    public void testAddEventThatNotAccepted()
        throws Exception
    {
        final Object event = new Object();
        final Mock mockValve = new Mock( EventValve.class );
        mockValve.expectAndReturn( "acceptEvent",
                                   C.args( C.eq( event ) ),
                                   false );
        final EventValve valve = (EventValve)mockValve.proxy();

        final Mock mockSink = new Mock( EventSink.class );
        final EventSink sink = (EventSink)mockSink.proxy();
        final ValveControlledSink vcs = new ValveControlledSink( sink, valve );

        assertEquals( "vcs.addEvent( event )", false,
                      vcs.addEvent( event ) );

        mockSink.verify();
        mockValve.verify();
    }
}
