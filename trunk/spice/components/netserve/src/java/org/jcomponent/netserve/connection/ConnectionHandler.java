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
 * @version $Revision: 1.4 $ $Date: 2003-10-26 01:04:18 $
 */
public interface ConnectionHandler
{
    /**
     * Processes connections as they occur.
     *
     * @param socket the socket
     */
    void handleConnection( Socket socket );

    /**
     * Shutdown the handler and any requests currently being handled.
     * The timeout specifies the time to wait while shutting
     * down request handlers. A timeout of 0 indicates that
     * should wait indefinetly.
     *
     * @param timeout the timeout
     */
    void shutdown( long timeout );
}
