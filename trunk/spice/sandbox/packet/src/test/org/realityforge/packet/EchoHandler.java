package org.realityforge.packet;

import org.codehaus.spice.event.AbstractEventHandler;
import org.codehaus.spice.event.EventHandler;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2004-02-03 04:08:55 $
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
        final long diff = System.currentTimeMillis() - TestServer.START_TIME;
        final double time = diff;
        if( null != _header )
        {
            System.out.println( _header + "[" + time + "]: " + event );
        }
        _eventHandler.handleEvent( event );
    }
}
