package org.realityforge.packet.events;

import org.realityforge.packet.session.Session;

/**
 * Event indicating disconnect requested for session.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-02-23 04:06:23 $
 */
public class ConnectRequestEvent
   extends AbstractSessionEvent
{
   /**
    * Create event.
    *
    * @param session the session
    */
   public ConnectRequestEvent( final Session session )
   {
      super( session );
   }
}
