package org.codehaus.spice.netevent.handlers;

import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.events.OutputDataPresentEvent;
import org.codehaus.spice.netevent.transport.ChannelTransport;

/**
 * Stuff data into stream and send resultent event.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-12 04:12:19 $
 */
public class OutputDataEventHandler
    extends AbstractDirectedHandler
{
    /**
     * Create handler with specified destination sink.
     * 
     * @param sink the destination
     */
    public OutputDataEventHandler( final EventSink sink )
    {
        super( sink );
    }

    /**
     * @see EventHandler#handleEvent(Object)
     */
    public void handleEvent( final Object event )
    {
        final OutputDataPresentEvent oe = (OutputDataPresentEvent)event;
        final ChannelTransport transport = oe.getTransport();
        transport.reregister();
    }
}
