package org.codehaus.spice.packet.events;

import org.codehaus.spice.packet.session.Session;

/**
 * Event indicating connect occured for session.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-03-22 01:17:50 $
 */
public class SessionErrorEvent
    extends AbstractSessionEvent
{
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
    public SessionErrorEvent( final Session session, final byte errorCode )
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
