package org.realityforge.packet;

import org.realityforge.packet.session.Session;

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-02-03 04:29:53 $
 */
class SessionData
{
    private final Session _session;
    private final boolean _isPersistent;
    private int _sentMessages;
    private int _receivedMessages;

    SessionData( final Session session,
                 final boolean persistent )
    {
        _session = session;
        _isPersistent = persistent;
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

    public int getUnAckedMessages()
    {
        return getSession().getTransmitQueue().size();
    }

    public Session getSession()
    {
        return _session;
    }

    public boolean isPersistent()
    {
        return _isPersistent;
    }

    public String toString()
    {
        return "SessionData[SessionID=" + getSession().getSessionID() +
               ", rx=" + getReceivedMessages() +
               ", tx=" + getSentMessages() +
               ", un-acked=" + getUnAckedMessages() +
               "]";
    }
}
