/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.5 $ $Date: 2003-10-24 08:07:21 $
 */
class BlockingServerSocket
    extends ServerSocket
{
    static final Socket SOCKET = new Socket();

    private int m_lockCount;
    private int m_acceptCount;

    public BlockingServerSocket()
        throws IOException
    {
    }

    public Socket accept()
        throws IOException
    {
        m_acceptCount++;
        while( true )
        {
            synchronized( this )
            {
                if( m_acceptCount <= m_lockCount )
                {
                    break;
                }
                try
                {
                    wait( 100 );
                }
                catch( InterruptedException e )
                {
                }
                notifyAll();
            }
        }
        return SOCKET;
    }

    synchronized void unlock()
    {
        m_lockCount++;
        notifyAll();
    }
}
