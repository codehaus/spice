package org.codehaus.spice.netevent;

import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.spice.event.AbstractEventHandler;
import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.events.AcceptPossibleEvent;
import org.codehaus.spice.netevent.events.ChannelClosedEvent;
import org.codehaus.spice.netevent.events.ConnectEvent;
import org.codehaus.spice.netevent.events.IOErrorEvent;
import org.codehaus.spice.netevent.events.ReadEvent;
import org.codehaus.spice.netevent.events.ReadPossibleEvent;
import org.codehaus.spice.netevent.events.WritePossibleEvent;
import org.codehaus.spice.netevent.handlers.AcceptEventHandler;
import org.codehaus.spice.netevent.handlers.CloseEventHandler;
import org.codehaus.spice.netevent.handlers.ConnectEventHandler;
import org.codehaus.spice.netevent.handlers.ReadEventHandler;
import org.codehaus.spice.netevent.handlers.WriteEventHandler;
import org.codehaus.spice.netevent.selector.SocketEventSource;
import org.codehaus.spice.netevent.transport.ChannelTransport;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-08 03:41:14 $
 */
class TestSocketEventHandler
    extends AbstractEventHandler
{
    private final Map _counts = new HashMap();
    private final CloseEventHandler _closeHandler;
    private final ReadEventHandler _readEventHandler;
    private final WriteEventHandler _writeEventHandler;
    private final AcceptEventHandler _acceptHandler;
    private final ConnectEventHandler _connectHandler;

    public TestSocketEventHandler( final SocketEventSource source,
                                   final EventSink queue )
    {
        _closeHandler = new CloseEventHandler();
        _acceptHandler = new AcceptEventHandler( queue );
        _connectHandler = new ConnectEventHandler( queue, source );
        _readEventHandler = new ReadEventHandler( queue );
        _writeEventHandler = new WriteEventHandler( queue );
    }

    /**
     * @see EventHandler#handleEvent(Object)
     */
    public void handleEvent( final Object event )
    {
        //System.out.println( "event = " + event );
        if( event instanceof ChannelClosedEvent )
        {
            final ChannelClosedEvent ce = (ChannelClosedEvent)event;
            final ChannelTransport transport = ce.getTransport();
            final Channel channel = transport.getChannel();
            final int port = ((SocketChannel)channel).socket().getPort();
            final int count = getTotalBytesRead( transport );
            System.out.println( "Received " + count + "B via " + port );
        }

        if( event instanceof ChannelClosedEvent ||
            event instanceof IOErrorEvent )
        {
            _closeHandler.handleEvent( event );
        }
        else if( event instanceof AcceptPossibleEvent )
        {
            _acceptHandler.handleEvent( event );
        }
        else if( event instanceof ConnectEvent )
        {
            _connectHandler.handleEvent( event );
        }
        else if( event instanceof ReadPossibleEvent )
        {
            _readEventHandler.handleEvent( event );
        }
        else if( event instanceof WritePossibleEvent )
        {
            _writeEventHandler.handleEvent( event );
        }
        else if( event instanceof ReadEvent )
        {
            final ReadEvent re = (ReadEvent)event;
            final ChannelTransport transport = re.getTransport();

            final int total =
                getTotalBytesRead( transport ) + re.getBuffer().position();

            _counts.put( transport, new Integer( total ) );
        }
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
