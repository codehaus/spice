package org.realityforge.packet;

import java.nio.ByteBuffer;
import java.util.Random;
import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.buffers.BufferManager;
import org.codehaus.spice.netevent.handlers.AbstractDirectedHandler;
import org.codehaus.spice.netevent.events.ChannelClosedEvent;
import org.codehaus.spice.timeevent.events.TimeEvent;
import org.codehaus.spice.timeevent.source.SchedulingKey;
import org.codehaus.spice.timeevent.source.TimeEventSource;
import org.codehaus.spice.timeevent.triggers.PeriodicTimeTrigger;
import org.realityforge.packet.events.DataPacketReadyEvent;
import org.realityforge.packet.events.PacketWriteRequestEvent;
import org.realityforge.packet.events.SessionActiveEvent;
import org.realityforge.packet.events.SessionConnectEvent;
import org.realityforge.packet.events.SessionDisconnectEvent;
import org.realityforge.packet.events.SessionEvent;
import org.realityforge.packet.events.SessionInactiveEvent;
import org.realityforge.packet.session.Session;

/**
 * @author Peter Donald
 * @version $Revision: 1.6 $ $Date: 2004-01-29 05:48:23 $
 */
class TestEventHandler
    extends AbstractDirectedHandler
{
    private static final byte[] DATA = new byte[]{'B', 'E', 'E', 'R'};

    private static final Random RANDOM = new Random();

    private final BufferManager _bufferManager;
    private final String _header;
    private int _clientSessions;
    private int _serverSessions;
    private final TimeEventSource _timeEventSource;
    private SchedulingKey _key;

    public TestEventHandler( final TimeEventSource timeEventSource,
                             final EventSink sink,
                             final BufferManager bufferManager,
                             final String header )
    {
        super( sink );
        _timeEventSource = timeEventSource;
        _bufferManager = bufferManager;
        _header = header;
    }

    /**
     * @see EventHandler#handleEvent(Object)
     */
    public void handleEvent( final Object event )
    {
        if( event instanceof TimeEvent )
        {
            final TimeEvent e = (TimeEvent)event;
            final Session session = (Session)e.getKey().getUserData();
            final SessionData sd = getSessionData( session );
            if( Session.STATUS_ESTABLISHED == session.getStatus() )
            {
                final int receivedMessages = sd.getReceivedMessages();
                if( receivedMessages > 5 )
                {
                    session.setPendingDisconnect();
                }
                else if( null != session.getTransport() &&
                         RANDOM.nextBoolean() )
                {
                    System.out.println( "^^^^^^^^^^^^^" +
                                        "CLOSING PERSISTENT CONNECTION" +
                                        "^^^^^^^^^^^^^" );
                    final ChannelClosedEvent cc =
                        new ChannelClosedEvent( session.getTransport() );
                    getSink().addEvent( cc );
                }
                else
                {
                    transmitData( session );
                }
            }
            return;
        }

        final SessionEvent se = (SessionEvent)event;
        final Session session = se.getSession();
        SessionData sd = getSessionData( session );

        if( event instanceof DataPacketReadyEvent )
        {
            sd.incReceivedMessages();
            transmitData( session );
        }
        else if( event instanceof SessionActiveEvent )
        {
            transmitData( session );
        }
        else if( event instanceof SessionInactiveEvent )
        {
            final String text = "Session Inactive. " + sd;
            output( session, text );
        }
        else if( event instanceof SessionConnectEvent )
        {
            if( null == session.getUserData() )
            {
                sd = new SessionData( session, false );
                session.setUserData( sd );
            }
            if( sd.isPersistent() )
            {
                final PeriodicTimeTrigger trigger =
                    new PeriodicTimeTrigger( 0, 3000 );
                _key = _timeEventSource.addTrigger( trigger, session );
            }

            if( !session.isClient() )
            {
                _serverSessions++;
            }
            else
            {
                _clientSessions++;
            }
            final String text = "Session Active. " + sd;
            output( session, text );
        }
        else if( event instanceof SessionDisconnectEvent )
        {
            if( sd.isPersistent() && null != _key )
            {
                _key.cancel();
                _key = null;
            }

            final int sessions;
            if( !session.isClient() )
            {
                _serverSessions--;
                sessions = _serverSessions;
            }
            else
            {
                _clientSessions--;
                sessions = _clientSessions;
            }
            final String text =
                "Session Completed. " + sd +
                "." + sessions + " sessions remaining.";
            output( session, text );
        }
    }

    private SessionData getSessionData( final Session session )
    {
        return (SessionData)session.getUserData();
    }

    private void transmitData( final Session session )
    {
        if( session.isPendingDisconnect() )
        {
            return;
        }
        final int transmitCount =
            Math.abs( RANDOM.nextInt() % 16 * 1024 );
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
        final SessionData sd = getSessionData( session );
        sd.incSentMessages();
        output( session, "Transmitting " + transmitCount );
    }

    private void output( final Session session, final String text )
    {
        final String message =
            _header + " (" + session.getSessionID() + "): " + text;
        System.out.println( message );
    }
}
