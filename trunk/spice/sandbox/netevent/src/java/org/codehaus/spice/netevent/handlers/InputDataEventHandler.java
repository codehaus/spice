package org.codehaus.spice.netevent.handlers;

import java.nio.ByteBuffer;
import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.events.InputDataPresentEvent;
import org.codehaus.spice.netevent.events.ReadEvent;
import org.codehaus.spice.netevent.transport.ChannelTransport;

/**
 * Stuff data into stream and send resultent event.
 * 
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2004-01-19 06:43:24 $
 */
public class InputDataEventHandler
    extends AbstractDirectedHandler
{
    /**
     * Create handler with specified destination sink.
     * 
     * @param sink the destination
     */
    public InputDataEventHandler( final EventSink sink )
    {
        super( sink );
    }

    /**
     * @see EventHandler#handleEvent(Object)
     */
    public void handleEvent( final Object event )
    {
        final ReadEvent re = (ReadEvent)event;
        final ChannelTransport transport = re.getTransport();
        final ByteBuffer buffer = re.getBuffer();
        transport.getInputStream().addBuffer( buffer );
        getSink().addEvent( new InputDataPresentEvent( transport ) );
    }
}
