package org.realityforge.mesnet;

/**
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.4 $ $Date: 2003-11-24 04:01:03 $
 */
public interface Session
{
    int STATUS_NOT_CONNECTED = 0;
    int STATUS_CONNECTED = 1;
    int STATUS_ESTABLISHED = 2;
    int STATUS_LOST = 3;
    int STATUS_DISCONNECTED = 4;

    long getSessionID();

    int getStatus();

    long getTimeOfLastStatusChange();

    void setStatus( int status );

    Object getProperty( String key );

    void setProperty( String key, Object value );
}
