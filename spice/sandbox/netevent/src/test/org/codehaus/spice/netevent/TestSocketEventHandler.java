package org.codehaus.spice.netevent;

import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.buffers.BufferManager;
import org.codehaus.spice.netevent.events.ChannelClosedEvent;
import org.codehaus.spice.netevent.events.ReadEvent;
import org.codehaus.spice.netevent.handlers.ChannelEventHandler;
import org.codehaus.spice.netevent.selector.SocketEventSource;
import org.codehaus.spice.netevent.transport.ChannelTransport;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2004-01-09 00:54:15 $
 */
class TestSocketEventHandler
    extends ChannelEventHandler
{
    private final Map _counts = new HashMap();
    private BufferManager _bufferManager;

    public TestSocketEventHandler( final SocketEventSource source,
                                   final EventSink queue,
                                   final BufferManager bufferManager )
    {
        super( source, queue, bufferManager );
        _bufferManager = bufferManager;
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
            final Channel channel = transport.getChannel();
            final int port = ((SocketChannel)channel).socket().getPort();
            final int count = getTotalBytesRead( transport );
            System.out.println( "Received " + count + "B via " + port );
        }
        else if( event instanceof ReadEvent )
        {
            final ReadEvent re = (ReadEvent)event;
            final ChannelTransport transport = re.getTransport();

            final int total =
                getTotalBytesRead( transport ) + re.getBuffer().position();

            _bufferManager.releaseBuffer( re.getBuffer() );

            _counts.put( transport, new Integer( total ) );
        }

        super.handleEvent( event );
    }

    private int getTotalBytesRead( final ChannelTransport transport )
    {
        final Integer integer = (Integer)_counts.get( transport );
        if( null == integer )
        {
            return 0;
        }
        else
        {
            return integer.intValue();
        }
    }
}
