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

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.4 $ $Date: 2003-10-24 08:05:22 $
 */
class DelayingConnectionHandler
    implements ConnectionHandler
{
    private final long m_delay;

    DelayingConnectionHandler( final long delay )
    {
        m_delay = delay;
    }

    public void handleConnection( final Socket connection )
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
