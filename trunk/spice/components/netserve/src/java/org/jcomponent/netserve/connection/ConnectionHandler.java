/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection;

import java.net.Socket;

/**
 * Implement this interface to process incoming socket connections.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-10-24 08:05:20 $
 */
public interface ConnectionHandler
{
    /**
     * Processes connections as they occur. The handler should not
     * close the <tt>connection</tt>, the caller will do that.
     *
     * @param connection the connection
     */
    void handleConnection( Socket connection );
}
