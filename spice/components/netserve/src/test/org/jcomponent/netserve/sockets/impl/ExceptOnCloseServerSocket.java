/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets.impl;

import java.net.ServerSocket;
import java.io.IOException;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-09 01:57:33 $
 */
class ExceptOnCloseServerSocket
    extends ServerSocket
{
    static final IOException EXCEPTION = new IOException( "No Close - ha ha!" );

    public ExceptOnCloseServerSocket()
        throws IOException
    {
    }

    public void close() throws IOException
    {
        throw EXCEPTION;
    }
}
