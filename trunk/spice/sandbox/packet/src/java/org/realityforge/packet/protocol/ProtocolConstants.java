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
 * @version $Revision: 1.2 $ $Date: 2003-11-11 22:35:58 $
 */
public interface ProtocolConstants
{
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
     * Message indicating client needs to login.
     * SessionID (short) and SessionAuth (short) fields follow.
     * If this is new Session the SessionID and SessionAuth
     * are -1.
     */
    byte C2S_LOGIN = 4;

    /**
     * Message indicating regular data being transmitted.
     */
    byte MSG_DATA = 5;

    /**
     * Message indicating client or server is
     * disconnecting.
     */
    byte MSG_DISCONNECT = 6;
}
