/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.netserve.connection.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import junit.framework.TestCase;
import org.apache.avalon.framework.logger.ConsoleLogger;
import org.realityforge.configkit.ConfigValidator;
import org.realityforge.configkit.ConfigValidatorFactory;
import org.realityforge.configkit.ValidateException;
import org.realityforge.netserve.connection.ConnectionHandlerManager;
import org.realityforge.netserve.connection.ConnectionManager;
import org.realityforge.netserve.connection.ConnectionHandler;
import org.xml.sax.ErrorHandler;

/**
 * TestCase for {@link ConnectionHandlerManager} and {@link ConnectionManager}.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.6 $ $Date: 2003-04-23 09:23:03 $
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

    public void testNullInCtor()
        throws Exception
    {
        final String name = "test-" + getName() + "-";
        final ServerSocket serverSocket = getServerSocket();
        final RandmoizingHandler handlerManager = new RandmoizingHandler();
        final ConnectionAcceptor acceptor =
            new ConnectionAcceptor( name,
                                    serverSocket,
                                    handlerManager,
                                    null );
        final Runnable runnable = new Runnable()
        {
            public void run()
            {
                try
                {
                    serverSocket.accept();
                }
                catch( IOException ioe )
                {
                    ioe.printStackTrace();
                }
            }
        };
        start( runnable );
        final Socket socket = new Socket( HOST, PORT );

        try
        {
            try
            {
                new ConnectionRunner( null,
                                      socket,
                                      handlerManager,
                                      acceptor );
                fail( "Expected a NPE" );
            }
            catch( NullPointerException e )
            {
                assertEquals( e.getMessage(), "name" );
            }
            try
            {
                new ConnectionRunner( name,
                                      socket,
                                      handlerManager,
                                      null );
                fail( "Expected a NPE" );
            }
            catch( NullPointerException e )
            {
                assertEquals( e.getMessage(), "acceptor" );
            }

            try
            {
                new ConnectionRunner( name,
                                      null,
                                      handlerManager,
                                      acceptor );
                fail( "Expected a NPE" );
            }
            catch( NullPointerException e )
            {
                assertEquals( e.getMessage(), "socket" );
            }

            try
            {
                new ConnectionRunner( name,
                                      socket,
                                      null,
                                      acceptor );
                fail( "Expected a NPE" );
            }
            catch( NullPointerException e )
            {
                assertEquals( e.getMessage(), "handler" );
            }

            try
            {
                new ConnectionAcceptor( null,
                                        serverSocket,
                                        handlerManager,
                                        null );
                fail( "Expected a NPE" );
            }
            catch( NullPointerException e )
            {
                assertEquals( e.getMessage(), "name" );
            }

            try
            {
                new ConnectionAcceptor( name,
                                        null,
                                        handlerManager,
                                        null );
                fail( "Expected a NPE" );
            }
            catch( NullPointerException e )
            {
                assertEquals( e.getMessage(), "serverSocket" );
            }
            try
            {
                new ConnectionAcceptor( name,
                                        serverSocket,
                                        null,
                                        null );
                fail( "Expected a NPE" );
            }
            catch( NullPointerException e )
            {
                assertEquals( e.getMessage(), "handlerManager" );
            }

        }
        finally
        {
            shutdown( serverSocket );
            try
            {
                socket.close();
            }
            catch( IOException ioe )
            {
            }
        }
    }

    public void testHammerServer()
        throws Exception
    {
        final ServerSocket serverSocket = getServerSocket();
        try
        {
            final ConnectionAcceptor acceptor =
                new ConnectionAcceptor( "test-" + getName() + "-",
                                        serverSocket,
                                        new RandmoizingHandler(),
                                        null );
            acceptor.enableLogging( new ConsoleLogger( ConsoleLogger.LEVEL_DISABLED ) );
            start( acceptor );

            for( int i = 0; i < 1000; i++ )
            {
                runClientConnect();
            }
            acceptor.close( 0, false );
        }
        finally
        {
            shutdown( serverSocket );
        }
    }

    public void testOverRunCleanShutdown()
        throws Exception
    {
        final ServerSocket serverSocket = getServerSocket();
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

    private void runClientConnect()
    {
        final Runnable runnable = new Runnable()
        {
            public void run()
            {
                doClientConnect();
            }
        };
        start( runnable );
    }

    private void doClientConnect()
    {
        try
        {
            final Socket socket = new Socket( HOST, PORT );
            socket.close();
        }
        catch( IOException ioe )
        {
            ioe.printStackTrace();
        }
    }

    public void testOverRunCleanShutdownNoLogging()
        throws Exception
    {
        final ServerSocket serverSocket = getServerSocket();
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
        final ServerSocket serverSocket = getServerSocket();
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
        final ServerSocket serverSocket = getServerSocket();
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
