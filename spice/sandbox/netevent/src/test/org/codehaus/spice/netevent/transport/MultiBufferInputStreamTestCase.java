package org.codehaus.spice.netevent.transport;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import junit.framework.TestCase;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.event.impl.collections.UnboundedFifoBuffer;
import org.codehaus.spice.netevent.buffers.BufferManager;
import org.jmock.C;
import org.jmock.Mock;

/**
 * @author Peter Donald
 * @version $Revision: 1.4 $ $Date: 2004-05-17 06:21:39 $
 */
public class MultiBufferInputStreamTestCase
    extends TestCase
{
    public void testStreamWithNoBuffers()
        throws Exception
    {
        final Mock mockBufferManager = new Mock( BufferManager.class );
        final BufferManager bm = (BufferManager) mockBufferManager.proxy();

        final Mock mockSink = new Mock( EventSink.class );
        final EventSink sink = (EventSink) mockSink.proxy();

        final ChannelTransport transport = new ChannelTransport( SocketChannel.open(),
                                                                 new UnboundedFifoBuffer( 1 ),
                                                                 bm,
                                                                 sink );

        final MultiBufferInputStream stream = new MultiBufferInputStream( bm, transport, sink );

        assertEquals( "stream.available()", 0, stream.available() );
        assertEquals( "stream.markSupported()", true, stream.markSupported() );
        stream.mark( 2 );
        stream.reset();
        assertEquals( "stream.available()", 0, stream.available() );
        stream.setClosePending();
        assertEquals( "stream.available()", 0, stream.available() );
        assertEquals( "stream.read()", -1, stream.read() );

        mockBufferManager.verify();
        mockSink.verify();
    }

    public void testStreamWithSingleBuffers()
        throws Exception
    {
        final ByteBuffer buffer = ByteBuffer.allocate( 30 );
        buffer.put( (byte) 'a' );
        buffer.put( (byte) 'b' );
        buffer.put( (byte) 'c' );
        buffer.flip();

        final Mock mockBufferManager = new Mock( BufferManager.class );
        mockBufferManager.expect( "releaseBuffer", C.args( C.eq( buffer ) ) );
        final BufferManager bm = (BufferManager) mockBufferManager.proxy();

        final Mock mockSink = new Mock( EventSink.class );
        final EventSink sink = (EventSink) mockSink.proxy();

        final ChannelTransport transport = new ChannelTransport( SocketChannel.open(),
                                                                 new UnboundedFifoBuffer( 1 ),
                                                                 bm,
                                                                 sink );

        final MultiBufferInputStream stream = new MultiBufferInputStream( bm, transport, sink );

        stream.addBuffer( buffer );

        assertEquals( "stream.available()", 3, stream.available() );
        assertEquals( "stream.markSupported()", true, stream.markSupported() );
        stream.mark( 2 );
        assertEquals( "stream.read(1)", 'a', stream.read() );
        assertEquals( "stream.read(2)", 'b', stream.read() );
        assertEquals( "stream.available()", 1, stream.available() );
        stream.reset();
        assertEquals( "stream.available()", 3, stream.available() );
        stream.setClosePending();
        assertEquals( "> stream.available()", 3, stream.available() );
        assertEquals( "> stream.read(1)", 'a', stream.read() );
        assertEquals( "> stream.read(2)", 'b', stream.read() );
        assertEquals( "> stream.read(3)", 'c', stream.read() );
        assertEquals( "stream.read()", -1, stream.read() );

        mockBufferManager.verify();
    }

    public void testStreamWithMultipleBuffers()
        throws Exception
    {
        final ByteBuffer buffer1 = ByteBuffer.allocate( 30 );
        buffer1.put( (byte) 'a' );
        buffer1.put( (byte) 'b' );
        buffer1.put( (byte) 'c' );
        buffer1.flip();

        final ByteBuffer buffer2 = ByteBuffer.allocate( 30 );
        buffer2.put( (byte) 'd' );
        buffer2.put( (byte) 'e' );
        buffer2.put( (byte) 'f' );
        buffer2.flip();

        final Mock mockBufferManager = new Mock( BufferManager.class );
        mockBufferManager.expect( "releaseBuffer", C.args( C.eq( buffer1 ) ) );
        mockBufferManager.expect( "releaseBuffer", C.args( C.eq( buffer2 ) ) );
        final BufferManager bm = (BufferManager) mockBufferManager.proxy();

        final Mock mockSink = new Mock( EventSink.class );
        final EventSink sink = (EventSink) mockSink.proxy();

        final ChannelTransport transport = new ChannelTransport( SocketChannel.open(),
                                                                 new UnboundedFifoBuffer( 1 ),
                                                                 bm,
                                                                 sink );

        final MultiBufferInputStream stream = new MultiBufferInputStream( bm, transport, sink );

        stream.addBuffer( buffer1 );
        stream.addBuffer( buffer2 );

        assertEquals( "stream.available()", 6, stream.available() );
        assertEquals( "stream.markSupported()", true, stream.markSupported() );
        stream.mark( 2 );
        assertEquals( "stream.read(1)", 'a', stream.read() );
        assertEquals( "stream.read(2)", 'b', stream.read() );
        assertEquals( "stream.available()", 4, stream.available() );
        stream.reset();
        assertEquals( "stream.available()", 6, stream.available() );

        stream.mark( 5 );
        assertEquals( "stream.read(1)", 'a', stream.read() );
        assertEquals( "stream.read(2)", 'b', stream.read() );
        assertEquals( "stream.read(3)", 'c', stream.read() );
        assertEquals( "stream.read(4)", 'd', stream.read() );
        assertEquals( "stream.available()", 2, stream.available() );
        stream.reset();
        assertEquals( "stream.available()", 6, stream.available() );

        stream.setClosePending();
        assertEquals( "> stream.available()", 6, stream.available() );
        assertEquals( "> stream.read(1)", 'a', stream.read() );
        assertEquals( "> stream.read(2)", 'b', stream.read() );
        assertEquals( "> stream.read(3)", 'c', stream.read() );
        assertEquals( "> stream.read(3)", 'd', stream.read() );
        assertEquals( "> stream.read(3)", 'e', stream.read() );
        assertEquals( "> stream.read(3)", 'f', stream.read() );
        assertEquals( "stream.read()", -1, stream.read() );

        mockBufferManager.verify();
    }

    public void testOverreadingMark()
        throws Exception
    {
        final ByteBuffer buffer = ByteBuffer.allocate( 30 );
        buffer.put( (byte) 'a' );
        buffer.put( (byte) 'b' );
        buffer.put( (byte) 'c' );
        buffer.flip();

        final Mock mockBufferManager = new Mock( BufferManager.class );
        mockBufferManager.expect( "releaseBuffer", C.args( C.eq( buffer ) ) );
        final BufferManager bm = (BufferManager) mockBufferManager.proxy();

        final Mock mockSink = new Mock( EventSink.class );
        final EventSink sink = (EventSink) mockSink.proxy();

        final ChannelTransport transport = new ChannelTransport( SocketChannel.open(),
                                                                 new UnboundedFifoBuffer( 1 ),
                                                                 bm,
                                                                 sink );

        final MultiBufferInputStream stream = new MultiBufferInputStream( bm, transport, sink );

        stream.addBuffer( buffer );

        assertEquals( "stream.available()", 3, stream.available() );
        assertEquals( "stream.markSupported()", true, stream.markSupported() );
        stream.mark( 1 );
        assertEquals( "stream.read(1)", 'a', stream.read() );
        assertEquals( "stream.read(2)", 'b', stream.read() );
        assertEquals( "stream.available()", 1, stream.available() );
        try
        {
            stream.reset();
            fail( "Expected to get an IOException as mark removed." );
        }
        catch( final IOException ioe )
        {
        }
        assertEquals( "stream.available()", 1, stream.available() );
        stream.setClosePending();
        assertEquals( "> stream.read(3)", 'c', stream.read() );
        assertEquals( "stream.read()", -1, stream.read() );

        mockBufferManager.verify();
    }

    public void testStreamWithSecondBufferAddedDuringRead()
        throws Exception
    {
        final ByteBuffer buffer1 = ByteBuffer.allocate( 30 );
        buffer1.put( (byte) 'a' );
        buffer1.put( (byte) 'b' );
        buffer1.put( (byte) 'c' );
        buffer1.flip();

        final ByteBuffer buffer2 = ByteBuffer.allocate( 30 );
        buffer2.put( (byte) 'd' );
        buffer2.put( (byte) 'e' );
        buffer2.put( (byte) 'f' );
        buffer2.flip();

        final Mock mockBufferManager = new Mock( BufferManager.class );
        mockBufferManager.expect( "releaseBuffer", C.args( C.eq( buffer1 ) ) );
        mockBufferManager.expect( "releaseBuffer", C.args( C.eq( buffer2 ) ) );
        final BufferManager bm = (BufferManager) mockBufferManager.proxy();

        final Mock mockSink = new Mock( EventSink.class );
        final EventSink sink = (EventSink) mockSink.proxy();

        final ChannelTransport transport = new ChannelTransport( SocketChannel.open(),
                                                                 new UnboundedFifoBuffer( 1 ),
                                                                 bm,
                                                                 sink );

        final MultiBufferInputStream stream = new MultiBufferInputStream( bm, transport, sink );

        stream.addBuffer( buffer1 );

        assertEquals( "stream.available()", 3, stream.available() );
        assertEquals( "stream.markSupported()", true, stream.markSupported() );
        stream.mark( 2 );
        assertEquals( "stream.read(1)", 'a', stream.read() );
        assertEquals( "stream.read(2)", 'b', stream.read() );
        assertEquals( "stream.available()", 1, stream.available() );
        stream.reset();
        assertEquals( "stream.available()", 3, stream.available() );
        assertEquals( "> stream.available()", 3, stream.available() );

        final ReaderThread thread = new ReaderThread( stream );
        thread.start();
        while( thread.getCount() < 3 )
        {
            Thread.yield();
        }

        assertEquals( "thread.isAlive()", true, thread.isAlive() );

        stream.addBuffer( buffer2 );
        while( thread.getCount() < 6 )
        {
            Thread.yield();
        }

        assertEquals( "thread.isAlive()", true, thread.isAlive() );

        stream.setClosePending();
        thread.join();

        mockBufferManager.verify();
    }

    static class ReaderThread
        extends Thread
    {
        private final InputStream _input;
        private int _count;

        public ReaderThread( final InputStream input )
        {
            _input = input;
        }

        public int getCount()
        {
            return _count;
        }

        public void run()
        {
            int ch = 0;
            while( -1 != ch )
            {
                try
                {
                    ch = _input.read();
                }
                catch( IOException e )
                {
                    e.printStackTrace();
                }
                _count++;
            }
        }
    }
}
