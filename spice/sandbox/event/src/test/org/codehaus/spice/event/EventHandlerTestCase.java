package org.codehaus.spice.event;

import junit.framework.TestCase;

public class EventHandlerTestCase
    extends TestCase
{
    public void testGetEvents()
    {
        final DummyEventHandler handler = new DummyEventHandler();
        assertEquals( "A handler.getCallCount()", 0, handler.getCallCount() );
        handler.handleEvent( new Object() );
        assertEquals( "B handler.getCallCount()", 1, handler.getCallCount() );
        handler.handleEvents( new Object[]{new Object(), new Object()} );
        assertEquals( "C handler.getCallCount()", 3, handler.getCallCount() );
    }
}
