package org.realityforge.packet;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Random;
import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.buffers.BufferManager;
import org.codehaus.spice.netevent.handlers.AbstractDirectedHandler;
import org.realityforge.packet.events.DataPacketReadyEvent;
import org.realityforge.packet.events.PacketWriteRequestEvent;
import org.realityforge.packet.events.SessionDisconnectEvent;
import org.realityforge.packet.events.SessionEstablishedEvent;
import org.realityforge.packet.events.SessionEvent;
import org.realityforge.packet.session.Session;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-16 06:48:00 $
 */
class TestEventHandler
    extends AbstractDirectedHandler
{
    private static final byte[] DATA = new byte[]{'B', 'E', 'E', 'R'};

    private static final Random RANDOM = new Random();

    private final BufferManager _bufferManager;
    private final String _header;
    private final int _transmitCount;
    private final int _receiveCount;
    private final boolean _closeOnReceive;

    public TestEventHandler( final EventSink sink,
                             final BufferManager bufferManager,
                             final String header,
                             final int transmitCount,
                             final int receiveCount,
                             final boolean closeOnReceive )
    {
        super( sink );
        _bufferManager = bufferManager;
        _header = header;
        _transmitCount = transmitCount;
        _receiveCount = receiveCount;
        _closeOnReceive = closeOnReceive;
    }

    /**
     * @see EventHandler#handleEvent(Object)
     */
    public void handleEvent( final Object event )
    {
        System.out.println( _header + ": " + event );

        final SessionEvent se = (SessionEvent)event;
        final Session session = se.getSession();

        output( session, String.valueOf( event ) );

        if( event instanceof DataPacketReadyEvent )
        {
            final DataPacketReadyEvent e = (DataPacketReadyEvent)event;
            final List incoming = session.getIncoming();
            incoming.add( e.getPacket() );
            final int available = incoming.size();
            if( available == _receiveCount && _closeOnReceive )
            {
                session.setTransport( null );
            }
        }
        else if( event instanceof SessionDisconnectEvent )
        {
            output( session, "Received " + session.getIncoming().size() );
        }
        else if( event instanceof SessionEstablishedEvent )
        {
            int transmitCount = _transmitCount;
            if( -1 == transmitCount )
            {
                transmitCount = Math.abs( RANDOM.nextInt() % 16 * 1024 );
            }
            final ByteBuffer buffer =
                _bufferManager.aquireBuffer( transmitCount );
            for( int i = 0; i < transmitCount; i++ )
            {
                final byte ch = DATA[ i % DATA.length ];
                buffer.put( ch );
            }
            final short sequence =
                (short)(session.getLastPacketTransmitted() + 1);
            session.setLastPacketTransmitted( sequence );
            final Packet packet = new Packet( sequence, 0, buffer );
            final PacketWriteRequestEvent ev =
                new PacketWriteRequestEvent( session, packet );
            getSink().addEvent( ev );

            output( session, "Transmitted " + transmitCount );
        }
    }

    private void output( final Session session, final String text )
    {
        final String message =
            _header + " (" + session.getSessionID() + "): " + text;
        System.out.println( message );
    }
}
