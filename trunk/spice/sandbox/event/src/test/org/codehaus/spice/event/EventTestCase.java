package org.codehaus.spice.event;

import junit.framework.TestCase;

public class EventTestCase
    extends TestCase
{
    public void testGetShortName()
    {
        final DummyEvent handler = new DummyEvent();
        assertEquals( "getShortName()", "DummyEvent", handler.getShortName() );
    }

    public void testGetString()
    {
        final DummyEvent handler = new DummyEvent();
        assertEquals( "toString()", "DummyEvent[Blah]", handler.toString() );
    }
}
