package org.realityforge.packet.net.handlers;

import java.util.HashMap;
import java.util.Map;
import org.codehaus.spice.event.AbstractEventHandler;
import org.codehaus.spice.event.EventHandler;
import org.realityforge.packet.net.CloseEvent;
import org.realityforge.packet.net.ConnectEvent;
import org.realityforge.packet.net.IOErrorEvent;
import org.realityforge.packet.net.ReadEvent;
import org.realityforge.packet.transport.CircularBuffer;
import org.realityforge.packet.transport.TcpTransport;
import org.realityforge.sca.selector.SelectorEventHandler;
import org.realityforge.sca.selector.SelectorManager;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-07 01:42:47 $
 */
class SocketEventHandler
    extends AbstractEventHandler
{
    private final Map _counts = new HashMap();
    private final CloseEventHandler _closeHandler;
    private final ConnectEventHandler _connectHandler;

    public SocketEventHandler( final SelectorManager selectorManager,
                               final SelectorEventHandler handler )
    {
        _closeHandler = new CloseEventHandler();
        _connectHandler = new ConnectEventHandler( selectorManager, handler );
    }

    /**
     * @see EventHandler#handleEvent(Object)
     */
    public void handleEvent( final Object event )
    {
        if( event instanceof CloseEvent )
        {
            final CloseEvent ce = (CloseEvent)event;
            final TcpTransport transport = ce.getTransport();
            final int port = transport.getChannel().socket().getPort();
            final int count = getTotalBytesRead( transport );
            System.out.println( "Received " + count + "B via " + port );
        }

        if( event instanceof CloseEvent ||
            event instanceof IOErrorEvent )
        {
            _closeHandler.handleEvent( event );
        }
        else if( event instanceof ConnectEvent )
        {
            _connectHandler.handleEvent( event );
        }

        if( event instanceof ReadEvent )
        {
            final ReadEvent re = (ReadEvent)event;
            final TcpTransport transport = re.getTransport();
            final CircularBuffer buffer = transport.getReceiveBuffer();
            buffer.readBytes( re.getCount() );

            final int total =
                getTotalBytesRead( transport ) + re.getCount();
            _counts.put( transport, new Integer( total ) );
        }
    }

    private int getTotalBytesRead( final TcpTransport transport )
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
