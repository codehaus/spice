/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.handlers;

/**
 * Set of constants used in network protocol.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-13 06:57:38 $
 */
public interface Protocol
{
    /** Byte array containing magic number sent at start of stream. */
    byte[] MAGIC = new byte[]
    {
        'm', 'a', 'g', 'i', 'c', '0', '1'
    };

    int SIZEOF_BYTE = 1;
    int SIZEOF_SHORT = 2;
    int SIZEOF_LONG = 8;

    /** The sizeof header that client sends to server. */
    int SIZEOF_GREETING = MAGIC.length +
                          SIZEOF_LONG /* session */ +
                          SIZEOF_SHORT /* auth */;

    int SIZEOF_MSG_HEADER = SIZEOF_BYTE /* code */ +
                            SIZEOF_SHORT /* message length */;

    /**
     * Message ID to indicate connection occured. SessionID (short) and
     * SessionAuth (short) fields follow.
     */
    byte S2C_CONNECTED = 0;

    /** Message indicating regular data being transmitted. */
    byte C2S_ESTABLISHED = 1;

    /** Message indicating regular data being transmitted. */
    byte MSG_DATA = 2;

    /**
     * Message indicating client or server is disconnecting. Reason follows (as
     * byte) - One of ERROR_* constants.
     */
    byte MSG_DISCONNECT = 3;

    /** Message indicating that received packet. Sequence follows (as short). */
    byte MSG_ACK = 4;

    /**
     * Message indicating that failed to receiv packet. Sequence follows (as
     * short).
     */
    byte MSG_NACK = 5;

    /** Message indicating No Error. */
    byte ERROR_NONE = 0;

    /** Message indicating that there stream header was invalid. */
    byte ERROR_BAD_MAGIC = 1;

    /** Message indicating bad SessionID. */
    byte ERROR_BAD_SESSION = 2;

    /** Message indicating bad SessionAuth. */
    byte ERROR_BAD_AUTH = 3;

    /** Message indicating Nack for unknown packet. */
    byte ERROR_BAD_NACK = 4;

    /** Message indicating unknown message code. */
    byte ERROR_BAD_MESSAGE = 5;

    /** Message indicating unknown message code. */
    byte ERROR_NO_SEQUENCE = 6;

    /** Message indicating IO Error. */
    byte ERROR_IO_ERROR = 7;
}
