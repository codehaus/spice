package org.realityforge.packet;

import org.realityforge.packet.session.Session;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-02-23 04:06:24 $
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
      return "Session[SessionID=" + session.getSessionID() +
             ", rx=" + session.getLastPacketProcessed() +
             ", tx=" + session.getLastPacketTransmitted() +
             ", rxSet=" + session.getReceiveQueue().getSequences() +
             ", txSet=" + session.getTransmitQueue().getSequences() +
             "]";
   }

}
