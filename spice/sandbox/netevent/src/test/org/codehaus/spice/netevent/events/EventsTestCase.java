package org.codehaus.spice.netevent.events;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import junit.framework.TestCase;
import org.codehaus.spice.event.impl.collections.UnboundedFifoBuffer;
import org.codehaus.spice.netevent.transport.ChannelTransport;

/**
 * @author Peter Donald
 * @version $Revision: 1.7 $ $Date: 2004-01-09 00:54:15 $
 */
public class EventsTestCase
    extends TestCase
{
    private ChannelTransport newTransport( final SocketChannel channel )
    {
        return new ChannelTransport( channel,
                                     new UnboundedFifoBuffer( 1 ) );
    }

    public void testNull_channel_PassedIntoCtor()
        throws Exception
    {
        try
        {
            new AcceptPossibleEvent( null );
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
        final AcceptPossibleEvent event = new AcceptPossibleEvent( channel );

        assertEquals( "event.getChannel()", channel,
                      event.getChannel() );
    }

    public void testToStringOnConnectEvent()
        throws Exception
    {
        final ServerSocketChannel channel = ServerSocketChannel.open();
        final AcceptPossibleEvent event = new AcceptPossibleEvent( channel );

        final String expected = "AcceptPossibleEvent[" +
                                channel.socket().getLocalSocketAddress() + "]";
        assertEquals( "event.toString()", expected, event.toString() );
    }

    public void testNull_ioe_PassedIntoCtor_of_IOError()
        throws Exception
    {
        final SocketChannel channel = SocketChannel.open();
        final ChannelTransport transport = newTransport( channel );
        try
        {
            new ReadErrorEvent( transport, null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "ioe", npe.getMessage() );
            return;
        }
        fail( "Expected a NPE when passing ioe into Ctor_of_IOError" );
    }

    public void testGetIoeOnWriteErrorEvent()
        throws Exception
    {
        final SocketChannel channel = SocketChannel.open();
        final ChannelTransport transport = newTransport( channel );
        final IOException ioe = new IOException( "X" );
        final WriteErrorEvent event = new WriteErrorEvent( transport, ioe );

        assertEquals( "event.getIoe()", ioe, event.getIoe() );
    }

    public void testToStringOnReadIOErrorEvent()
        throws Exception
    {
        final SocketChannel channel = SocketChannel.open();
        final ChannelTransport transport = newTransport( channel );
        final IOException ioe = new IOException( "X" );
        final ReadErrorEvent event = new ReadErrorEvent( transport, ioe );

        final String expected =
            "ReadErrorEvent[java.io.IOException: X error connected to " +
            channel + "]";
        assertEquals( "event.toString()", expected, event.toString() );
    }

    public void testToStringOnReadEvent()
        throws Exception
    {
        final SocketChannel channel = SocketChannel.open();
        final ChannelTransport transport = newTransport( channel );
        final ByteBuffer buffer = ByteBuffer.allocate( 4 );
        buffer.limit( 4 );
        buffer.position( 4 );
        final ReadEvent event = new ReadEvent( transport, buffer );

        final String expected =
            "ReadEvent[4 bytes to " + channel + "]";
        assertEquals( "event.toString()", expected, event.toString() );
    }
}
