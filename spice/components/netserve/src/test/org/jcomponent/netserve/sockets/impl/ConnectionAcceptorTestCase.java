/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets.impl;

import junit.framework.TestCase;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.5 $ $Date: 2003-10-09 02:25:19 $
 */
public class ConnectionAcceptorTestCase
    extends TestCase
{
    public void testNullNameInCtor()
        throws Exception
    {
        try
        {
            new ConnectionAcceptor( null,
                                    new MockServerSocket(),
                                    new MockSocketConnectionHandler(),
                                    new NullAcceptorMonitor() );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.message", "name", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to NPE for name" );
    }

    public void testNullServerSocketInCtor()
        throws Exception
    {
        try
        {
            new ConnectionAcceptor( "name",
                                    null,
                                    new MockSocketConnectionHandler(),
                                    new NullAcceptorMonitor() );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.message", "serverSocket", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to NPE for serverSocket" );
    }

    public void testNullHandlerInCtor()
        throws Exception
    {
        try
        {
            new ConnectionAcceptor( "name",
                                    new MockServerSocket(),
                                    null,
                                    new NullAcceptorMonitor() );
        }
        catch( NullPointerException npe )
        {
            assertEquals( "npe.message", "handler", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to NPE for handler" );
    }

    public void testNullMonitorInCtor()
        throws Exception
    {
        try
        {
            new ConnectionAcceptor( "name",
                                    new MockServerSocket(),
                                    new MockSocketConnectionHandler(),
                                    null );
        }
        catch( NullPointerException npe )
        {
            assertEquals( "npe.message", "monitor", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to NPE for monitor" );
    }

    public void testCloseWhenNotRunning()
        throws Exception
    {
        final ConnectionAcceptor acceptor =
            new ConnectionAcceptor( "name",
                                    new MockServerSocket(),
                                    new MockSocketConnectionHandler(),
                                    new NullAcceptorMonitor() );
        assertFalse( "isRunning() pre-close()", acceptor.isRunning() );
        acceptor.close();
        assertFalse( "isRunning() post-close()", acceptor.isRunning() );
    }

    public void testShutdownServerSocketCausesError()
        throws Exception
    {
        final RecordingAcceptorMonitor monitor = new RecordingAcceptorMonitor();
        final ConnectionAcceptor acceptor =
            new ConnectionAcceptor( "name",
                                    new ExceptOnCloseServerSocket(),
                                    new MockSocketConnectionHandler(),
                                    monitor );
        assertEquals( "errorClosingServerSocket pre-shutdownServerSocket()",
                      null,
                      monitor.getErrorClosingServerSocket() );
        acceptor.shutdownServerSocket();
        assertEquals( "errorClosingServerSocket post-shutdownServerSocket()",
                      ExceptOnCloseServerSocket.EXCEPTION,
                      monitor.getErrorClosingServerSocket() );
    }

    public void testShutdownServerSocket()
        throws Exception
    {
        final RecordingAcceptorMonitor monitor = new RecordingAcceptorMonitor();
        final ConnectionAcceptor acceptor =
            new ConnectionAcceptor( "name",
                                    new MockServerSocket(),
                                    new MockSocketConnectionHandler(),
                                    monitor );
        assertEquals( "errorClosingServerSocket pre-shutdownServerSocket()",
                      null,
                      monitor.getErrorClosingServerSocket() );
        acceptor.shutdownServerSocket();
        assertEquals( "errorClosingServerSocket post-shutdownServerSocket()",
                      null,
                      monitor.getErrorClosingServerSocket() );
    }

    public void testExceptionOnAccept()
        throws Exception
    {
        final RecordingAcceptorMonitor monitor = new RecordingAcceptorMonitor();
        final ConnectionAcceptor acceptor =
            new ConnectionAcceptor( "name",
                                    new ExceptOnAcceptServerSocket( false ),
                                    new MockSocketConnectionHandler(),
                                    monitor );
        assertEquals( "getErrorAcceptingConnection pre-shutdownServerSocket()",
                      null,
                      monitor.getErrorAcceptingConnection() );
        acceptor.shutdownServerSocket();

        final Thread thread = startAcceptor( acceptor );
        waitUntilStarted( acceptor );

        //Sleep enough so that exception can be thrown
        try
        {
            Thread.sleep( 30 );
        }
        catch( final InterruptedException e )
        {
        }

        assertEquals( "getErrorAcceptingConnection post-shutdownServerSocket()",
                      ExceptOnAcceptServerSocket.ERROR_EXCEPTION,
                      monitor.getErrorAcceptingConnection() );

        acceptor.close();
        thread.join();
    }


    public void testInteruptOnAccept()
        throws Exception
    {
        final RecordingAcceptorMonitor monitor = new RecordingAcceptorMonitor();
        final ConnectionAcceptor acceptor =
            new ConnectionAcceptor( "name",
                                    new ExceptOnAcceptServerSocket( true ),
                                    new MockSocketConnectionHandler(),
                                    monitor );
        assertEquals( "getErrorAcceptingConnection pre-shutdownServerSocket()",
                      null,
                      monitor.getErrorAcceptingConnection() );
        acceptor.shutdownServerSocket();

        final Thread thread = startAcceptor( acceptor );
        waitUntilStarted( acceptor );

        //Sleep enough so that exception can be thrown
        try
        {
            Thread.sleep( 30 );
        }
        catch( final InterruptedException e )
        {
        }

        assertEquals( "getErrorAcceptingConnection post-shutdownServerSocket()",
                      ExceptOnAcceptServerSocket.INTERRUPTED_EXCEPTION,
                      monitor.getErrorAcceptingConnection() );

        acceptor.close();
        thread.join();
    }


    private Thread startAcceptor( final ConnectionAcceptor acceptor )
    {
        final Thread thread = new Thread( acceptor );
        thread.start();
        return thread;
    }

    private void waitUntilStarted( final ConnectionAcceptor acceptor )
    {
        while( !acceptor.isRunning() )
        {
            try
            {
                Thread.sleep( 30 );
            }
            catch( final InterruptedException e )
            {
            }
        }
    }
}
