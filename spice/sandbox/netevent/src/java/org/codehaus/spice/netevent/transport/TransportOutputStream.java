package org.codehaus.spice.netevent.transport;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.buffers.BufferManager;
import org.codehaus.spice.netevent.events.CloseChannelRequestEvent;
import org.codehaus.spice.netevent.events.OutputDataPresentEvent;

/**
 * An output stream that will send chunks of data via transport.
 * 
 * @author Peter Donald
 * @version $Revision: 1.8 $ $Date: 2004-02-06 00:49:49 $
 */
public class TransportOutputStream
    extends OutputStream
{
    /**
     * The bufferManager used to get buffers to store data in.
     */
    private final BufferManager _bufferManager;

    /**
     * The underlying transport that this stream is linked to.
     */
    private final ChannelTransport _transport;

    /**
     * The sink ythat write requests are sent to.
     */
    private final EventSink _sink;

    /**
     * The size of buffer requested.
     */
    private final int _bufferSize;

    /**
     * The current byte buffer that is written to. May be null.
     */
    private ByteBuffer _buffer;

    /**
     * The flag indicating whether stream is closed.
     */
    private boolean _closed;

    /**
     * Create output stream.
     * 
     * @param bufferManager the bufferManager
     * @param transport the transport
     * @param sink the sink
     */
    public TransportOutputStream( final BufferManager bufferManager,
                                  final ChannelTransport transport,
                                  final EventSink sink,
                                  final int bufferSize )
    {
        if( null == bufferManager )
        {
            throw new NullPointerException( "bufferManager" );
        }
        if( null == transport )
        {
            throw new NullPointerException( "transport" );
        }
        if( null == sink )
        {
            throw new NullPointerException( "sink" );
        }
        if( bufferSize <= 0 )
        {
            throw new NullPointerException( "bufferSize <= 0" );
        }
        _bufferManager = bufferManager;
        _transport = transport;
        _sink = sink;
        _bufferSize = bufferSize;
    }

    /**
     * @see OutputStream#write(int)
     */
    public synchronized void write( final int data )
        throws IOException
    {
        if( _closed )
        {
            throw new EOFException();
        }
        ByteBuffer buffer = getBuffer();
        if( 0 == buffer.remaining() )
        {
            flush();
            buffer = getBuffer();
        }

        _transport.incTxByteCount();
        buffer.put( (byte)data );
    }

    /**
     * Return the current ByteBuffer or create a new one.
     * 
     * @return the current ByteBuffer or create a new one.
     */
    protected ByteBuffer getBuffer()
    {
        if( null != _buffer )
        {
            return _buffer;
        }
        else
        {
            _buffer = _bufferManager.aquireBuffer( _bufferSize );
            return _buffer;
        }
    }

    /**
     * @see OutputStream#flush()
     */
    public synchronized void flush()
    {
        if( null != _buffer )
        {
            _buffer.flip();
            _transport.getTransmitBuffer().add( _buffer );
            final OutputDataPresentEvent event =
                new OutputDataPresentEvent( _transport, _buffer.limit() );
            _sink.addEvent( event );
            _buffer = null;
        }
    }

    /**
     * @see OutputStream#close()
     */
    public void close()
    {
        if( !_closed )
        {
            flush();
            _closed = true;
            _sink.addEvent( new CloseChannelRequestEvent( _transport ) );
        }
    }

    /**
     * Return true if stream is closed.
     *
     * @return true if stream is closed.
     */
    public boolean isClosed()
    {
        return _closed;
    }
}
