package org.realityforge.packet;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.buffers.BufferManager;
import org.codehaus.spice.netevent.events.CloseChannelRequestEvent;
import org.codehaus.spice.netevent.handlers.AbstractDirectedHandler;
import org.codehaus.spice.timeevent.events.TimeEvent;
import org.codehaus.spice.timeevent.source.SchedulingKey;
import org.codehaus.spice.timeevent.source.TimeEventSource;
import org.codehaus.spice.timeevent.triggers.PeriodicTimeTrigger;
import org.realityforge.packet.events.AbstractSessionEvent;
import org.realityforge.packet.events.DataPacketReadyEvent;
import org.realityforge.packet.events.SessionActiveEvent;
import org.realityforge.packet.events.SessionConnectEvent;
import org.realityforge.packet.events.SessionDisconnectEvent;
import org.realityforge.packet.events.SessionInactiveEvent;
import org.realityforge.packet.session.Session;

/**
 * @author Peter Donald
 * @version $Revision: 1.12 $ $Date: 2004-02-11 00:02:29 $
 */
class TestEventHandler
    extends AbstractDirectedHandler
{
    private static final byte[] DATA = new byte[]{'B', 'E', 'E', 'R'};

    private static final Random RANDOM = new Random();

    private final BufferManager _bufferManager;
    private final String _header;
    private final Set _serverSessions = new HashSet();
    private final Set _clientSessions = new HashSet();
    private final TimeEventSource _timeEventSource;

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
            performLogic( session );
            return;
        }

        final AbstractSessionEvent se = (AbstractSessionEvent)event;
        final Session session = se.getSession();
        SessionData sd = getSessionData( session );

        if( event instanceof DataPacketReadyEvent )
        {
            //final DataPacketReadyEvent e = (DataPacketReadyEvent)event;
            //output( session, "Received " + e.getPacket().getData().limit() );
            sd.incReceivedMessages();
            performLogic( session );
        }
        else if( event instanceof SessionActiveEvent )
        {
            final String text = "Session Active. " + sd;
            sd.incConnectionCount();
            output( session, text );
            performLogic( session );
        }
        else if( event instanceof SessionInactiveEvent )
        {
            final String text =
                "Session Inactive. " + sd +
                " status=" + session.getStatus() +
                " sd.Connects=" + sd.getConnectionCount() +
                " connections=" + session.getConnections();
            output( session, text );
        }
        else if( event instanceof SessionConnectEvent )
        {
            if( null == session.getUserData() )
            {
                sd = new SessionData( session );
                session.setUserData( sd );
            }

            final PeriodicTimeTrigger trigger =
                new PeriodicTimeTrigger( 0, 3000 );
            final SchedulingKey key =
                _timeEventSource.addTrigger( trigger, session );
            sd.setKey( key );

            if( !session.isClient() )
            {
                _serverSessions.add( session );
            }
            else
            {
                _clientSessions.add( session );
            }
            final String text = "Session Started. " + sd;
            output( session, text );
        }
        else if( event instanceof SessionDisconnectEvent )
        {
            final SchedulingKey key = sd.getKey();
            if( null != key )
            {
                sd.setKey( null );
                key.cancel();
            }

            final Set sessions;
            if( !session.isClient() )
            {
                sessions = _serverSessions;
            }
            else
            {
                sessions = _clientSessions;
            }
            sessions.remove( session );

            final String text =
                "Session Completed. " + sd +
                "." + sessions.size() + " sessions remaining. " +
                "Sessions=" + toIDs( sessions );
            output( session, text );
        }
    }

    private Object toIDs( final Set sessions )
    {
        final ArrayList result = new ArrayList();

        final Iterator iterator = sessions.iterator();
        while( iterator.hasNext() )
        {
            final Session session = (Session)iterator.next();
            result.add( new Long( session.getSessionID() ) );
        }

        Collections.sort( result );

        return result;
    }

    private void performLogic( final Session session )
    {
        if( Session.STATUS_ESTABLISHED != session.getStatus() )
        {
            return;
        }
        final SessionData sd = getSessionData( session );
        final int receivedMessages = sd.getReceivedMessages();
        final long dropOn = sd.getSession().getSessionID() % 5;
        if( receivedMessages == 5 )
        {
            session.requestShutdown();
        }
        else if( null != session.getTransport() &&
                 receivedMessages == dropOn &&
                 !sd.isDisconencted() &&
                 session.isClient() )
        {
            output( session, "----------------- CLOSING CONNECTION" );
            sd.setDisconencted();
            final CloseChannelRequestEvent cc =
                new CloseChannelRequestEvent( session.getTransport() );
            getSink().addEvent( cc );
        }
        else
        {
            if( sd.getSentMessages() < 5 )
            {
                transmitData( session );
            }
        }
    }

    private SessionData getSessionData( final Session session )
    {
        return (SessionData)session.getUserData();
    }

    private void transmitData( final Session session )
    {
        final int transmitCount =
            Math.abs( RANDOM.nextInt() % 16 * 1024 );
        //final int transmitCount = 4;
        final ByteBuffer buffer =
            _bufferManager.aquireBuffer( transmitCount );

        for( int i = 0; i < transmitCount; i++ )
        {
            final byte ch = DATA[ i % DATA.length ];
            buffer.put( ch );
        }
        buffer.flip();

        final SessionData sd = getSessionData( session );
        if( sd.getSentMessages() < 5 &&
            session.sendPacket( buffer ) )
        {
            sd.incSentMessages();
            //output( session, "Transmitting " + transmitCount );
        }
        else
        {
            _bufferManager.releaseBuffer( buffer );
        }
    }

    private void output( final Session session, final String text )
    {
        final String message =
            _header + " (" + session.getSessionID() + "): " + text;
        System.out.println( message );
    }
}
