/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.impl;

import java.io.IOException;
import java.net.ProtocolException;
import java.net.Socket;

import org.jcomponent.netserve.connection.ConnectionHandler;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-08-31 09:33:55 $
 */
class ExceptingConnectionHandler implements ConnectionHandler
{
    public void handleConnection( Socket connection )
        throws IOException, ProtocolException
    {
        throw new IOException( "Blah!" );
    }
}
