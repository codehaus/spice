package org.realityforge.packet.events;

import org.realityforge.packet.session.Session;

/**
 * Event indicating connect occured for session.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-02-18 02:33:38 $
 */
public class SessionErrorEvent
   extends AbstractSessionEvent
{
   /**
    * Message indicating No Error.
    */
   public static final byte ERROR_NONE = 0;

   /**
    * Message indicating that there stream header was invalid.
    */
   public static final byte ERROR_BAD_MAGIC = 1;

   /**
    * Message indicating bad SessionID.
    */
   public static final byte ERROR_BAD_SESSION = 2;

   /**
    * Message indicating bad SessionAuth.
    */
   public static final byte ERROR_BAD_AUTH = 3;

   /**
    * Message indicating Nack for unknown packet.
    */
   public static final byte ERROR_BAD_NACK = 4;

   /**
    * Message indicating unknown message code.
    */
   public static final byte ERROR_BAD_MESSAGE = 5;

   /**
    * Message indicating unknown message code.
    */
   public static final byte ERROR_NO_SEQUENCE = 6;

   /**
    * Message indicating IO Error.
    */
   public static final byte ERROR_IO_ERROR = 7;

   /**
    * Message indicating local session has been disconnected.
    */
   public static final byte ERROR_SESSION_DISCONNECTED = 8;

   /**
    * Message indicating local session has been disconnected.
    */
   public static final byte ERROR_SESSION_TIMEOUT = 9;

   /**
    * the code for error.
    */
   private final byte _errorCode;

   /**
    * Create event.
    *
    * @param session the session
    */
   public SessionErrorEvent( final Session session,
                             final byte errorCode )
   {
      super( session );
      _errorCode = errorCode;
   }

   /**
    * Return the code for error.
    *
    * @return the code for error.
    */
   public byte getErrorCode()
   {
      return _errorCode;
   }

   /**
    * @see AbstractSessionEvent#getEventDescription()
    */
   protected String getEventDescription()
   {
      return super.getEventDescription() + " errorCode=" + getErrorCode();
   }
}
