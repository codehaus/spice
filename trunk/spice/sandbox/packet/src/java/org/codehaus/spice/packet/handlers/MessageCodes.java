package org.codehaus.spice.packet.handlers;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-03-22 01:17:50 $
 */
public interface MessageCodes
{
    /**
     * Message ID to indicate connection occured. SessionID (short) and
     * SessionAuth (short) fields follow. Sent from the Server to the Client.
     */
    byte CONNECT = 0;

    /**
     * Message sent from client to server in response to CONNECT. Indicates that
     * the Server can transmit data.
     */
    byte ESTABLISHED = 1;

    /**
     * Message indicating regular data being transmitted.
     */
    byte DATA = 2;

    /**
     * Message indicating that the initiatiator wants to shut down the session.
     */
    byte DISCONNECT_REQUEST = 3;

    /**
     * Message indicating that listener has successfully shutdown session. This
     * is sent in response to a DISCONNECT_REQUEST sent from initiator. It is
     * only sent after all acks and nacks have been sent though.
     */
    byte DISCONNECT = 4;

    /**
     * Message indicating that received packet. Sequence follows (as short).
     */
    byte ACK = 5;

    /**
     * Message indicating that failed to receiv packet. Sequence follows (as
     * short).
     */
    byte NACK = 6;

    /**
     * Message indicating client or server is disconnecting. Reason follows (as
     * byte) - One of ERROR_* constants.
     */
    byte ERROR = 7;

    /**
     * Message transmitted to keep connection alive. Sequence follows (as short)
     * of last message transmitted.
     */
    byte PING = 8;
}
