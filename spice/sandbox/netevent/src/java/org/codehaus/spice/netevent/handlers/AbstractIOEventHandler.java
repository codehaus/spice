package org.codehaus.spice.netevent.handlers;

import java.nio.ByteBuffer;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.events.AbstractTransportEvent;

/**
 * Abstract handler for IO based events.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-08 03:41:14 $
 */
public abstract class AbstractIOEventHandler
    extends AbstractDirectedHandler
{
    /**
     * Create handler with specified destination sink.
     * 
     * @param sink the destination
     */
    protected AbstractIOEventHandler( final EventSink sink )
    {
        super( sink );
    }

    /**
     * Aquire a buffer for specified event.
     * 
     * @param event the event
     * @return the buffer
     */
    protected ByteBuffer aquireBuffer( final AbstractTransportEvent event )
    {
        return ByteBuffer.allocate( 1024 * 8 );
    }

    /**
     * Release a buffer for use again.
     * 
     * @param buffer the buffer
     */
    protected void releaseBuffer( final ByteBuffer buffer )
    {
    }
}
