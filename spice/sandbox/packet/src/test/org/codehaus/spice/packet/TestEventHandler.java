package org.codehaus.spice.packet;

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
import org.codehaus.spice.packet.events.AbstractSessionEvent;
import org.codehaus.spice.packet.events.DataPacketReadyEvent;
import org.codehaus.spice.packet.events.SessionActiveEvent;
import org.codehaus.spice.packet.events.SessionConnectEvent;
import org.codehaus.spice.packet.events.SessionDisconnectEvent;
import org.codehaus.spice.packet.events.SessionInactiveEvent;
import org.codehaus.spice.packet.session.Session;
import org.codehaus.spice.timeevent.events.TimeEvent;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-03-22 01:17:51 $
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

    public TestEventHandler( final EventSink sink,
                             final BufferManager bufferManager,
                             final String header )
    {
        super( sink );
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
            final TimeEvent e = (TimeEvent) event;
            final Session session = (Session) e.getKey().getUserData();
            performLogic( session );
            return;
        }

        final AbstractSessionEvent se = (AbstractSessionEvent) event;
        final Session session = se.getSession();

        if( event instanceof DataPacketReadyEvent )
        {
            final DataPacketReadyEvent e = (DataPacketReadyEvent) event;
            final Packet packet = e.getPacket();
            output( session, "Received packet " + packet.getSequence() );
            performLogic( session );
        }
        else if( event instanceof SessionActiveEvent )
        {
            final String text = "Session Active.";
            output( session, text );
            performLogic( session );
        }
        else if( event instanceof SessionInactiveEvent )
        {
            final String text = "Session Inactive." + " status=" + session.getStatus() + " isConnecting=" + session.isConnecting() + " connections=" + session.getConnections();
            output( session, text );
        }
        else if( event instanceof SessionConnectEvent )
        {
            if( !session.isClient() )
            {
                _serverSessions.add( session );
            }
            else
            {
                _clientSessions.add( session );
            }
            session.setUserData( new TestSessionData( session ) );

            final String text = "Session Started.";
            output( session, text );
        }
        else if( event instanceof SessionDisconnectEvent )
        {
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
            final String text = "Session Completed. " + sessions.size() + " sessions remaining. " +
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
            final Session session = (Session) iterator.next();
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
        final int lastPacketProcessed = session.getLastPacketProcessed();
        final short lastPacketTransmitted = session.getLastPacketTransmitted();
        final long dropOn = session.getSessionID() % TestServer.MESSAGE_COUNT;
        if( lastPacketProcessed == TestServer.MESSAGE_COUNT &&
            lastPacketTransmitted == TestServer.MESSAGE_COUNT )
        {
            if( !session.isInShutdown() )
            {
                output( session, "----------------- SHUTDOWN SESSION" );
                session.requestShutdown();
            }
        }
        else if( null != session.getTransport() &&
                 lastPacketProcessed == dropOn &&
                 1 == session.getConnections() && session.isClient() )
        {
            output( session, "----------------- CLOSING CONNECTION" );
            final CloseChannelRequestEvent cc = new CloseChannelRequestEvent(
                session.getTransport() );
            getSink().addEvent( cc );
        }
        else
        {
            transmitData( session );
        }
    }

    private void transmitData( final Session session )
    {
        if( session.getLastPacketTransmitted() == TestServer.MESSAGE_COUNT )
        {
            return;
        }
        final int transmitCount = Math.abs(
            RANDOM.nextInt() % 16 * TestServer.TX_SIZE_FACTOR );
        final ByteBuffer buffer = _bufferManager.aquireBuffer( transmitCount );

        for( int i = 0; i < transmitCount; i++ )
        {
            final byte ch = DATA[ i % DATA.length ];
            buffer.put( ch );
        }
        buffer.flip();

        output( session, "----------------- SEND DATA" );
        if( !session.sendPacket( buffer ) )
        {
            final String text = "----------------- FAILED TO SEND DATA. " +
                                " pendingDisconnect=" +
                                session.isPendingDisconnect() +
                                " disconnectRequested=" + session.isDisconnectRequested();
            output( session, text );
            _bufferManager.releaseBuffer( buffer );
        }
    }

    private String describeSession( final Session session )
    {
        return "Session[SessionID=" + session.getSessionID() + ", rx=" + session.getLastPacketProcessed() + ", tx=" + session.getLastPacketTransmitted() + ", rxSet=" + session.getReceiveQueue().getSequences() +
               ", txSet=" + session.getTransmitQueue().getSequences() + "]";
    }

    private void output( final Session session, final String text )
    {
        final String desc = describeSession( session );
        final String message = _header + " (" + session.getSessionID() + "): " + text +
                               " " + desc;
        System.out.println( message );
    }
}
