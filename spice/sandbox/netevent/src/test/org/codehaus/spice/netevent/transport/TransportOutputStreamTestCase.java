package org.codehaus.spice.netevent.transport;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import junit.framework.TestCase;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.event.impl.collections.UnboundedFifoBuffer;
import org.codehaus.spice.netevent.buffers.BufferManager;
import org.codehaus.spice.netevent.events.OutputDataPresentEvent;
import org.jmock.C;
import org.jmock.Mock;

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-05-17 06:21:39 $
 */
public class TransportOutputStreamTestCase
    extends TestCase
{
    public void testGetBufferWhenNoneExists()
        throws Exception
    {
        final Mock mockBM = new Mock( BufferManager.class );
        final int count = 3;
        final ByteBuffer expected = ByteBuffer.allocate( count );
        mockBM.expectAndReturn( "aquireBuffer", C.args( C.eq( count ) ), expected );
        final BufferManager bm = (BufferManager) mockBM.proxy();

        final Mock mockSink = new Mock( EventSink.class );
        final EventSink sink = (EventSink) mockSink.proxy();

        final ChannelTransport transport = new ChannelTransport( SocketChannel.open(),
                                                                 new UnboundedFifoBuffer( 1 ),
                                                                 bm,
                                                                 sink );
        final TransportOutputStream stream = new TransportOutputStream( bm,
                                                                        transport,
                                                                        sink,
                                                                        count );
        final ByteBuffer actual = stream.getBuffer();
        assertEquals( "actual", expected, actual );

        mockBM.verify();
        mockSink.verify();
    }

    public void testWriteBelowThreshold()
        throws Exception
    {
        final Mock mockBM = new Mock( BufferManager.class );
        final int count = 3;
        final ByteBuffer expected = ByteBuffer.allocate( count );
        mockBM.expectAndReturn( "aquireBuffer", C.args( C.eq( count ) ), expected );
        final BufferManager bm = (BufferManager) mockBM.proxy();

        final Mock mockSink = new Mock( EventSink.class );
        final EventSink sink = (EventSink) mockSink.proxy();

        final ChannelTransport transport = new ChannelTransport( SocketChannel.open(),
                                                                 new UnboundedFifoBuffer( 1 ),
                                                                 bm,
                                                                 sink );
        final TransportOutputStream stream = new TransportOutputStream( bm,
                                                                        transport,
                                                                        sink,
                                                                        count );
        stream.write( 'a' );
        stream.write( 'b' );

        final ByteBuffer actual = stream.getBuffer();
        assertEquals( "buffer", expected, actual );
        assertEquals( "buffer[0]", 'a', actual.get( 0 ) );
        assertEquals( "buffer[1]", 'b', actual.get( 1 ) );
        assertEquals( "buffer.position", 2, actual.position() );
        assertEquals( "buffer.remaining", 1, actual.remaining() );

        mockBM.verify();
        mockSink.verify();
    }

    public void testWriteBelowThresholdThenFlush()
        throws Exception
    {
        final int count = 3;
        final ByteBuffer expected = ByteBuffer.allocate( count );

        final Mock mockBM = new Mock( BufferManager.class );
        mockBM.expectAndReturn( "aquireBuffer", C.args( C.eq( count ) ), expected );
        final BufferManager bm = (BufferManager) mockBM.proxy();

        final Mock mockSink = new Mock( EventSink.class );
        mockSink.expectAndReturn( "addEvent",
                                  C.args( C.isA( OutputDataPresentEvent.class ) ),
                                  true );
        final EventSink sink = (EventSink) mockSink.proxy();

        final ChannelTransport transport = new ChannelTransport( SocketChannel.open(),
                                                                 new UnboundedFifoBuffer( 1 ),
                                                                 bm,
                                                                 sink );
        final TransportOutputStream stream = new TransportOutputStream( bm,
                                                                        transport,
                                                                        sink,
                                                                        count );
        stream.write( 'a' );
        stream.write( 'b' );
        stream.flush();

        assertEquals( "buffer[0]", 'a', expected.get( 0 ) );
        assertEquals( "buffer[1]", 'b', expected.get( 1 ) );
        assertEquals( "buffer.position", 0, expected.position() );
        assertEquals( "buffer.remaining", 2, expected.remaining() );

        mockBM.verify();
        mockSink.verify();
    }

    public void testWriteAboveThreshold()
        throws Exception
    {
        final int count = 3;
        final ByteBuffer buffer1 = ByteBuffer.allocate( count );
        final ByteBuffer buffer2 = ByteBuffer.allocate( count );

        final Mock mockBM = new Mock( BufferManager.class );
        mockBM.matchAndReturn( "aquireBuffer",
                               C.args( new CallOnceConstraint( C.eq( count ) ) ),
                               buffer2 );
        mockBM.matchAndReturn( "aquireBuffer",
                               C.args( new CallOnceConstraint( C.eq( count ) ) ),
                               buffer1 );
        final BufferManager bm = (BufferManager) mockBM.proxy();

        final Mock mockSink = new Mock( EventSink.class );
        mockSink.expectAndReturn( "addEvent",
                                  C.args( C.isA( OutputDataPresentEvent.class ) ),
                                  true );
        final EventSink sink = (EventSink) mockSink.proxy();

        final ChannelTransport transport = new ChannelTransport( SocketChannel.open(),
                                                                 new UnboundedFifoBuffer( 1 ),
                                                                 bm,
                                                                 sink );
        final TransportOutputStream stream = new TransportOutputStream( bm,
                                                                        transport,
                                                                        sink,
                                                                        count );
        stream.write( 'a' );
        stream.write( 'b' );
        stream.write( 'c' );
        stream.write( 'd' );

        assertEquals( "buffer[0]", 'a', buffer1.get( 0 ) );
        assertEquals( "buffer[1]", 'b', buffer1.get( 1 ) );
        assertEquals( "buffer[2]", 'c', buffer1.get( 2 ) );
        assertEquals( "buffer.position", 0, buffer1.position() );
        assertEquals( "buffer.remaining", 3, buffer1.remaining() );

        assertEquals( "buffer2[0]", 'd', buffer2.get( 0 ) );
        assertEquals( "buffer2.position", 1, buffer2.position() );
        assertEquals( "buffer2.remaining", 2, buffer2.remaining() );

        mockBM.verify();
        mockSink.verify();
    }
}
