package org.realityforge.mesnet;



/**
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-11-10 05:54:18 $
 */
public class MessageUtils
{
    public static final byte MESSAGE_CONNECTED = 0; //session id, auth id
    public static final byte MESSAGE_ESTABLISHED = 1;
    public static final byte MESSAGE_BAD_SESSION = 2;
    public static final byte MESSAGE_BAD_AUTH = 3;
    public static final byte MESSAGE_BAD_MAGIC = 4;

    public static final String AUTH_KEY = "msg.auth";
}
