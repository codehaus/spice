/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.protocol;

/**
 * Set of constants used in network protocol.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.4 $ $Date: 2003-11-12 00:43:59 $
 */
public interface ProtocolConstants
{
    /**
     * Byte array containing magic number sent at
     * start of stream.
     */
    byte[] MAGIC = new byte[]
    {
        'm', 'a', 'g', 'i', 'c', '0', '1'
    };

    /**
     * Message ID to indicate connection occured.
     * SessionID (short) and SessionAuth (short) fields follow.
     */
    byte S2C_CONNECTED = 0;

    /**
     * Message indicating that there stream header was invalid.
     */
    byte S2C_BAD_MAGIC = 1;

    /**
     * Message indicating bad SessionID.
     */
    byte S2C_BAD_SESSION = 2;

    /**
     * Message indicating bad SessionAuth.
     */
    byte S2C_BAD_AUTH = 3;

    /**
     * Message indicating regular data being transmitted.
     */
    byte MSG_DATA = 4;

    /**
     * Message indicating client or server is
     * disconnecting.
     */
    byte MSG_DISCONNECT = 5;
}
