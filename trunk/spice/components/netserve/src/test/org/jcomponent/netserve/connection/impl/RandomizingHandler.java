/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.impl;

import java.net.Socket;
import java.util.Random;
import org.jcomponent.netserve.connection.ConnectionHandler;
import org.jcomponent.netserve.connection.ConnectionHandlerManager;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-10-24 08:05:22 $
 */
class RandomizingHandler
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