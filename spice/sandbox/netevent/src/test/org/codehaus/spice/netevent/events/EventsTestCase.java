package org.codehaus.spice.netevent.events;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import junit.framework.TestCase;
import org.codehaus.spice.event.impl.NullEventSink;
import org.codehaus.spice.event.impl.collections.UnboundedFifoBuffer;
import org.codehaus.spice.netevent.buffers.DefaultBufferManager;
import org.codehaus.spice.netevent.transport.ChannelTransport;

/**
 * @author Peter Donald
 * @version $Revision: 1.12 $ $Date: 2004-01-21 03:44:55 $
 */
public class EventsTestCase
    extends TestCase
{
    private ChannelTransport newTransport( final SocketChannel channel )
    {
        return new ChannelTransport( channel,
                                     new UnboundedFifoBuffer( 1 ),
                                     new DefaultBufferManager(),
                                     new NullEventSink() );
    }

    public void testNull_channel_PassedIntoCtor()
        throws Exception
    {
        try
        {
            new AcceptPossibleEvent( null, new Object() );
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
        final AcceptPossibleEvent event = new AcceptPossibleEvent( channel,
                                                                   new Object() );

        assertEquals( "event.getChannel()", channel,
                      event.getChannel() );
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
}
