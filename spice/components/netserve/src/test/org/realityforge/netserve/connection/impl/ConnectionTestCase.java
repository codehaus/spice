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
import java.util.ArrayList;
import junit.framework.TestCase;
import org.apache.avalon.framework.logger.ConsoleLogger;
import org.realityforge.configkit.ConfigValidator;
import org.realityforge.configkit.ConfigValidatorFactory;
import org.realityforge.configkit.ValidateException;
import org.realityforge.netserve.connection.ConnectionHandlerManager;
import org.realityforge.netserve.connection.ConnectionManager;
import org.xml.sax.ErrorHandler;

/**
 * TestCase for {@link ConnectionHandlerManager} and {@link ConnectionManager}.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.13 $ $Date: 2003-04-23 10:00:20 $
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
        final RandmoizingHandler handlerManager = new RandmoizingHandler();
        final ServerSocket serverSocket = getServerSocket();
        final ConnectionAcceptor acceptor =
            new ConnectionAcceptor( name,
                                    serverSocket,
                                    handlerManager,
                                    null );
        acceptor.enableLogging( new ConsoleLogger() );
        Socket socket = null;
        try
        {
            final Runnable runnable = new Runnable()
            {
                public void run()
                {
                    acceptLoop( serverSocket );
                }
            };
            start( runnable );
            Thread.sleep( 50 );
            socket = new Socket( HOST, PORT );

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
            acceptor.close( 1, true );
            try
            {
                socket.close();
            }
            catch( Exception ioe )
            {
            }
        }
    }

    private void acceptLoop( final ServerSocket serverSocket )
    {
        while( !serverSocket.isClosed() )
        {
            try
            {
                serverSocket.accept();
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

            final ArrayList list = new ArrayList();
            for( int i = 0; i < 1000; i++ )
            {
                final Thread thread = runClientConnect();
                list.add( thread );
            }
            final Thread[] threads = (Thread[])list.toArray( new Thread[ list.size() ] );
            for( int i = 0; i < threads.length; i++ )
            {
                try
                {
                    threads[ i ].join();
                }
                catch( InterruptedException e )
                {
                }
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

    private Thread runClientConnect()
    {
        final Runnable runnable = new Runnable()
        {
            public void run()
            {
                doClientConnect();
            }
        };
        final Thread thread = new Thread( runnable );
        thread.start();
        return thread;
    }

    private void doClientConnect()
    {
        try
        {
            Thread.sleep( 50 );
            final Socket socket = new Socket( HOST, PORT );
            socket.close();
        }
        catch( final Exception e )
        {
            e.printStackTrace();
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

    public void testExceptingHandlerManager()
        throws Exception
    {
        final ServerSocket serverSocket = getServerSocket();
        try
        {
            final ConnectionAcceptor acceptor =
                new ConnectionAcceptor( "test-" + getName() + "-",
                                        serverSocket,
                                        new ExceptingHandlerManager(),
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

    public void testExceptingHandler()
        throws Exception
    {
        final ServerSocket serverSocket = getServerSocket();
        try
        {
            final TestConnectionHandlerManager chm = new TestConnectionHandlerManager();
            chm.addHandler( new ExceptingConnectionHandler() );
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
