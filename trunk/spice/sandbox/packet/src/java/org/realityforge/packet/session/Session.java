/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.session;

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2003-12-05 06:57:12 $
 */
public interface Session
{
    int STATUS_NOT_CONNECTED = 0;
    int STATUS_CONNECTED = 1;
    int STATUS_ESTABLISHED = 2;
    int STATUS_LOST = 3;
    int STATUS_DISCONNECTED = 4;

    long getSessionID();

    int getStatus();

    long getTimeOfLastStatusChange();

    void setStatus( int status );

    Object getProperty( String key );

    void setProperty( String key, Object value );
}
