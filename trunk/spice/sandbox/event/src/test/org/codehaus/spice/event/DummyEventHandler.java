package org.codehaus.spice.event;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-04-19 08:05:36 $
 */
public class DummyEventHandler
    extends AbstractEventHandler
{
    private int _callCount;

    public int getCallCount()
    {
        return _callCount;
    }

    public void handleEvent( final Object event )
    {
        _callCount++;
    }
}
