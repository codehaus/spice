package org.codehaus.spice.event.impl;

import junit.framework.TestCase;
import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSource;
import org.jmock.C;
import org.jmock.Mock;

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-02-11 04:01:13 $
 */
public class EventPumpTestCase
    extends TestCase
{
    public void testNull_source_PassedIntoCtor()
        throws Exception
    {
        final Mock mockHandler = new Mock( EventHandler.class );
        final EventHandler handler = (EventHandler)mockHandler.proxy();
        try
        {
            new EventPump( null, handler );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "source", npe.getMessage() );
            return;
        }
        fail( "Expected a NPE when passing source into Ctor" );
    }

    public void testNull_handler_PassedIntoCtor()
        throws Exception
    {
        final Mock mockSource = new Mock( EventSource.class );
        final EventSource source = (EventSource)mockSource.proxy();
        try
        {
            new EventPump( source, null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "handler", npe.getMessage() );
            return;
        }
        fail( "Expected a NPE when passing handler into Ctor" );
    }

    public void testCreation()
        throws Exception
    {
        final Mock mockSource = new Mock( EventSource.class );
        final EventSource source = (EventSource)mockSource.proxy();

        final Mock mockHandler = new Mock( EventHandler.class );
        final EventHandler handler = (EventHandler)mockHandler.proxy();

        final EventPump pump = new EventPump( source, handler );
        assertEquals( "pump.m_source", source, pump.m_source );
        assertEquals( "pump.m_handler", handler, pump.m_handler );

        mockHandler.verify();
        mockSource.verify();
    }

    public void testSetBatchCount()
        throws Exception
    {
        final Mock mockSource = new Mock( EventSource.class );
        final EventSource source = (EventSource)mockSource.proxy();

        final Mock mockHandler = new Mock( EventHandler.class );
        final EventHandler handler = (EventHandler)mockHandler.proxy();

        final EventPump pump = new EventPump( source, handler );
        assertEquals( "batchSize", Integer.MAX_VALUE, pump.getBatchSize() );
        pump.setBatchSize( 15 );
        assertEquals( "batchSize", 15, pump.getBatchSize() );

        mockHandler.verify();
        mockSource.verify();
    }

    public void testRefreshNonBatchedWithHandling()
        throws Exception
    {
        final Object event = new Object();
        final Mock mockSource = new Mock( EventSource.class );
        mockSource.expectAndReturn( "getEvent", event );
        final EventSource source = (EventSource)mockSource.proxy();

        final Mock mockHandler = new Mock( EventHandler.class );
        mockHandler.expect( "handleEvent", C.args( C.eq( event ) ) );
        final EventHandler handler = (EventHandler)mockHandler.proxy();

        final EventPump pump = new EventPump( source, handler );
        pump.setBatchSize( 1 );

        pump.refresh();

        mockHandler.verify();
        mockSource.verify();
    }

    public void testRefreshNonBatchedWithNoHandling()
        throws Exception
    {
        final Mock mockSource = new Mock( EventSource.class );
        mockSource.expectAndReturn( "getEvent", null );
        final EventSource source = (EventSource)mockSource.proxy();

        final Mock mockHandler = new Mock( EventHandler.class );
        final EventHandler handler = (EventHandler)mockHandler.proxy();

        final EventPump pump = new EventPump( source, handler );
        pump.setBatchSize( 1 );

        pump.refresh();

        mockHandler.verify();
        mockSource.verify();
    }

    public void testRefreshBatchedWithHandling()
        throws Exception
    {
        final Object event = new Object();
        final Object[] events = new Object[]{event};
        final Mock mockSource = new Mock( EventSource.class );
        mockSource.expectAndReturn( "getEvents", C.args( C.eq( 20 ) ), events );
        final EventSource source = (EventSource)mockSource.proxy();

        final Mock mockHandler = new Mock( EventHandler.class );
        mockHandler.expect( "handleEvents", C.args( C.eq( events ) ) );
        final EventHandler handler = (EventHandler)mockHandler.proxy();

        final EventPump pump = new EventPump( source, handler );
        pump.setBatchSize( 20 );

        pump.refresh();

        mockHandler.verify();
        mockSource.verify();
    }

    public void testRefreshBatchedWithNoHandling()
        throws Exception
    {
        final Object[] events = new Object[ 0 ];
        final Mock mockSource = new Mock( EventSource.class );
        mockSource.expectAndReturn( "getEvents", C.args( C.eq( 20 ) ), events );
        final EventSource source = (EventSource)mockSource.proxy();

        final Mock mockHandler = new Mock( EventHandler.class );
        final EventHandler handler = (EventHandler)mockHandler.proxy();

        final EventPump pump = new EventPump( source, handler );
        pump.setBatchSize( 20 );

        pump.refresh();

        mockHandler.verify();
        mockSource.verify();
    }
}
