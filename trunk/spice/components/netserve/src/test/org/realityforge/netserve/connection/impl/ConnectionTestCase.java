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
import java.util.Random;
import junit.framework.TestCase;
import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.service.DefaultServiceManager;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.container.ContainerUtil;
import org.realityforge.configkit.ConfigValidator;
import org.realityforge.configkit.ConfigValidatorFactory;
import org.realityforge.configkit.ValidateException;
import org.realityforge.netserve.connection.ConnectionHandlerManager;
import org.realityforge.netserve.connection.ConnectionManager;
import org.realityforge.threadpool.ThreadPool;
import org.xml.sax.ErrorHandler;

/**
 * TestCase for {@link ConnectionHandlerManager} and {@link ConnectionManager}.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.16 $ $Date: 2003-04-23 14:15:43 $
 */
public class ConnectionTestCase
    extends TestCase
{
    private static final int PORT = 1977;
    private static final InetAddress HOST = getLocalHost();

    private static final int TEST_COUNT = 16;

    private static final boolean[] ADD_TP = new boolean[]
    {
        false, true, false, true, false, true, false, true,
        false, true, false, true, false, true, false, true
    };

    private static final int[] SO_TIMEOUT = new int[]
    {
        50, 50, 1000, 1000, 50, 50, 1000, 1000,
        50, 50, 1000, 1000, 50, 50, 1000, 1000
    };

    private static final boolean[] FORCE_SHUTDOWN = new boolean[]
    {
        true, true, true, true, false, false, false, false,
        true, true, true, true, false, false, false, false
    };

    private static final int[] SHUTDOWN_TIMEOUT = new int[]
    {
        100, 100, 100, 100, 100, 100, 100, 100,
        0, 0, 0, 0, 0, 0, 0, 0
    };
    private static final Random RANDOM = new Random();

    /**
     * Delay used to try and trick OS into unbinding socket.
     */
    private static final int PRECREATE_DELAY = 100;

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
    }

    public void testDoNothingDCM()
        throws Exception
    {
        final DefaultConnectionManager cm =
            createCM( true, 50, true, 200 );
        try
        {
            //do nothing
        }
        finally
        {
            cm.dispose();
        }
    }

    public void testDCM()
        throws Exception
    {
        for( int i = 0; i < TEST_COUNT; i++ )
        {
            doCMTests( i );

            //need to sleep a bit so that OS can recover bound socket
            System.gc();
            System.gc();
            try
            {
                Thread.sleep( 200 );
            }
            catch( InterruptedException e )
            {
            }
        }
    }

    private void doCMTests( int i ) throws Exception, ConfigurationException
    {
        final ConnectionManager cm = createCM( ADD_TP[ i ],
                                               SO_TIMEOUT[ i ],
                                               FORCE_SHUTDOWN[ i ],
                                               SHUTDOWN_TIMEOUT[ i ] );
        try
        {
            runCMTests( cm );
        }
        finally
        {
            ContainerUtil.dispose( cm );
        }
    }

    private void runCMTests( final ConnectionManager cm ) throws Exception
    {
        final RandmoizingHandler handler = new RandmoizingHandler();
        cm.connect( "a", getServerSocket(), handler );
        cm.disconnect( "a", false );
        cm.connect( "a", getServerSocket(), handler );
        cm.disconnect( "a", true );
        cm.connect( "a", getServerSocket(), handler );
        doClientConnect();
        doClientConnect();
        doClientConnect();
        doClientConnect();
        cm.disconnect( "a", true );
        cm.connect( "a", getServerSocket(), handler, null );
        doClientConnect();
        doClientConnect();
        doClientConnect();
        doClientConnect();
        cm.disconnect( "a", true );
        cm.connect( "a", getServerSocket(), handler, new TestThreadPool() );
        doClientConnect();
        doClientConnect();
        doClientConnect();
        doClientConnect();
        cm.disconnect( "a", true );
        cm.connect( "a", getServerSocket(), handler );
        runClientConnect();
        runClientConnect();
        runClientConnect();
        runClientConnect();
        cm.disconnect( "a", true );
        try
        {
            cm.disconnect( "a", true );
            fail( "Was able to disconnect non-connections" );
        }
        catch( Exception e )
        {
        }
        cm.connect( "p", getServerSocket(), handler );
        doClientConnect();
        doClientConnect();
        doClientConnect();
        doClientConnect();

        cm.connect( "q", getServerSocket( PORT + RANDOM.nextInt( 40 ) ), handler );
        doClientConnect();
        doClientConnect();
        doClientConnect();
        cm.disconnect( "p", true );
        cm.disconnect( "q", true );
    }

    private DefaultConnectionManager createCM( boolean addThreadPool, final int soTimeoutVal, final boolean forceShutdown, final int shutdownTimeout ) throws ServiceException, ConfigurationException
    {
        final DefaultConnectionManager cm = new DefaultConnectionManager();
        cm.enableLogging( new ConsoleLogger( ConsoleLogger.LEVEL_DISABLED ) );
        //cm.enableLogging( new ConsoleLogger() );

        final DefaultServiceManager manager = new DefaultServiceManager();
        if( addThreadPool )
        {
            manager.put( ThreadPool.ROLE, new TestThreadPool() );
        }
        cm.service( manager );

        final DefaultConfiguration config = new DefaultConfiguration( "root", "" );
        final DefaultConfiguration soTimeoutConfig = new DefaultConfiguration( "soTimeout", "" );
        soTimeoutConfig.setValue( String.valueOf( soTimeoutVal ) );
        config.addChild( soTimeoutConfig );
        final DefaultConfiguration forceShutdownConfig =
            new DefaultConfiguration( "forceShutdown", "" );
        forceShutdownConfig.setValue( String.valueOf( forceShutdown ) );
        config.addChild( forceShutdownConfig );
        final DefaultConfiguration shutdownTimeoutConfig =
            new DefaultConfiguration( "shutdownTimeout", "" );
        shutdownTimeoutConfig.setValue( String.valueOf( shutdownTimeout ) );
        config.addChild( shutdownTimeoutConfig );
        cm.configure( config );
        return cm;
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
            startAndWait( runnable );
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
            startAndWait( acceptor );
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
            final Socket socket = new Socket( HOST, PORT );
            socket.close();
        }
        catch( final Exception e )
        {
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
            startAndWait( acceptor );
            doClientConnect();
            acceptor.close( 0, false );
        }
        finally
        {
            shutdown( serverSocket );
        }
    }

    private void startAndWait( final Runnable runnable )
        throws InterruptedException
    {
        start( runnable );
        Thread.sleep( 50 );
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
            startAndWait( acceptor );
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
            startAndWait( acceptor );
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
            startAndWait( acceptor );
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
            startAndWait( acceptor );
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
            startAndWait( acceptor );
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
        return getServerSocket( PORT );
    }

    private ServerSocket getServerSocket( final int port ) throws IOException
    {
        try
        {
            Thread.sleep( PRECREATE_DELAY );
        }
        catch( InterruptedException e )
        {

        }
        final ServerSocket serverSocket = new ServerSocket( port, 5, HOST );
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
            if( serverSocket.isBound() )
            {
                serverSocket.setSoTimeout( 1 );
            }
            serverSocket.close();
        }
        catch( final Exception e )
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
