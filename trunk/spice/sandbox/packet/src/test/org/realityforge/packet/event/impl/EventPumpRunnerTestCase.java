package org.realityforge.packet.event.impl;

import junit.framework.TestCase;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-09 05:27:05 $
 */
public class EventPumpRunnerTestCase
    extends TestCase
{
    public void testNull_eventPumps_PassedIntoCtor()
        throws Exception
    {
        try
        {
            new EventPumpRunner( null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "eventPumps", npe.getMessage() );
            return;
        }
        fail( "Expected a NPE when passing eventPumps into Ctor" );
    }

    public void testNull_eventPumps_ElementPassedIntoCtor()
        throws Exception
    {
        try
        {
            new EventPumpRunner( new EventPump[ 1 ] );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "eventPumps[0]",
                          npe.getMessage() );
            return;
        }
        fail( "Expected a NPE when passing eventPumps[0] into Ctor" );
    }

    public void testCreation()
        throws Exception
    {
        final EventPump[] eventPumps = new EventPump[ 0 ];
        final EventPumpRunner runner = new EventPumpRunner( eventPumps );
        assertEquals( "runner._eventPumps", eventPumps, runner._eventPumps );
    }

    public void testStartThenDeactivate()
        throws Exception
    {
        final EventPump[] eventPumps = new EventPump[ 0 ];
        final EventPumpRunner runner = new EventPumpRunner( eventPumps );

        synchronized( runner )
        {
            final Thread thread = new Thread( runner );
            thread.start();
            runner.waitUntilStarted();
        }
        assertEquals( "runner._started", true, runner._started );
        assertEquals( "runner._active", true, runner._active );
        runner.deactivate();
        assertEquals( "runner._finished", true, runner._finished );
        assertEquals( "runner._active", false, runner._active );
    }

    public void testSimpleRun()
        throws Exception
    {
        final DummyEventHandler eventHandler = new DummyEventHandler();
        final DummyEventSource source = new DummyEventSource();
        final EventPump pump = new EventPump( source, eventHandler );
        final EventPump[] eventPumps = new EventPump[]{pump};
        final EventPumpRunner runner = new EventPumpRunner( eventPumps );

        synchronized( runner )
        {
            final Thread thread = new Thread( runner );
            thread.start();
            runner.waitUntilStarted();
        }

        assertEquals( "runner._started", true, runner._started );
        assertEquals( "runner._active", true, runner._active );
        Thread.sleep( 30 );

        assertTrue( "eventHandler.getCallCount() > 0",
                    eventHandler.getCallCount() > 0 );

        runner.deactivate();
        assertEquals( "runner._finished", true, runner._finished );
        assertEquals( "runner._active", false, runner._active );
    }
}
