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
import java.io.OutputStream;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.4 $ $Date: 2003-10-10 03:52:19 $
 */
class ClosingSocketConnectionHandler
    implements SocketConnectionHandler
{
    static final String MESSAGE = "Bye!";

    public void handleConnection( final Socket socket )
        throws IOException
    {
        final OutputStream outputStream = socket.getOutputStream();
        outputStream.write( MESSAGE.getBytes() );
        outputStream.flush();
        outputStream.close();
    }
}
