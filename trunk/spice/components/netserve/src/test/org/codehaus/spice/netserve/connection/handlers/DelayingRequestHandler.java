/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netserve.connection.handlers;

import java.net.Socket;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-11-20 00:25:05 $
 */
class DelayingRequestHandler
    extends AbstractRequestHandler
{
    private final long m_delay;
    private final boolean m_wakeupOnInterrupt;

    private boolean m_exited;
    private boolean m_exitDueToInterrupt;

    DelayingRequestHandler( final long delay,
                            final boolean wakeupOnInterrupt )
    {
        m_delay = delay;
        m_wakeupOnInterrupt = wakeupOnInterrupt;
    }

    protected void doPerformRequest( Socket socket )
        throws Exception
    {
        final int code = System.identityHashCode( this );
        final String prefix = "Handler(" + code + ") ";
        System.out.println( prefix + "Started" );
        final long then = System.currentTimeMillis() + m_delay;
        while( System.currentTimeMillis() < then )
        {
            final long rest = then - System.currentTimeMillis();
            System.out.println( prefix + "Sleeping for " + rest );
            try
            {
                Thread.sleep( rest );
            }
            catch( InterruptedException e )
            {
                System.out.println( prefix + "Woken up" );
                if( m_wakeupOnInterrupt )
                {
                    m_exitDueToInterrupt = true;
                    break;
                }
            }
        }
        m_exited = true;
        System.out.println( prefix + "Returning" );
    }

    boolean isExitDueToInterrupt()
    {
        return m_exitDueToInterrupt;
    }

    boolean isExited()
    {
        return m_exited;
    }
}
