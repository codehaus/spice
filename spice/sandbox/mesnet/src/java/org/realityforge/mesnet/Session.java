package org.realityforge.mesnet;

import java.util.LinkedList;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.channels.SelectionKey;

/**
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-11-05 04:08:36 $
 */
public class Session
{
    private final LinkedList _readQueue = new LinkedList();
    private final LinkedList _writeQueue = new LinkedList();
    private final int _sessionID;
    private final int _sessionAuth;
    private final ByteBuffer _readBuffer;
    private final ByteBuffer _writeBuffer;
    private SocketChannel _channel;
    private SelectionKey _key;
    private long _lastReadTime;
    private long _lastWriteTime;

    public Session( final int sessionID,
                    final int sessionAuth,
                    final int maxReadSize,
                    final int maxWriteSize )
    {
        _sessionID = sessionID;
        _sessionAuth = sessionAuth;
        _readBuffer = ByteBuffer.allocateDirect( maxReadSize );
        _writeBuffer = ByteBuffer.allocateDirect( maxWriteSize );
    }

    public SocketChannel getChannel()
    {
        return _channel;
    }

    public void setChannel( final SocketChannel channel )
    {
        _channel = channel;
    }

    public SelectionKey getKey()
    {
        return _key;
    }

    public void setKey( final SelectionKey key )
    {
        _key = key;
    }

    public long getLastReadTime()
    {
        return _lastReadTime;
    }

    public void setLastReadTime( final long lastReadTime )
    {
        _lastReadTime = lastReadTime;
    }

    public long getLastWriteTime()
    {
        return _lastWriteTime;
    }

    public void setLastWriteTime( final long lastWriteTime )
    {
        _lastWriteTime = lastWriteTime;
    }

    public int getSessionID()
    {
        return _sessionID;
    }

    public int getSessionAuth()
    {
        return _sessionAuth;
    }

    public ByteBuffer getReadBuffer()
    {
        return _readBuffer;
    }

    public ByteBuffer getWriteBuffer()
    {
        return _writeBuffer;
    }

    public LinkedList getWriteQueue()
    {
        return _writeQueue;
    }

    public LinkedList getReadQueue()
    {
        return _readQueue;
    }
}
