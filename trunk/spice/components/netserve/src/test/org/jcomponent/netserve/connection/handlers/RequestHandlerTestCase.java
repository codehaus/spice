/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.handlers;

import junit.framework.TestCase;
import java.net.Socket;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-10-29 03:26:56 $
 */
public class RequestHandlerTestCase
    extends TestCase
{
    public void testEndConnectionWithError()
        throws Exception
    {
        final MockRequestHandler handler = new MockRequestHandler();
        final ExceptionOnCloseSocket socket = new ExceptionOnCloseSocket();
        handler.endConnection( socket );
        assertEquals( "getErrorClosingConnectionSocket",
                      socket,
                      handler.getErrorClosingConnectionSocket() );
        assertEquals( "getErrorClosingConnectionThrowable",
                      ExceptionOnCloseSocket.IO_EXCEPTION,
                      handler.getErrorClosingConnectionThrowable() );
    }

    public void testCreateRunnable()
        throws Exception
    {
        final MockRequestHandler handler = new MockRequestHandler();
        final Socket socket = new Socket();
        final Runnable runnable = handler.createRunnable( socket );
        runnable.run();
        assertEquals( "getPerformRequestSocket",
                      socket,
                      handler.getPerformRequestSocket() );
    }

    public void testPerformRequestWithError()
        throws Exception
    {
        final ExceptingRequestHandler handler = new ExceptingRequestHandler();
        final Socket socket = new Socket();
        handler.performRequest( socket );
        assertEquals( "getPerformRequestSocket",
                      socket,
                      handler.getPerformRequestSocket() );
        assertEquals( "getErrorHandlingConnectionSocket",
                      socket,
                      handler.getErrorHandlingConnectionSocket() );
        assertEquals( "getErrorClosingConnectionThrowable",
                      ExceptingRequestHandler.EXCEPTION,
                      handler.getErrorHandlingConnectionThrowable() );
    }

    public void testShutdownWhileThreadStillGoingButInteruptible()
        throws Exception
    {
        final DelayingRequestHandler handler = new DelayingRequestHandler( 2000, true );
        final Socket socket = new Socket();
        final Runnable runnable = new Runnable()
        {
            public void run()
            {
                handler.handleConnection( socket );
            }
        };
        final Thread thread = new Thread( runnable );
        thread.start();
        Thread.sleep( 50 );

        handler.shutdown( 50 );
        assertEquals( "isShutdown", true, handler.isShutdown() );
        assertEquals( "isExited", true, handler.isExited() );
        assertEquals( "isExitDueToInterrupt", true, handler.isExitDueToInterrupt() );
    }

    public void testShutdownWhileThreadStillGoingAndNotInteruptible()
        throws Exception
    {
        final DelayingRequestHandler handler =
            new DelayingRequestHandler( 2000, false );
        final Socket socket = new Socket();
        final Runnable runnable = new Runnable()
        {
            public void run()
            {
                handler.handleConnection( socket );
            }
        };
        final Thread thread = new Thread( runnable );
        thread.start();
        Thread.sleep( 50 );

        handler.shutdown( 50 );
        assertEquals( "isShutdown", true, handler.isShutdown() );
        assertEquals( "isExited", false, handler.isExited() );
        assertEquals( "isExitDueToInterrupt", false, handler.isExitDueToInterrupt() );
    }

    public void testShutdownWhileThreadStillGoingAndWaitIndefinetly()
        throws Exception
    {
        final DelayingRequestHandler handler =
            new DelayingRequestHandler( 200, false );
        final Socket socket = new Socket();
        final Runnable runnable = new Runnable()
        {
            public void run()
            {
                handler.handleConnection( socket );
            }
        };
        final Thread thread = new Thread( runnable );
        thread.start();
        Thread.sleep( 50 );

        handler.shutdown( 0 );
        assertEquals( "isShutdown", true, handler.isShutdown() );
        assertEquals( "isExited", true, handler.isExited() );
        assertEquals( "isExitDueToInterrupt", false, handler.isExitDueToInterrupt() );
    }
}
