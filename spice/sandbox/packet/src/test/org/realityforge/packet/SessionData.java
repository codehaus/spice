package org.realityforge.packet;

import org.codehaus.spice.timeevent.source.SchedulingKey;
import org.realityforge.packet.session.Session;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2004-02-05 04:06:49 $
 */
class SessionData
{
    private final Session _session;
    private SchedulingKey _key;
    private int _sentMessages;
    private int _receivedMessages;
    private boolean _disconencted;

    SessionData( final Session session )
    {
        _session = session;
    }

    public int getSentMessages()
    {
        return _sentMessages;
    }

    public void incSentMessages()
    {
        _sentMessages++;
    }

    public int getReceivedMessages()
    {
        return _receivedMessages;
    }

    public void incReceivedMessages()
    {
        _receivedMessages++;
    }

    public Session getSession()
    {
        return _session;
    }

    public SchedulingKey getKey()
    {
        return _key;
    }

    public void setKey( final SchedulingKey key )
    {
        _key = key;
    }

    public boolean isDisconencted()
    {
        return _disconencted;
    }

    public void setDisconencted()
    {
        _disconencted = true;
    }

    public String toString()
    {
        return "SessionData[SessionID=" + getSession().getSessionID() +
               ", rx=" + getReceivedMessages() +
               ", tx=" + getSentMessages() +
               ", rxSet=" + getSession().getReceiveQueue().getSequences() +
               ", txSet=" + getSession().getTransmitQueue().getSequences() +
               "]";
    }
}
