package org.codehaus.spice.netevent.events;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import junit.framework.TestCase;
import org.codehaus.spice.netevent.transport.TcpTransport;

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-01-07 04:13:36 $
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

    public void testNull_channel_PassedIntoCtor()
        throws Exception
    {
        try
        {
            new ConnectEvent( null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "channel", npe.getMessage() );
            return;
        }
        fail( "Expected a NPE when passing channel into Ctor" );
    }

    public void testGetServerSocketChannelOnConnectEvent()
        throws Exception
    {
        final ServerSocketChannel channel = ServerSocketChannel.open();
        final ConnectEvent event = new ConnectEvent( channel );

        assertEquals( "event.getServerSocketChannel()", channel,
                      event.getServerSocketChannel() );
    }

    public void testToStringOnConnectEvent()
        throws Exception
    {
        final ServerSocketChannel channel = ServerSocketChannel.open();
        final ConnectEvent event = new ConnectEvent( channel );

        final String expected = "org.codehaus.spice.netevent.events.ConnectEvent[null]";
        assertEquals( "event.toString()", expected, event.toString() );
    }
}
