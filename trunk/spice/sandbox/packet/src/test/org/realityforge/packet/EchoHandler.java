package org.realityforge.packet;

import org.codehaus.spice.event.AbstractEventHandler;
import org.codehaus.spice.event.EventHandler;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-20 06:05:04 $
 */
public class EchoHandler
    extends AbstractEventHandler
{
    private final EventHandler _eventHandler;
    private final String _header;

    public EchoHandler( final String header,
                        final EventHandler eventHandler )
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
