package org.realityforge.packet;

import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.buffers.BufferManager;
import org.codehaus.spice.netevent.handlers.ChannelEventHandler;
import org.codehaus.spice.netevent.selector.SocketEventSource;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-16 06:48:00 $
 */
public class ChannelEventHandlerWithEcho
    extends ChannelEventHandler
{
    private final String _header;

    public ChannelEventHandlerWithEcho( final SocketEventSource source,
                                        final EventSink queue,
                                        final EventSink target,
                                        final BufferManager bufferManager,
                                        final String header )
    {
        super( source, queue, target, bufferManager );
        _header = header;
    }

    public void handleEvent( final Object event )
    {
        System.out.println( _header + ": " + event );
        super.handleEvent( event );
    }
}
