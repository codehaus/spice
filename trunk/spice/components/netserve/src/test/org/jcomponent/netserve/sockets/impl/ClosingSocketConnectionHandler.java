/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets.impl;

import org.jcomponent.netserve.sockets.SocketConnectionHandler;
import java.net.Socket;
import java.io.IOException;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-09 07:54:01 $
 */
class ClosingSocketConnectionHandler
    implements SocketConnectionHandler
{
    static final String MESSAGE = "Bye!";

    public void handleConnection( final Socket connection )
        throws IOException
    {
        connection.getOutputStream().write( MESSAGE.getBytes() );
        connection.close();
    }
}
