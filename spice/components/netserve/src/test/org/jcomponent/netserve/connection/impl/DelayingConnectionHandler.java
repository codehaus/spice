/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.impl;

import java.net.Socket;
import org.jcomponent.netserve.connection.ConnectionHandler;
import org.jcomponent.netserve.connection.handlers.AbstractRequestHandler;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.5 $ $Date: 2003-10-26 01:04:18 $
 */
class DelayingConnectionHandler
    extends AbstractRequestHandler
{
    private final long m_delay;

    DelayingConnectionHandler( final long delay )
    {
        m_delay = delay;
    }

    protected void doPerformRequest( Socket socket )
        throws Exception
    {
        final long start = System.currentTimeMillis();
        final long end = start + m_delay;

        long now = start;
        while( now < end )
        {
            try
            {
                Thread.sleep( end - now );
            }
            catch( InterruptedException e )
            {
            }
            now = System.currentTimeMillis();
        }
    }
}
