package org.codehaus.spice.netevent.handlers;

import java.nio.ByteBuffer;
import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.events.DataPresentEvent;
import org.codehaus.spice.netevent.events.ReadEvent;
import org.codehaus.spice.netevent.transport.ChannelTransport;

/**
 * Stuff data into stream and send resultent event.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-12 02:32:41 $
 */
public class DataEventHandler
    extends AbstractDirectedHandler
{
    /**
     * Create handler with specified destination sink.
     * 
     * @param sink the destination
     */
    public DataEventHandler( final EventSink sink )
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
        buffer.flip();
        transport.getReceivedData().addBuffer( buffer );
        getSink().addEvent( new DataPresentEvent( transport ) );
    }
}
