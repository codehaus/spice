package org.codehaus.spice.netevent.selector;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import org.codehaus.spice.event.EventSource;
import org.codehaus.spice.event.impl.DefaultEventQueue;
import org.realityforge.sca.selector.impl.DefaultSelectorManager;

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-01-08 03:41:14 $
 */
public class SocketEventSource
    implements EventSource
{
    private final DefaultSelectorManager _selectorManager;
    private final EventSource _source;
    private SocketSelectorEventHandler _handler;

    public SocketEventSource( final DefaultSelectorManager selectorManager,
                              final DefaultEventQueue queue )
    {
        _handler = new SocketSelectorEventHandler( queue );
        _selectorManager = selectorManager;
        _source = queue;
    }

    public Object getEvent()
    {
        refresh();
        return _source.getEvent();
    }

    public Object[] getEvents( final int count )
    {
        refresh();
        return _source.getEvents( count );
    }

    public Object getSourceLock()
    {
        return _source.getSourceLock();
    }

    public SelectionKey registerChannel( final SelectableChannel channel,
                                         final int ops,
                                         final Object userData )
        throws IOException
    {
        return _selectorManager.registerChannel( channel,
                                                 ops,
                                                 _handler,
                                                 userData );
    }

    void refresh()
    {
        _selectorManager.setRunning( true );
        _selectorManager.refresh();
    }
}
