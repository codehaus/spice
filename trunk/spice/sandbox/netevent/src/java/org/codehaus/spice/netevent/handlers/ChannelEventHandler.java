package org.codehaus.spice.netevent.handlers;

import org.codehaus.spice.event.AbstractEventHandler;
import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.buffers.BufferManager;
import org.codehaus.spice.netevent.events.AcceptEvent;
import org.codehaus.spice.netevent.events.AcceptPossibleEvent;
import org.codehaus.spice.netevent.events.ChannelClosedEvent;
import org.codehaus.spice.netevent.events.IOErrorEvent;
import org.codehaus.spice.netevent.events.ReadEvent;
import org.codehaus.spice.netevent.events.ReadPossibleEvent;
import org.codehaus.spice.netevent.events.WritePossibleEvent;
import org.codehaus.spice.netevent.selector.SocketEventSource;

/**
 * An event handler that is capable of handling any of the standard channel
 * events.
 * 
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2004-01-12 02:42:19 $
 */
public class ChannelEventHandler
    extends AbstractEventHandler
{
    private final InputDataEventHandler _dataHandler;
    private final CloseEventHandler _closeHandler;
    private final ReadEventHandler _readHandler;
    private final WriteEventHandler _writeHandler;
    private final AcceptEventHandler _acceptHandler;
    private final ConnectEventHandler _connectHandler;

    public ChannelEventHandler( final SocketEventSource source,
                                final EventSink queue,
                                final BufferManager bufferManager )
    {
        _closeHandler = new CloseEventHandler();
        _acceptHandler = new AcceptEventHandler( queue );
        _connectHandler =
        new ConnectEventHandler( queue, bufferManager, source );
        _readHandler = new ReadEventHandler( queue, bufferManager );
        _writeHandler = new WriteEventHandler( queue, bufferManager );
        _dataHandler = new InputDataEventHandler( queue );
    }

    /**
     * @see EventHandler#handleEvent(Object)
     */
    public void handleEvent( final Object event )
    {
        if( event instanceof ChannelClosedEvent ||
            event instanceof IOErrorEvent )
        {
            _closeHandler.handleEvent( event );
        }
        else if( event instanceof AcceptPossibleEvent )
        {
            _acceptHandler.handleEvent( event );
        }
        else if( event instanceof AcceptEvent )
        {
            _connectHandler.handleEvent( event );
        }
        else if( event instanceof ReadPossibleEvent )
        {
            _readHandler.handleEvent( event );
        }
        else if( event instanceof WritePossibleEvent )
        {
            _writeHandler.handleEvent( event );
        }
        else if( event instanceof ReadEvent )
        {
            _dataHandler.handleEvent( event );
        }
    }
}
