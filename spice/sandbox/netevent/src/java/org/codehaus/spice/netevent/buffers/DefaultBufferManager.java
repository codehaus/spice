package org.codehaus.spice.netevent.buffers;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple BufferManager that caches buffers in map.
 * 
 * @author Peter Donald
 * @version $Revision: 1.4 $ $Date: 2004-01-18 23:51:37 $
 */
public class DefaultBufferManager
    implements BufferManager
{
    /** The buffer cache. */
    private final List _cache = new ArrayList();

    /** the number of new buffers created. */
    private int _newCount;

    /** the number of buffers currently active. */
    private int _activeCount;
    private static final int DEFAULT_SIZE = 1024 * 8;

    /**
     * @see BufferManager#aquireBuffer(int)
     */
    public synchronized ByteBuffer aquireBuffer( final int size )
    {
        _activeCount++;
        final int count = _cache.size();
        if( count > 0 )
        {
            final ByteBuffer buffer = (ByteBuffer)_cache.remove( count - 1 );
            if( buffer.capacity() >= size )
            {
                return buffer;
            }
        }
        _newCount++;
        final int sz = Math.max( DEFAULT_SIZE, size );
        final ByteBuffer buffer = ByteBuffer.allocate( sz );
        buffer.clear();
        return buffer;
    }

    /**
     * @see BufferManager#releaseBuffer(ByteBuffer)
     */
    public synchronized void releaseBuffer( final ByteBuffer buffer )
    {
        _activeCount--;
        buffer.clear();
        _cache.add( buffer );
    }

    /**
     * Return the number of new buffers created.
     * 
     * @return the number of new buffers created.
     */
    public int getNewCount()
    {
        return _newCount;
    }

    /**
     * Return the number of buffers currently active.
     * 
     * @return the number of buffers currently active.
     */
    public int getActiveCount()
    {
        return _activeCount;
    }
}
