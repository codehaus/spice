package org.codehaus.spice.netevent.handlers;

import junit.framework.TestCase;
import org.codehaus.spice.netevent.events.CloseEvent;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-07 05:06:31 $
 */
public class CloseEventHandlerTestCase
    extends TestCase
{
    public void testCloseCalled()
        throws Exception
    {
        final DummyTransport transport = new DummyTransport();
        final CloseEventHandler handler = new CloseEventHandler();
        final CloseEvent event = new CloseEvent( transport );
        handler.handleEvent( event );

        assertEquals( "transport.isClosed()", true, transport.isClosed() );
    }
}
