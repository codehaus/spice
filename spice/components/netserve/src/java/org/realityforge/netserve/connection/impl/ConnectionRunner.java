/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.netserve.connection.impl;

import java.net.Socket;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.realityforge.netserve.connection.ConnectionHandler;

/**
 * 
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-23 03:07:33 $
 */
class ConnectionRunner
    extends AbstractLogEnabled
    implements Runnable
{
    private final String m_name;
    private final Socket m_socket;
    private final ConnectionAcceptor m_acceptor;
    private final ConnectionHandler m_handler;
    private Thread m_thread;

    ConnectionRunner( final String name,
                      final Socket socket,
                      final ConnectionAcceptor acceptor,
                      final ConnectionHandler handler )
    {
        if( null == name )
        {
            throw new NullPointerException( "name" );
        }
        if( null == socket )
        {
            throw new NullPointerException( "socket" );
        }
        if( null == acceptor )
        {
            throw new NullPointerException( "acceptor" );
        }
        if( null == handler )
        {
            throw new NullPointerException( "handler" );
        }
        m_name = name;
        m_socket = socket;
        m_acceptor = acceptor;
        m_handler = handler;
    }

    void close()
        throws Exception
    {
        if( null != m_thread )
        {
            m_thread.interrupt();
            m_thread = null;
            //wait till done??
        }
    }

    public void run()
    {
        try
        {
            m_thread = Thread.currentThread();
            getLogger().debug( "Starting connection on " + m_socket );
            m_handler.handleConnection( m_socket );
            getLogger().debug( "Ending connection on " + m_socket );
        }
        catch( final Exception e )
        {
            getLogger().warn( "Error handling connection", e );
        }
        finally
        {
            m_acceptor.disposeRunner( this );
        }
    }

    /**
     * Return the socket associated with runner.
     *
     * @return the socket associated with runner.
     */
    Socket getSocket()
    {
        return m_socket;
    }

    /**
     * Return the handler associated with runner.
     *
     * @return the handler associated with runner
     */
    ConnectionHandler getHandler()
    {
        return m_handler;
    }

    /**
     * Overide toString to return name of connection.
     *
     * @return the name of connection
     */
    public String toString()
    {
        return m_name;
    }
}
