/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.netserve.connection.impl;

import org.realityforge.netserve.connection.ConnectionHandler;
import java.net.Socket;
import java.net.ProtocolException;
import java.io.IOException;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-04-23 11:30:38 $
 */
class ExceptingConnectionHandler implements ConnectionHandler
{
    public void handleConnection( Socket connection )
        throws IOException, ProtocolException
    {
        throw new IOException( "Blah!" );
    }
}
