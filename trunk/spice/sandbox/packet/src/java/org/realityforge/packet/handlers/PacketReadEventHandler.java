package org.realityforge.packet.handlers;

import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.handlers.AbstractDirectedHandler;
import org.realityforge.packet.Packet;
import org.realityforge.packet.events.AckRequestEvent;
import org.realityforge.packet.events.DataPacketReadyEvent;
import org.realityforge.packet.events.PacketReadEvent;
import org.realityforge.packet.session.PacketQueue;
import org.realityforge.packet.session.Session;

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-01-16 06:48:00 $
 */
public class PacketReadEventHandler
    extends AbstractDirectedHandler
{
    /** The destination of all events destined for next layer. */
    private final EventSink _target;

    /**
     * Create handler.
     * 
     * @param sink the destination
     */
    public PacketReadEventHandler( final EventSink sink,
                                   final EventSink target )
    {
        super( sink );
        if( null == target )
        {
            throw new NullPointerException( "target" );
        }
        _target = target;
    }

    /**
     * @see EventHandler#handleEvent(Object)
     */
    public void handleEvent( final Object event )
    {
        final PacketReadEvent pe = (PacketReadEvent)event;
        final Session session = pe.getSession();
        final Packet packet = pe.getPacket();

        final PacketQueue queue = session.getMessageQueue();
        queue.addPacket( packet );

        Packet candidate = queue.peek();
        short processed = session.getLastPacketProcessed();

        while( null != candidate &&
               Protocol.isNextInSequence( candidate.getSequence(), processed ) )
        {
            candidate = queue.pop();
            final DataPacketReadyEvent response =
                new DataPacketReadyEvent( session, packet );
            _target.addEvent( response );
            processed++;
            session.setLastPacketProcessed( processed );
        }

        getSink().addEvent( new AckRequestEvent( session, processed ) );
        //TODO: Send out some nacks
    }
}
