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
 * @version $Revision: 1.4 $ $Date: 2004-01-19 04:28:23 $
 */
public class Protocol
{
    /** Byte array containing magic number sent at start of stream. */
    public static final byte[] MAGIC = new byte[]
    {
        'm', 'a', 'g', 'i', 'c', '0', '1'
    };

    public static final int SIZEOF_BYTE = 1;
    public static final int SIZEOF_SHORT = 2;
    public static final int SIZEOF_LONG = 8;

    /** The sizeof header that client sends to server. */
    public static final int SIZEOF_GREETING = MAGIC.length +
                                              SIZEOF_LONG /* session */ +
                                              SIZEOF_SHORT /* auth */;

    /**
     * The Max-message header is the "connect" message to client. The "data"
     * message is the largest size but it does secondary checking before reading
     * from payload.
     */
    public static final int MAX_MESSAGE_HEADER = SIZEOF_BYTE +
                                                 SIZEOF_LONG +
                                                 SIZEOF_SHORT;

    /**
     * Message ID to indicate connection occured. SessionID (short) and
     * SessionAuth (short) fields follow.
     */
    public static final byte S2C_CONNECT = 0;

    /** Message indicating regular data being transmitted. */
    public static final byte C2S_ESTABLISHED = 1;

    /** Message indicating regular data being transmitted. */
    public static final byte MSG_DATA = 2;

    /**
     * Message indicating client or server is disconnecting. Reason follows (as
     * byte) - One of ERROR_* constants.
     */
    public static final byte MSG_DISCONNECT = 3;

    /** Message indicating that received packet. Sequence follows (as short). */
    public static final byte MSG_ACK = 4;

    /**
     * Message indicating that failed to receiv packet. Sequence follows (as
     * short).
     */
    public static final byte MSG_NACK = 5;

    /** Message indicating No Error. */
    public static final byte ERROR_NONE = 0;

    /** Message indicating that there stream header was invalid. */
    public static final byte ERROR_BAD_MAGIC = 1;

    /** Message indicating bad SessionID. */
    public static final byte ERROR_BAD_SESSION = 2;

    /** Message indicating bad SessionAuth. */
    public static final byte ERROR_BAD_AUTH = 3;

    /** Message indicating Nack for unknown packet. */
    public static final byte ERROR_BAD_NACK = 4;

    /** Message indicating unknown message code. */
    public static final byte ERROR_BAD_MESSAGE = 5;

    /** Message indicating unknown message code. */
    public static final byte ERROR_NO_SEQUENCE = 6;

    /** Message indicating IO Error. */
    public static final byte ERROR_IO_ERROR = 7;
    /**
     * Max difference between successive sequence numbers to test for wrap
     * around.
     */
    public static final short MAX_DIFF = Short.MAX_VALUE / 2;

    /**
     * Return true if seq1 is less than or equal seq2 accounting for
     * wraparound.
     * 
     * @param seq1 a sequence
     * @param seq2 a sequence
     * @return true if seq1 is less than or equal seq2
     */
    public static boolean isLessThanOrEqual( final short seq1,
                                             final short seq2 )
    {
        return seq1 <= seq2 || seq1 - MAX_DIFF > seq2;
    }

    /**
     * Return true if seq1 is next in sequence after seq2 accounting for
     * wrapping.
     * 
     * @param seq1 the first sequence
     * @param seq2 the seconds sequence
     * @return true if seq1 is next in sequence after seq2 accounting for
     *         wrapping.
     */
    public static final boolean isNextInSequence( final short seq1,
                                                  final short seq2 )
    {
        return (short)(seq2 + 1) == seq1;
    }
}
