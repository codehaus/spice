package org.codehaus.spice.netevent.handlers;

import java.nio.ByteBuffer;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.buffers.BufferManager;
import org.codehaus.spice.netevent.events.AbstractTransportEvent;

/**
 * Abstract handler for IO based events.
 * 
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2004-05-17 06:21:38 $
 */
public abstract class AbstractIOEventHandler
    extends AbstractDirectedHandler
{
    /** The associated BufferManager. */
    private final BufferManager _bufferManager;

    /**
     * Create handler with specified destination sink.
     * 
     * @param sink the destination
     * @param bufferManager the bufferManager
     */
    protected AbstractIOEventHandler( final EventSink sink, final BufferManager bufferManager )
    {
        super( sink );
        if( null == bufferManager )
        {
            throw new NullPointerException( "bufferManager" );
        }
        _bufferManager = bufferManager;
    }

    /**
     * Aquire a buffer for specified event.
     * 
     * @param event the event
     * @return the buffer
     */
    protected ByteBuffer aquireBuffer( final AbstractTransportEvent event )
    {
        return _bufferManager.aquireBuffer( 1024 * 8 );
    }

    /**
     * Return the associated BufferManager.
     * 
     * @return the associated BufferManager.
     */
    protected BufferManager getBufferManager()
    {
        return _bufferManager;
    }
}
