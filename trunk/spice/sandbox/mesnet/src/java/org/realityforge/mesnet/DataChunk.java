package org.realityforge.mesnet;

import java.nio.ByteBuffer;

/**
 *
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-11-10 05:54:18 $
 */
public class DataChunk
{
    private final short _sequence;
    private final ByteBuffer _data;

    public DataChunk( final short sequence,
                      final ByteBuffer data )
    {
        _sequence = sequence;
        _data = data;
    }

    public short getSequence()
    {
        return _sequence;
    }

    public ByteBuffer getData()
    {
        return _data;
    }
}
