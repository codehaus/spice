package org.codehaus.spice.event.impl;

import org.codehaus.spice.event.EventHandler;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-16 02:03:12 $
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
