package org.codehaus.spice.netevent.buffers;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple BufferManager that caches buffers in map.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-09 00:28:32 $
 */
public class DefaultBufferManager
    implements BufferManager
{
    private final List _cache = new ArrayList();

    /**
     * @see BufferManager#aquireBuffer(int)
     */
    public ByteBuffer aquireBuffer( final int size )
    {
        final int count = _cache.size();
        if( count > 0 )
        {
            final ByteBuffer buffer = (ByteBuffer)_cache.remove( count - 1 );
            /*
            System.out.println( "OLD aquireBuffer(" +
                                System.identityHashCode( buffer ) + ")" );
            */
            return buffer;
        }
        else
        {
            final ByteBuffer buffer = ByteBuffer.allocate( 1024 * 8 );
            buffer.clear();
            /*
            System.out.println( "NEW aquireBuffer(" +
                                System.identityHashCode( buffer ) + ")" );
            */
            return buffer;
        }
    }

    /**
     * @see BufferManager#releaseBuffer(ByteBuffer)
     */
    public void releaseBuffer( final ByteBuffer buffer )
    {
        /*
        System.out.println( "releaseBuffer(" +
                            System.identityHashCode( buffer ) + ")" );
        */
        buffer.clear();
        _cache.add( buffer );
    }
}
