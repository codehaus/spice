package org.codehaus.spice.event.impl;

import junit.framework.TestCase;

public class NullEventSinkTestCase
    extends TestCase
{
    public void testGetEvents()
    {
        final NullEventSink sink = new NullEventSink();
        assertEquals( "getSinkLock()", sink, sink.getSinkLock() );
        assertEquals( "addEvent()", false, sink.addEvent( new Object() ) );
        assertEquals( "addEvents()",
                      false,
                      sink.addEvents( new Object[]{new Object()} ) );
    }
}
