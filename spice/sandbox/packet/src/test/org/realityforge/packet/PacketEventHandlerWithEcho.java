package org.realityforge.packet;

import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.buffers.BufferManager;
import org.realityforge.packet.handlers.PacketEventHandler;
import org.realityforge.packet.session.SessionManager;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-16 06:48:00 $
 */
public class PacketEventHandlerWithEcho
    extends PacketEventHandler
{
    private final String m_header;

    public PacketEventHandlerWithEcho( final String header,
                                       final EventSink sink,
                                       final EventSink target,
                                       final BufferManager bufferManager,
                                       final SessionManager sessionManager )
    {
        super( sink, target, bufferManager, sessionManager );
        m_header = header;
    }

    public void handleEvent( final Object event )
    {
        System.out.println( m_header + ": " + event );
        super.handleEvent( event );
    }
}
