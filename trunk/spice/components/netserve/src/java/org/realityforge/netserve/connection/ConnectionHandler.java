/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.netserve.connection;

import java.io.IOException;
import java.net.ProtocolException;
import java.net.Socket;

/**
 * Implement this interface to process incoming socket connections.
 *
 * @author <a href="mailto:peter at apache.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-23 01:34:15 $
 */
public interface ConnectionHandler
{
    /**
     * Processes connections as they occur. The handler should not
     * close the <tt>connection</tt>, the caller will do that.
     *
     * @param connection the connection
     * @exception IOException if an error reading from socket occurs
     * @exception ProtocolException if an error handling connection occurs
     */
    void handleConnection( Socket connection )
        throws IOException, ProtocolException;
}
