package org.realityforge.packet.handlers;

import org.codehaus.spice.event.AbstractEventHandler;
import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.buffers.BufferManager;
import org.realityforge.packet.events.AckEvent;
import org.realityforge.packet.events.NackEvent;
import org.realityforge.packet.events.PacketReadEvent;
import org.realityforge.packet.session.SessionManager;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-16 06:48:00 $
 */
public class PacketEventHandler
    extends AbstractEventHandler
{
    private PacketIOEventHandler _ioHandler;
    private AckEventHandler _ackHandler;
    private NackEventHandler _nackHandler;
    private PacketReadEventHandler _readHandler;

    /**
     * Create handler with specified destination sink.
     * 
     * @param sink the destination
     */
    public PacketEventHandler( final EventSink sink,
                               final EventSink target,
                               final BufferManager bufferManager,
                               final SessionManager sessionManager )
    {
        _ioHandler = new PacketIOEventHandler( sink,
                                               target,
                                               bufferManager,
                                               sessionManager );
        _ackHandler = new AckEventHandler();
        _nackHandler = new NackEventHandler( sink );
        _readHandler = new PacketReadEventHandler( sink, target );
    }

    /**
     * @see EventHandler#handleEvent(Object)
     */
    public void handleEvent( final Object event )
    {
        if( event instanceof AckEvent )
        {
            _ackHandler.handleEvent( event );
        }
        else if( event instanceof NackEvent )
        {
            _nackHandler.handleEvent( event );
        }
        else if( event instanceof PacketReadEvent )
        {
            _readHandler.handleEvent( event );
        }
        else
        {
            _ioHandler.handleEvent( event );
        }
    }
}



