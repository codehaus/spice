package org.codehaus.spice.netevent.buffers;

import java.nio.ByteBuffer;

/**
 * Interface for management of Buffers used in reading and writing data.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-09 00:28:32 $
 */
public interface BufferManager
{
    /**
     * Aquire a buffer that is of specified size or larger.
     * 
     * @param size the size
     * @return the buffer
     */
    ByteBuffer aquireBuffer( int size );

    /**
     * Release specified buffer.
     * 
     * @param buffer the buffer
     */
    void releaseBuffer( ByteBuffer buffer );
}
