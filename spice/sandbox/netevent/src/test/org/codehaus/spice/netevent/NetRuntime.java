package org.codehaus.spice.netevent;

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

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-05-17 06:15:21 $
 */
class NetRuntime
{
    private final SelectableChannelEventSource _source;

    /**
     * Manager for all the data chunks used in system.
     */
    private final BufferManager _bufferManager;

    /**
     * EventSource use to register sockets and so forth.
     */
    private final EventSink _sessionSink;

    private final EventPump[] _pumps;

    private final TestEventHandler m_testEventHandler;

    public NetRuntime( final TestEventHandler testEventHandler,
                       final SelectableChannelEventSource source,
                       final BufferManager bufferManager,
                       final EventSink sessionSink,
                       final EventPump[] pumps )
    {
        m_testEventHandler = testEventHandler;
        _source = source;
        _bufferManager = bufferManager;
        _sessionSink = sessionSink;
        _pumps = pumps;
    }

    public SelectableChannelEventSource getSource()
    {
        return _source;
    }

    public BufferManager getBufferManager()
    {
        return _bufferManager;
    }

    public EventSink getSessionSink()
    {
        return _sessionSink;
    }

    public EventPump[] getPumps()
    {
        return _pumps;
    }

    public TestEventHandler getTestEventHandler()
    {
        return m_testEventHandler;
    }

    static NetRuntime createRuntime( final String type,
                                     final long transmitCount,
                                     final long receiveCount,
                                     final boolean closeOnReceive )
        throws IOException
    {
        final DefaultEventQueue queue1 = new DefaultEventQueue( new BoundedFifoBuffer( 15 ) );
        final DefaultEventQueue queue2 = new DefaultEventQueue( new UnboundedFifoBuffer( 15 ) );

        final SelectableChannelEventSource source1 = new SelectableChannelEventSource( queue1 );
        source1.setSelectTimeout( 200 );

        final DefaultBufferManager bufferManager = new DefaultBufferManager();
        final ChannelEventHandler ceh = new ChannelEventHandler( source1,
                                                                 queue1,
                                                                 queue2,
                                                                 bufferManager );
        final EventHandler handler1 = new EchoHandler( /*"CHAN " + type*/null, ceh );

        final TestEventHandler testEventHandler = new TestEventHandler( "TEST " + type,
                                                                        transmitCount,
                                                                        receiveCount,
                                                                        closeOnReceive );
        final EventHandler handler2 = new EchoHandler( "TEST " + type, testEventHandler );

        final EventPump pump1 = newPump( "CHAN " + type, source1, handler1 );
        final EventPump pump2 = newPump( "TEST " + type, queue2, handler2 );

        final EventPump[] pumps = new EventPump[]{pump1, pump2};
        return new NetRuntime( testEventHandler, source1, bufferManager, queue2, pumps );
    }

    private static EventPump newPump( final String name,
                                      final EventSource source,
                                      final EventHandler handler )
    {
        return new EventPump( name, new BlockingEventSource( source, 50 ), handler );
    }
}
