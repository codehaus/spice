package org.codehaus.spice.netevent.events;

import java.nio.channels.SocketChannel;
import junit.framework.TestCase;
import org.codehaus.spice.netevent.transport.TcpTransport;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-07 04:02:38 $
 */
public class EventsTestCase
    extends TestCase
{
    public void testNullPassedIntoAbstractTransportEventCtor()
        throws Exception
    {
        try
        {
            new CloseEvent( null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "transport", npe.getMessage() );
            return;
        }
        fail( "Expected to get a npe for null passed into ctor" );
    }

    public void testTransportPassedAbstractTransportEvent()
        throws Exception
    {
        final SocketChannel channel = SocketChannel.open();
        final TcpTransport transport = new TcpTransport( channel, 1, 1 );
        final BufferOverflowEvent event =
            new BufferOverflowEvent( transport );

        assertEquals( "event.getTransport()", transport, event.getTransport() );
    }

    public void testToStringOnAbstractTransportEvent()
        throws Exception
    {
        final SocketChannel channel = SocketChannel.open();
        final TcpTransport transport = new TcpTransport( channel, 1, 1 );
        final BufferUnderflowEvent event =
            new BufferUnderflowEvent( transport );

        final String expected = "org.codehaus.spice.netevent.events.BufferUnderflowEvent[null]";
        assertEquals( "event.toString()", expected, event.toString() );
    }
}
