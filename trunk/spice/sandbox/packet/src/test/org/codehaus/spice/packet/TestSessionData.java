package org.codehaus.spice.packet;

import org.codehaus.spice.packet.session.Session;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-03-22 01:17:51 $
 */
class TestSessionData
{
    private final Session _session;

    public TestSessionData( final Session session )
    {
        _session = session;
    }

    public String toString()
    {
        return describeSession( _session );
    }

    private static String describeSession( final Session session )
    {
        return "Session[SessionID=" + session.getSessionID() + ", rx=" + session.getLastPacketProcessed() + ", tx=" + session.getLastPacketTransmitted() + ", rxSet=" + session.getReceiveQueue().getSequences() +
               ", txSet=" + session.getTransmitQueue().getSequences() + "]";
    }

}
