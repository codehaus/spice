/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.jndikit.rmi.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;
import org.realityforge.jndikit.DefaultNameParser;
import org.realityforge.jndikit.DefaultNamespace;
import org.realityforge.jndikit.memory.MemoryContext;

/**
 * This is a simple test name server and should NOT be used in a
 * production system.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.4 $
 */
public class Main
    implements Runnable
{
    //Config settings
    private final boolean m_debug;
    private final int m_port;

    //Runtime flags
    private boolean m_isRunning;
    private boolean m_isInitialized;

    //Server facet
    private RMINamingProviderImpl m_server;
    private ServerSocket m_serverSocket;
    private MarshalledObject m_serverStub;

    public Main( final boolean debug,
                 final int port )
    {
        m_debug = debug;
        m_port = port;
    }

    public void init()
        throws Exception
    {
        if( m_isInitialized )
        {
            return;
        }

        try
        {
            m_serverSocket = new ServerSocket( m_port );
            debug( "Started server on port " + m_serverSocket.getLocalPort() );
            m_isInitialized = true;
        }
        catch( final IOException ioe )
        {
            debug( "Failed starting server" );
            throw ioe;
        }
    }

    public void start()
        throws Exception
    {
        init();
        export();
    }

    public void export()
        throws Exception
    {
        final DefaultNameParser parser = new DefaultNameParser();
        final DefaultNamespace namespace = new DefaultNamespace( parser );
        final MemoryContext context =
            new MemoryContext( namespace, null, null );
        m_server = new RMINamingProviderImpl( context );

        // Start listener
        try
        {
            // Export server
            debug( "Exporting RMI object." );
            final Remote remote =
                UnicastRemoteObject.exportObject( m_server );
            m_serverStub = new MarshalledObject( remote );
        }
        catch( final IOException ioe )
        {
            debug( "Failed exporting object" );
            throw ioe;
        }
    }

    public void run()
    {
        accept();
    }

    public void dispose()
        throws Exception
    {
        debug( "Shutting down server" );
        m_isRunning = false;
        final ServerSocket serverSocket = m_serverSocket;
        m_serverSocket = null;
        serverSocket.close();
        debug( "Server shutdown" );
    }

    public void stop()
        throws Exception
    {
        debug( "Stopping" );
        m_isRunning = false;
        debug( "Unexporting object" );
        UnicastRemoteObject.unexportObject( m_server, true );
        m_serverStub = null;
        debug( "Server stopped" );
    }

    public void accept()
    {
        m_isRunning = true;
        while( m_isRunning )
        {
            // Accept a connection
            try
            {
                final Socket socket = m_serverSocket.accept();
                debug( "Accepted Connection" );
                final ObjectOutputStream output =
                    new ObjectOutputStream( socket.getOutputStream() );

                output.writeObject( m_serverStub );

                socket.close();
            }
            catch( final IOException ioe )
            {
                if( !m_isRunning )
                {
                    break;
                }
                ioe.printStackTrace();
            }
        }
    }

    private void debug( final String message )
    {
        if( m_debug )
        {
            System.out.println( "RNC: " + message );
        }
    }
}
