package org.codehaus.spice.event.impl;

import junit.framework.TestCase;

public class NullEventValveTestCase
    extends TestCase
{
    public void testGetEvents()
    {
        final NullEventValve sink = new NullEventValve();
        assertEquals( "acceptEvent()",
                      false,
                      sink.acceptEvent( new Object() ) );
        assertEquals( "acceptEvents()",
                      false,
                      sink.acceptEvents( new Object[]{new Object()} ) );
    }
}
