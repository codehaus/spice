package org.codehaus.spice.netevent;

import org.codehaus.spice.event.AbstractEventHandler;
import org.codehaus.spice.event.EventHandler;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-05-17 05:45:34 $
 */
class EchoHandler
    extends AbstractEventHandler
{
    private final EventHandler _eventHandler;
    private final String _header;

    EchoHandler( final String header, final EventHandler eventHandler )
    {
        _header = header;
        _eventHandler = eventHandler;
    }

    public void handleEvent( final Object event )
    {
        if( null != _header )
        {
            System.out.println( _header + ": " + event );
        }
        _eventHandler.handleEvent( event );
    }
}
