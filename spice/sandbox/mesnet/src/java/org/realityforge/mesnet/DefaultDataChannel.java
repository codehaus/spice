package org.realityforge.mesnet;

import java.nio.ByteBuffer;

/**
 * Simple implementation of a data queue.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-11-10 04:41:33 $
 */
public class DefaultDataChannel
    implements DataChannel
{
    /**
     * The associated session.
     */
    private final Session _session;

    /**
     * The read buffer.
     */
    private final ByteBuffer _readBuffer;

    /**
     * The write buffer.
     */
    private final ByteBuffer _writeBuffer;

    /**
     * Create the DataChannel.
     *
     * @param session the associated session
     * @param readBuffer the read buffer
     * @param writeBuffer the write buffer
     */
    public DefaultDataChannel( final Session session,
                               final ByteBuffer readBuffer,
                               final ByteBuffer writeBuffer )
    {
        _session = session;
        _readBuffer = readBuffer;
        _writeBuffer = writeBuffer;
    }

    /**
     * @see DataChannel#getReadBuffer()
     */
    public ByteBuffer getReadBuffer()
    {
        return _readBuffer;
    }

    /**
     * @see DataChannel#getWriteBuffer()
     */
    public ByteBuffer getWriteBuffer()
    {
        return _writeBuffer;
    }

    /**
     * @see DataChannel#queueWrite()
     */
    public void queueWrite()
    {
    }

    /**
     * @see DataChannel#getSession()
     */
    public Session getSession()
    {
        return _session;
    }
}
