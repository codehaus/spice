/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 * This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/).
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
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision: 1.2 $
 */
public class Main
    implements Runnable
{
    public static void main( final String[] args )
        throws Exception
    {
        boolean debug = true;
        int port = 1977;

        for( int i = 0; i < args.length; i++ )
        {
            String arg = args[ i ];
            if( "-q".equals( arg ) )
            {
                debug = false;
            }
            else
            {
                port = Integer.decode( arg ).intValue();
            }
        }

        final Main main = new Main( debug, port );
        main.start();
        main.accept();
    }

    private final boolean m_debug;
    private final int m_port;
    private RMINamingProviderImpl m_server;
    private ServerSocket m_serverSocket;
    private MarshalledObject m_serverStub;
    private boolean m_running;
    private boolean m_initialized;

    public Main( final boolean debug,
                 final int port )
    {
        m_debug = debug;
        m_port = port;
    }

    public Main()
    {
        this( true, 1977 );
    }

    public void init()
        throws Exception
    {
        if( m_initialized )
        {
            return;
        }

        try
        {
            m_serverSocket = new ServerSocket( m_port );
            debug( "Started server on port " + m_serverSocket.getLocalPort() );
            m_initialized = true;
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
            m_serverStub =
                new MarshalledObject( remote );
        }
        catch( final IOException ioe )
        {
            debug( "Failed exporting object" );
            throw ioe;
        }
    }

    public void dispose()
        throws Exception
    {
        debug( "Shutting down server" );
        m_running = false;
        final ServerSocket serverSocket = m_serverSocket;
        m_serverSocket = null;
        serverSocket.close();
        debug( "Server shutdown" );
    }

    public void stop()
        throws Exception
    {
        debug( "Stopping" );
        m_running = false;
        debug( "Unexporting object" );
        UnicastRemoteObject.unexportObject( m_server, true );
        m_serverStub = null;
        debug( "Server stopped" );
    }

    public void accept()
    {
        m_running = true;
        while( m_running )
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
                if( !m_running )
                {
                    break;
                }
                ioe.printStackTrace();
            }
        }
    }

    public void run()
    {
        accept();
    }

    private void debug( final String message )
    {
        if( m_debug )
        {
            System.out.println( message );
        }
    }
}
