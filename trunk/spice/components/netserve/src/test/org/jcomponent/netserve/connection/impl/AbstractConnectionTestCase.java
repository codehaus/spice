/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import junit.framework.TestCase;

import org.jcomponent.netserve.connection.ConnectionManager;
import org.realityforge.configkit.ConfigValidator;
import org.realityforge.configkit.ConfigValidatorFactory;
import org.realityforge.configkit.ValidateException;
import org.xml.sax.ErrorHandler;

/**
 * Abstract TestCase for {@link org.jcomponent.netserve.connection.ConnectionHandlerManager} and {@link ConnectionManager}.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @version $Revision: 1.1 $ $Date: 2003-08-31 10:07:43 $
 */
public abstract class AbstractConnectionTestCase
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

    /**
     * Delay used to try and trick OS into unbinding socket.
     */
    private static final int PRECREATE_DELAY =
        Integer.parseInt( System.getProperty( "netserve.precreate.delay", "100" ) );

    private ConnectionMonitor m_monitor;
    private Set m_sockets = new HashSet();

    public AbstractConnectionTestCase( final String name )
    {
        super( name );
    }


    protected void tearDown() throws Exception
    {
        final HashSet copy = new HashSet();
        copy.addAll( m_sockets );
        final Iterator iterator = copy.iterator();
        while( iterator.hasNext() )
        {
            final ServerSocket serverSocket = (ServerSocket)iterator.next();
            shutdown( serverSocket );
        }
    }


    public void testSchemaValidation()
        throws Exception
    {
        final InputStream schema =
            getClass().getResourceAsStream( "ConnectionManager-schema.xml" );
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

    public void testDoNothingConnectionManager()
        throws Exception
    {
        final ConnectionManager cm = createConnectionManager( true, 50, true, 200 );
        try
        {
            //do nothing
        }
        finally
        {
            disposeConnectionManager( cm );
        }
    }

    public void testConnectionManager()
        throws Exception
    {
        for( int i = 0; i < TEST_COUNT; i++ )
        {
            doConnectionManagerTests( i );

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

    private void doConnectionManagerTests( int i ) throws Exception
    {
        final ConnectionManager cm = createConnectionManager( ADD_TP[ i ],
                                               SO_TIMEOUT[ i ],
                                               FORCE_SHUTDOWN[ i ],
                                               SHUTDOWN_TIMEOUT[ i ] );
        try
        {
            runConnectionManagerTests( cm );
        }
        finally
        {
            disposeConnectionManager( cm );
        }
    }

    private void runConnectionManagerTests( final ConnectionManager cm ) throws Exception
    {
        final RandomizingHandler handler = new RandomizingHandler();
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

        cm.connect( "q", getServerSocket( PORT + 1 ), handler );
        doClientConnect();
        doClientConnect();
        doClientConnect();
        cm.disconnect( "p", true );
        cm.disconnect( "q", true );
    }


    public void testNullInCtor()
        throws Exception
    {
        final String name = "test-" + getName() + "-";
        final RandomizingHandler handlerManager = new RandomizingHandler();
        final ServerSocket serverSocket = getServerSocket();
        final ConnectionAcceptor acceptor =
            new ConnectionAcceptor( name,
                                    serverSocket,
                                    handlerManager,
                                    m_monitor,
                                    null );
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
                                      acceptor,
                                      m_monitor );
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
                                      null,
                                      m_monitor );
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
                                      acceptor,
                                      m_monitor );
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
                                      acceptor,
                                      m_monitor );
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
                                        m_monitor,
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
                                        m_monitor,
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
                                        m_monitor,
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
                                        new RandomizingHandler(),
                                        m_monitor,
                                        null );
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
                                        m_monitor,
                                        null );
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
                                        m_monitor,
                                        new TestThreadPool() );
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
                                        m_monitor,
                                        new TestThreadPool() );
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
                                        m_monitor,
                                        null );
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
                                        m_monitor,
                                        null );
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
            final ConnectionMonitor monitor = createConnectionMonitorNoLogging();
            final ConnectionAcceptor acceptor =
                new ConnectionAcceptor( "test-" + getName() + "-",
                                        serverSocket,
                                        chm,
                                        monitor,
                                        null );
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
            final ConnectionMonitor monitor = createConnectionMonitorNoLogging();
            final ConnectionAcceptor acceptor =
                new ConnectionAcceptor( "test-" + getName() + "-",
                                        serverSocket,
                                        chm,
                                        monitor,
                                        null );
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
        try
        {
            final ServerSocket serverSocket = new ServerSocket( port, 5, HOST );
            m_sockets.add( serverSocket );
            serverSocket.setSoTimeout( 50 );
            return serverSocket;
        }
        catch( IOException ioe )
        {
            System.out.println( "port = " + port );
            ioe.printStackTrace( System.out );
            throw ioe;
        }
    }

    private void start( final Runnable acceptor )
    {
        final Thread thread = new Thread( acceptor );
        thread.start();
    }

    protected void shutdown( final ServerSocket serverSocket )
    {
        try
        {
            if( serverSocket.isBound() )
            {
                serverSocket.setSoTimeout( 1 );
            }
            serverSocket.close();
            m_sockets.remove( serverSocket );
        }
        catch( final Exception e )
        {
            e.printStackTrace();
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

    protected void setMonitor( final ConnectionMonitor monitor )
    {
        m_monitor = monitor;
    }

    protected abstract ConnectionManager createConnectionManager( boolean addThreadPool, final int soTimeoutVal, final boolean forceShutdown, final int shutdownTimeout ) 
        throws Exception;

    protected abstract ConnectionMonitor createConnectionMonitor();

    protected abstract ConnectionMonitor createConnectionMonitorNoLogging();

    protected abstract void disposeConnectionManager( final ConnectionManager cm )
        throws Exception;

}