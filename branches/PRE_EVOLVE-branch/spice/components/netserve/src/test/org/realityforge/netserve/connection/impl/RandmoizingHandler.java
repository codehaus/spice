/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.netserve.connection.impl;

import org.realityforge.netserve.connection.ConnectionHandler;
import org.realityforge.netserve.connection.ConnectionHandlerManager;
import java.net.Socket;
import java.net.ProtocolException;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-23 08:54:10 $
 */
class RandmoizingHandler
    implements ConnectionHandlerManager, ConnectionHandler
{
    private static final Random RANDOM = new Random();

    public ConnectionHandler aquireHandler()
        throws Exception
    {
        return this;
    }

    public void releaseHandler( ConnectionHandler handler )
    {
    }

    public void handleConnection( Socket connection )
        throws IOException, ProtocolException
    {
        try
        {
            Thread.sleep( RANDOM.nextInt( 400 ) );
        }
        catch( InterruptedException e )
        {
        }
    }
}