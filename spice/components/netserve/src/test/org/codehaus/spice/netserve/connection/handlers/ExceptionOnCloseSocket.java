/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netserve.connection.handlers;

import java.net.Socket;
import java.io.IOException;

/**
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-03-21 23:42:59 $
 */
class ExceptionOnCloseSocket
    extends Socket
{
    static final IOException IO_EXCEPTION = new IOException();

    public boolean isConnected()
    {
        return true;
    }

    public synchronized void close() throws IOException
    {
        throw IO_EXCEPTION;
    }
}
