package org.realityforge.mesnet;

import java.nio.ByteBuffer;

/**
 * A DataChannel represents a channel that application
 * layer uses to write data. Each channel may have different
 * characteristerics (such as reliability, latency, error
 * correction etc).
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-11-10 04:33:18 $
 */
public interface DataChannel
{
    /**
     * Return the associated session.
     *
     * @return the associated session.
     */
    Session getSession();

    /**
     * Return the write buffer.
     *
     * @return the write buffer.
     */
    ByteBuffer getWriteBuffer();

    /**
     * Return the read buffer.
     *
     * @return the read buffer.
     */
    ByteBuffer getReadBuffer();

    /**
     * Notify the underlying subsystem
     * that write buffer has data that
     * is read to be written.
     */
    void queueWrite();
}
