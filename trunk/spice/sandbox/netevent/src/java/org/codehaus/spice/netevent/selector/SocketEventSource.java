package org.codehaus.spice.netevent.selector;

import org.codehaus.spice.event.EventSource;
import org.realityforge.sca.selector.impl.DefaultSelectorManager;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-07 06:26:17 $
 */
public class SocketEventSource
    implements EventSource
{
    private final DefaultSelectorManager _selectorManager;
    private final EventSource _source;

    public SocketEventSource( final DefaultSelectorManager selectorManager,
                              final EventSource source )
    {
        _selectorManager = selectorManager;
        _source = source;
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

    void refresh()
    {
        _selectorManager.setRunning( true );
        _selectorManager.refresh();
    }
}
