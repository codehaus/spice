package org.realityforge.mesnet;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-11-10 05:54:18 $
 */
public class SessionImpl
    implements Session
{
    private final long _sessionID;
    private int _status;
    private long _lastReadTime;
    private long _lastWriteTime;
    private long _timeOfLastStatusChange;
    private DataChannel _dataChannel;
    private Map _properties = new HashMap();

    public SessionImpl( final long sessionID,
                        final long sessionAuth,
                        final int maxReadSize,
                        final int maxWriteSize )
    {
        _sessionID = sessionID;
        setProperty( MessageUtils.AUTH_KEY, new Long( sessionAuth ) );
        setStatus( STATUS_NOT_CONNECTED );
        final ByteBuffer readBuffer =
            ByteBuffer.allocateDirect( maxReadSize );
        final ByteBuffer writeBuffer =
            ByteBuffer.allocateDirect( maxWriteSize );
        _dataChannel =
        new DefaultDataChannel( this, readBuffer, writeBuffer );
    }

    public long getSessionID()
    {
        return _sessionID;
    }

    public int getStatus()
    {
        return _status;
    }

    public void setStatus( final int status )
    {
        _status = status;
        _timeOfLastStatusChange = System.currentTimeMillis();
    }

    public long getTimeOfLastStatusChange()
    {
        return _timeOfLastStatusChange;
    }

    public void setProperty( final String key, final Object value )
    {
        _properties.put( key, value );
    }

    public Object getProperty( final String key )
    {
        return _properties.get( key );
    }

    public DataChannel getDataChannel()
    {
        return _dataChannel;
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

    public void queueData()
    {
        throw new IllegalStateException( "Not implemented yet!" );
    }
}
