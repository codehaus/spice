/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets.impl;

import java.io.IOException;
import java.net.Socket;
import org.jcomponent.netserve.sockets.SocketConnectionHandler;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-10-09 08:33:12 $
 */
class ExceptingSocketConnectionHandler
    implements SocketConnectionHandler
{
    static final IOException EXCEPTION = new IOException( "You can not do that Dave!");

    public void handleConnection( final Socket connection )
        throws IOException
    {
        throw EXCEPTION;
    }
}
