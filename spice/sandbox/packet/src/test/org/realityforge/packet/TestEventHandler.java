package org.realityforge.packet;

import java.nio.ByteBuffer;
import java.util.Random;
import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.buffers.BufferManager;
import org.codehaus.spice.netevent.handlers.AbstractDirectedHandler;
import org.realityforge.packet.events.DataPacketReadyEvent;
import org.realityforge.packet.events.PacketWriteRequestEvent;
import org.realityforge.packet.events.SessionActiveEvent;
import org.realityforge.packet.events.SessionDisconnectEvent;
import org.realityforge.packet.events.SessionDisconnectRequestEvent;
import org.realityforge.packet.events.SessionEvent;
import org.realityforge.packet.events.SessionInactiveEvent;
import org.realityforge.packet.session.Session;

/**
 * @author Peter Donald
 * @version $Revision: 1.4 $ $Date: 2004-01-22 05:52:16 $
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
    private int _sessions;

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
        final SessionEvent se = (SessionEvent)event;
        final Session session = se.getSession();

        if( event instanceof DataPacketReadyEvent )
        {
            final DataPacketReadyEvent e = (DataPacketReadyEvent)event;
            final Packet packet = e.getPacket();
            final int available = packet.getData().limit();

            if( _closeOnReceive && available >= _receiveCount )
            {
                final boolean persistent =
                    ((Boolean)session.getUserData()).booleanValue();
                if( persistent )
                {
                    System.out.println( "^^^^^^^^^^^^^" +
                                        "CLOSING PERSISTENT CONNECTION" +
                                        "^^^^^^^^^^^^^" );
                    session.getTransport().getOutputStream().close();
                }
                else
                {
                    final SessionDisconnectRequestEvent response =
                        new SessionDisconnectRequestEvent( e.getSession() );
                    getSink().addEvent( response );
                }
            }
        }
        else if( event instanceof SessionInactiveEvent )
        {
            final int queueSize = session.getMessageQueue().size();
            final String text =
                "Session Inactive. Unacked=" + queueSize;
            output( session, text );
        }
        else if( event instanceof SessionDisconnectEvent )
        {
            _sessions--;
            final String text =
                "Session Completed. " + _sessions + " sessions remaining.";
            output( session, text );
        }
        else if( event instanceof SessionActiveEvent )
        {
            _sessions++;
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
            buffer.flip();
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
