package org.realityforge.packet.events;

import org.realityforge.packet.session.Session;

/**
 * Event indicating disconnect requested for session.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-02-13 04:41:51 $
 */
public class PingRequestEvent
   extends AbstractSessionEvent
{
   /**
    * Create event.
    *
    * @param session the session
    */
   public PingRequestEvent( final Session session )
   {
      super( session );
   }
}
