package org.codehaus.spice.netevent.transport;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.buffers.BufferManager;
import org.codehaus.spice.netevent.events.CloseChannelRequestEvent;

/**
 * This input stream will read data out of multiple ByteBuffer objects. The
 * developer adds ByteBuffers to the stream by calling {@link
 * #addBuffer(ByteBuffer)}. The buffers are recycled using the BufferManager
 * specified in the constructor.
 * 
 * @author Peter Donald
 * @version $Revision: 1.5 $ $Date: 2004-01-16 06:38:40 $
 */
public class MultiBufferInputStream
    extends InputStream
{
    /** The BufferManager to return Buffers to after they have been read. */
    private final BufferManager _bufferManager;

    /** The underlying transport that this stream is linked to. */
    private final ChannelTransport _transport;

    /** The sink ythat write requests are sent to. */
    private final EventSink _sink;

    /** thye list of ByteBuffers. */
    private final LinkedList _buffers = new LinkedList();

    /**
     * The index of current buffer in linked list. -1 indicates that there is no
     * current buffer.
     */
    private int _currentBuffer = -1;

    /**
     * The index into buffer 0 where mark is set. -1 indicates that there is no
     * mark set.
     */
    private int _markBufferIndex = -1;

    /** The amount of data required before mark can be removed. */
    private int _markDataRemaining = -1;

    /** Flag set to true when close occurs after last buffer is cleared. */
    private boolean _closePending;

    /** Flag set to true when stream actually closed. */
    private boolean _closed;

    /**
     * Create Stream that returns buffers to specified manager.
     * 
     * @param bufferManager the BufferManager
     */
    public MultiBufferInputStream( final BufferManager bufferManager,
                                   final ChannelTransport transport,
                                   final EventSink sink )
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
        _bufferManager = bufferManager;
        _transport = transport;
        _sink = sink;
    }

    /**
     * Add a buffer to list that stream is reading from.
     * 
     * @param buffer the buffer
     */
    public synchronized void addBuffer( final ByteBuffer buffer )
    {
        if( _closed )
        {
            return;
        }
        _buffers.addLast( buffer );
        if( -1 == _currentBuffer )
        {
            _currentBuffer = 0;
        }
        notifyAll();
    }

    /**
     * Set the flag that indicates that the underlying source has closed.
     */
    public synchronized void setClosePending()
    {
        _closePending = true;
        notifyAll();
    }

    public boolean isClosed()
    {
        return _closed;
    }

    /**
     * @see InputStream#close()
     */
    public synchronized void close()
    {
        if( !_closed )
        {
            setClosedState();
            _sink.addEvent( new CloseChannelRequestEvent( _transport ) );
            setClosePending();
        }
    }

    /**
     * Set up state for closed stream.
     */
    private void setClosedState()
    {
        _closed = true;
        _buffers.clear();
        _markBufferIndex = -1;
        _currentBuffer = -1;
    }

    /**
     * @see InputStream#read()
     */
    public synchronized int read()
        throws IOException
    {
        if( _markDataRemaining <= 0 && -1 != _markBufferIndex )
        {
            clearMark();
        }
        _markDataRemaining--;

        while( true )
        {
            if( _closed )
            {
                return -1;
            }
            else if( -1 == _currentBuffer && _closePending )
            {
                setClosedState();
                return -1;
            }
            else if( -1 != _currentBuffer )
            {
                final ByteBuffer buffer =
                    (ByteBuffer)_buffers.get( _currentBuffer );
                if( buffer.remaining() > 0 )
                {
                    return buffer.get() & 0xff;
                }
                else if( _currentBuffer + 1 != _buffers.size() )
                {
                    if( -1 == _markBufferIndex )
                    {
                        _bufferManager.releaseBuffer( buffer );
                        _buffers.remove( 0 );
                    }
                    else
                    {
                        _currentBuffer++;
                    }
                    continue;
                }
                else if( _closePending )
                {
                    if( -1 == _markBufferIndex )
                    {
                        _bufferManager.releaseBuffer( buffer );
                        _buffers.remove( 0 );
                    }
                    return -1;
                }
            }

            try
            {
                wait();
            }
            catch( final InterruptedException ie )
            {
                //Ignored
            }
        }
    }

    /**
     * @see InputStream#mark(int)
     */
    public synchronized void mark( final int readlimit )
    {
        if( -1 != _markBufferIndex )
        {
            clearMark();
        }
        _markDataRemaining = readlimit;
        if( _currentBuffer >= 0 )
        {
            final ByteBuffer buffer = (ByteBuffer)_buffers.get( 0 );
            _markBufferIndex = buffer.position();
        }
        else
        {
            _markBufferIndex = 0;
        }
    }

    /**
     * @see InputStream#reset()
     */
    public synchronized void reset()
        throws IOException
    {
        if( -1 != _markBufferIndex )
        {
            for( int i = 1; i <= _currentBuffer; i++ )
            {
                final ByteBuffer buffer = (ByteBuffer)_buffers.get( i );
                buffer.position( 0 );
            }

            if( _currentBuffer >= 0 )
            {
                final ByteBuffer buffer = (ByteBuffer)_buffers.get( 0 );
                buffer.position( _markBufferIndex );
                _currentBuffer = 0;
            }
            _markBufferIndex = -1;
        }
        else
        {
            throw new IOException( "No such mark" );
        }
    }

    /**
     * @see InputStream#markSupported()
     */
    public boolean markSupported()
    {
        return true;
    }

    /**
     * @see InputStream#available()
     */
    public synchronized int available()
    {
        if( _closed || -1 == _currentBuffer )
        {
            return 0;
        }

        int available = 0;
        final int count = _buffers.size();
        for( int i = _currentBuffer; i < count; i++ )
        {
            final ByteBuffer buffer = (ByteBuffer)_buffers.get( i );
            available += buffer.remaining();
        }

        return available;
    }

    /**
     * Clear any marks that are set and reclaim the buffers as necessary.
     */
    protected synchronized void clearMark()
    {
        if( -1 == _markBufferIndex )
        {
            return;
        }
        else
        {
            _markBufferIndex = -1;
            final Iterator iterator = _buffers.iterator();
            while( iterator.hasNext() && _currentBuffer > 0 )
            {
                final ByteBuffer buffer = (ByteBuffer)iterator.next();
                iterator.remove();
                _bufferManager.releaseBuffer( buffer );
                _currentBuffer--;
            }
        }
    }
}
