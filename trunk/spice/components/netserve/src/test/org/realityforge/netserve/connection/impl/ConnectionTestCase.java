/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.netserve.connection.impl;

import java.io.InputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import junit.framework.TestCase;
import org.realityforge.configkit.ConfigValidator;
import org.realityforge.configkit.ConfigValidatorFactory;
import org.realityforge.configkit.ValidateException;
import org.realityforge.netserve.connection.ConnectionHandlerManager;
import org.realityforge.netserve.connection.ConnectionManager;
import org.xml.sax.ErrorHandler;
import org.apache.avalon.framework.logger.ConsoleLogger;

/**
 * TestCase for {@link ConnectionHandlerManager} and {@link ConnectionManager}.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-04-23 08:15:50 $
 */
public class ConnectionTestCase
    extends TestCase
{
    private static final int PORT = 1977;
    private static final InetAddress HOST = getLocalHost();

    public ConnectionTestCase( final String name )
    {
        super( name );
    }

    public void testSchemaValidation()
        throws Exception
    {
        final InputStream schema =
            getClass().getResourceAsStream( "DefaultConnectionManager-schema.xml" );
        assertNotNull( "Schema file", schema );
        final ConfigValidator validator =
            ConfigValidatorFactory.create( "http://relaxng.org/ns/structure/1.0", schema );
        final InputStream config =
            getClass().getResourceAsStream( "connections-config.xml" );
        try
        {
            validator.validate( config, (ErrorHandler)null );
        }
        catch( ValidateException e )
        {
            fail( "Unexpected validation failure: " + e );
        }
        final ConnectionManager cm = null;
    }

    public void testOverRunCleanShutdown()
        throws Exception
    {
        final ServerSocket serverSocket = new ServerSocket( PORT, 5, HOST );
        try
        {
            final TestConnectionHandlerManager chm = new TestConnectionHandlerManager();
            chm.addHandler( new DelayingConnectionHandler( 200 ) );
            final ConnectionAcceptor acceptor =
                new ConnectionAcceptor( "test-" + getName() + "-",
                                        serverSocket,
                                        chm,
                                        null );
            acceptor.enableLogging( new ConsoleLogger() );
            start( acceptor );
            doClientConnect();
            acceptor.close( 0, false );
        }
        finally
        {
            shutdown( serverSocket );
        }
    }

    private void doClientConnect() throws IOException
    {
        final Socket socket = new Socket( HOST, PORT );
        socket.close();
    }

    public void testOverRunCleanShutdownNoLogging()
        throws Exception
    {
        final ServerSocket serverSocket = new ServerSocket( PORT, 5, HOST );
        try
        {
            final TestConnectionHandlerManager chm = new TestConnectionHandlerManager();
            chm.addHandler( new DelayingConnectionHandler( 200 ) );
            final ConnectionAcceptor acceptor =
                new ConnectionAcceptor( "test-" + getName() + "-",
                                        serverSocket,
                                        chm,
                                        new TestThreadPool() );
            acceptor.enableLogging( new ConsoleLogger( ConsoleLogger.LEVEL_DISABLED ) );
            start( acceptor );
            doClientConnect();
            acceptor.close( 0, false );
        }
        finally
        {
            shutdown( serverSocket );
        }
    }

    public void testOverRunForceShutdown()
        throws Exception
    {
        final ServerSocket serverSocket = new ServerSocket( PORT, 5, HOST );
        try
        {
            final TestConnectionHandlerManager chm = new TestConnectionHandlerManager();
            chm.addHandler( new DelayingConnectionHandler( 200 ) );
            final ConnectionAcceptor acceptor =
                new ConnectionAcceptor( "test-" + getName() + "-",
                                        serverSocket,
                                        chm,
                                        new TestThreadPool() );
            acceptor.enableLogging( new ConsoleLogger() );
            start( acceptor );
            doClientConnect();
            acceptor.close( 5, true );
        }
        finally
        {
            shutdown( serverSocket );
        }
    }

    public void testOverRunForceShutdownNoLogging()
        throws Exception
    {
        final ServerSocket serverSocket = new ServerSocket( PORT, 5, HOST );
        try
        {
            final TestConnectionHandlerManager chm = new TestConnectionHandlerManager();
            chm.addHandler( new DelayingConnectionHandler( 200 ) );
            final ConnectionAcceptor acceptor =
                new ConnectionAcceptor( "test-" + getName() + "-",
                                        serverSocket,
                                        chm,
                                        null );
            acceptor.enableLogging( new ConsoleLogger( ConsoleLogger.LEVEL_DISABLED ) );
            start( acceptor );
            doClientConnect();
            acceptor.close( 5, true );
        }
        finally
        {
            shutdown( serverSocket );
        }
    }

    public void testNoAquire()
        throws Exception
    {
        final ServerSocket serverSocket = getServerSocket();
        try
        {
            final TestConnectionHandlerManager chm = new TestConnectionHandlerManager();
            final ConnectionAcceptor acceptor =
                new ConnectionAcceptor( "test-" + getName() + "-",
                                        serverSocket,
                                        chm,
                                        null );
            acceptor.enableLogging( new ConsoleLogger( ConsoleLogger.LEVEL_DISABLED ) );
            start( acceptor );
            doClientConnect();
            acceptor.close( 5, true );
        }
        finally
        {
            shutdown( serverSocket );
        }
    }

    private ServerSocket getServerSocket() throws IOException
    {
        final ServerSocket serverSocket = new ServerSocket( PORT, 5, HOST );
        serverSocket.setSoTimeout( 50 );
        return serverSocket;
    }

    private void start( final Runnable acceptor )
    {
        final Thread thread = new Thread( acceptor );
        thread.start();
    }

    private void shutdown( final ServerSocket serverSocket )
    {
        try
        {
            serverSocket.close();
        }
        catch( IOException ioe )
        {
        }
    }

    private static InetAddress getLocalHost()
    {
        try
        {
            return InetAddress.getLocalHost();
        }
        catch( UnknownHostException e )
        {
            throw new IllegalStateException( e.toString() );
        }
    }
}
