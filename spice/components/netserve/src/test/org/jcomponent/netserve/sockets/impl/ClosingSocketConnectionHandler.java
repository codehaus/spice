/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import org.jcomponent.netserve.connection.ConnectionHandler;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.6 $ $Date: 2003-10-24 08:05:23 $
 */
class ClosingSocketConnectionHandler
    implements ConnectionHandler
{
    static final String MESSAGE = "Bye!";

    public void handleConnection( final Socket socket )
    {
        try
        {
            final OutputStream outputStream = socket.getOutputStream();
            outputStream.write( MESSAGE.getBytes() );
            outputStream.flush();
            outputStream.close();
        }
        catch( final IOException ioe )
        {
            ioe.printStackTrace();
        }
    }
}
