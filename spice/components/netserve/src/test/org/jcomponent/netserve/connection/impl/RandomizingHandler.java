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
import org.jcomponent.netserve.connection.handlers.AbstractRequestHandler;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.4 $ $Date: 2003-10-26 01:04:18 $
 */
class RandomizingHandler
    extends AbstractRequestHandler
    implements ConnectionHandlerManager
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

    protected void doPerformRequest( Socket socket )
        throws Exception
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