package org.realityforge.packet.session;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-05 03:25:38 $
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
