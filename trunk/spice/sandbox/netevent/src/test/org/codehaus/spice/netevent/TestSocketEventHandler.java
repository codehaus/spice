package org.codehaus.spice.netevent;

import java.nio.channels.SocketChannel;
import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.buffers.BufferManager;
import org.codehaus.spice.netevent.events.ChannelClosedEvent;
import org.codehaus.spice.netevent.handlers.ChannelEventHandler;
import org.codehaus.spice.netevent.selector.SocketEventSource;
import org.codehaus.spice.netevent.transport.ChannelTransport;

/**
 * @author Peter Donald
 * @version $Revision: 1.4 $ $Date: 2004-01-12 02:32:41 $
 */
class TestSocketEventHandler
    extends ChannelEventHandler
{
    public TestSocketEventHandler( final SocketEventSource source,
                                   final EventSink queue,
                                   final BufferManager bufferManager )
    {
        super( source, queue, bufferManager );
    }

    /**
     * @see EventHandler#handleEvent(Object)
     */
    public void handleEvent( final Object event )
    {
        /*
        System.out.println( "event = " + event );
        */
        if( event instanceof ChannelClosedEvent )
        {
            final ChannelClosedEvent ce = (ChannelClosedEvent)event;
            final ChannelTransport transport = ce.getTransport();
            final int port =
                ((SocketChannel)transport.getChannel()).socket().getPort();
            final String message =
                "Received " + transport.getReceivedData().available() +
                "B via " + port;
            System.out.println( message );
        }

        super.handleEvent( event );
    }
}
