package org.codehaus.spice.event.impl;

import junit.framework.TestCase;

public class NullEventValveTestCase
    extends TestCase
{
    public void testGetEvents()
    {
        final NullEventValve sink = new NullEventValve();
        assertEquals( "acceptEvent()",
                      true,
                      sink.acceptEvent( new Object() ) );
        assertEquals( "acceptEvents()",
                      true,
                      sink.acceptEvents( new Object[]{new Object()} ) );
    }
}
