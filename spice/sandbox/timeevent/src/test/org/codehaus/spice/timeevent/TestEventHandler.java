package org.codehaus.spice.timeevent;

import org.codehaus.spice.event.AbstractEventHandler;
import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.timeevent.events.TimeEvent;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-22 04:16:49 $
 */
class TestEventHandler
    extends AbstractEventHandler
{
    /**
     * @see EventHandler#handleEvent(Object)
     */
    public void handleEvent( final Object event )
    {
        final TimeEvent e = (TimeEvent)event;
        System.out.println( e );
    }
}
