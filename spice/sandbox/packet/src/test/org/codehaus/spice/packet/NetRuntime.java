package org.codehaus.spice.packet;

import java.io.IOException;
import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.event.EventSource;
import org.codehaus.spice.event.impl.BlockingEventSource;
import org.codehaus.spice.event.impl.DefaultEventQueue;
import org.codehaus.spice.event.impl.EventPump;
import org.codehaus.spice.event.impl.collections.BoundedFifoBuffer;
import org.codehaus.spice.event.impl.collections.UnboundedFifoBuffer;
import org.codehaus.spice.netevent.buffers.BufferManager;
import org.codehaus.spice.netevent.buffers.DefaultBufferManager;
import org.codehaus.spice.netevent.handlers.ChannelEventHandler;
import org.codehaus.spice.netevent.source.SelectableChannelEventSource;
import org.codehaus.spice.packet.handlers.PacketIOEventHandler;
import org.codehaus.spice.packet.session.DefaultSessionManager;
import org.codehaus.spice.packet.session.SessionManager;
import org.codehaus.spice.timeevent.source.TimeEventSource;

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-05-17 06:25:47 $
 */
class NetRuntime
{
    private final SelectableChannelEventSource _source;

    /**
     * Manager for all the data chunks used in system.
     */
    private final BufferManager _bufferManager;

    /**
     * Session manager associated with runtime.
     */
    private final SessionManager _sessionManager;

    /**
     * EventSource use to register sockets and so forth.
     */
    private final EventSink _sessionSink;

    private final EventPump[] _pumps;

    NetRuntime( final SelectableChannelEventSource source,
                final BufferManager bufferManager,
                final SessionManager sessionManager,
                final EventSink sessionSink,
                final EventPump[] pumps )
    {
        _source = source;
        _bufferManager = bufferManager;
        _sessionManager = sessionManager;
        _sessionSink = sessionSink;
        _pumps = pumps;
    }

    SelectableChannelEventSource getSource()
    {
        return _source;
    }

    BufferManager getBufferManager()
    {
        return _bufferManager;
    }

    SessionManager getSessionManager()
    {
        return _sessionManager;
    }

    EventSink getSessionSink()
    {
        return _sessionSink;
    }

    EventPump[] getPumps()
    {
        return _pumps;
    }

    static NetRuntime createRuntime( final String type )
        throws IOException
    {
        final DefaultEventQueue queue1 = new DefaultEventQueue( new BoundedFifoBuffer( 15 ) );
        final DefaultEventQueue queue2 = new DefaultEventQueue( new UnboundedFifoBuffer( 15 ) );
        final DefaultEventQueue queue3 = new DefaultEventQueue( new UnboundedFifoBuffer( 15 ) );
        final DefaultEventQueue queue4 = new DefaultEventQueue( new UnboundedFifoBuffer( 15 ) );
        final DefaultEventQueue queue5 = new DefaultEventQueue( new UnboundedFifoBuffer( 15 ) );

        final SelectableChannelEventSource source1 = new SelectableChannelEventSource( queue1 );
        source1.setSelectTimeout( 0 );
        final TimeEventSource source2 = new TimeEventSource( queue4 );
        final TimeEventSource source3 = new TimeEventSource( queue5 );

        final DefaultBufferManager bufferManager = new DefaultBufferManager();
        final EventHandler handler1 = new EchoHandler( "CHAN " + type, new ChannelEventHandler(
            source1, queue1, queue2, bufferManager ) );

        final DefaultSessionManager sessionManager = new DefaultSessionManager();
        final EventHandler handler2 = new EchoHandler( "PACK " + type, new PacketIOEventHandler(
            source2, source1, queue2, queue3, bufferManager, sessionManager ) );

        final EventHandler handler3 = new EchoHandler( "TEST " + type, new TestEventHandler(
            queue1, bufferManager, "TEST " + type ) );

        final EventPump pump1 = newPump( "CHAN " + type, source1, handler1 );
        final EventPump pump2 = newPump( "PACK " + type, queue2, handler2 );
        final EventPump pump3 = newPump( "TEST " + type, queue3, handler3 );
        final EventPump pump4 = newPump( "PACK TIME " + type, source2, handler2 );
        final EventPump pump5 = newPump( "TEST TIME " + type, source3, handler3 );

        final EventPump[] pumps = new EventPump[]{pump1, pump2, pump3, pump4, pump5};
        return new NetRuntime( source1, bufferManager, sessionManager, queue2, pumps );
    }

    private static EventPump newPump( final String name,
                                      final EventSource source,
                                      final EventHandler handler )
    {
        return new EventPump( name, new BlockingEventSource( source ), handler );
    }
}
