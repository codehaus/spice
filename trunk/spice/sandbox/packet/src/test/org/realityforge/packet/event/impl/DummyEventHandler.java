package org.realityforge.packet.event.impl;

import org.realityforge.packet.event.EventHandler;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-09 05:27:05 $
 */
class DummyEventHandler
    implements EventHandler
{
    private int _callCount;

    int getCallCount()
    {
        return _callCount;
    }

    public void handleEvent( final Object event )
    {
        _callCount++;
    }

    public void handleEvents( final Object[] events )
    {
        _callCount++;
    }
}
