/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.handlers;

import junit.framework.TestCase;
import com.mockobjects.dynamic.Mock;
import com.mockobjects.dynamic.C;
import org.jcomponent.netserve.connection.RequestHandler;
import org.jcomponent.threadpool.ThreadPool;
import org.jcomponent.threadpool.ThreadControl;
import java.net.Socket;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-10-29 03:51:59 $
 */
public class ThreadPerRequestHandlerTestCase
    extends TestCase
{
    public void testNullThreadPoolPassedToCtor()
        throws Exception
    {
        final Mock mockHandler = new Mock( RequestHandler.class );
        final RequestHandler handler = (RequestHandler)mockHandler.proxy();
        try
        {
            new ThreadPerRequestHandler( handler, null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "threadPool", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null ThreadPool passed into Ctor" );
    }

    public void testThreadPoolInvoked()
        throws Exception
    {
        final Mock mockControl = new Mock( ThreadControl.class );
        final ThreadControl control = (ThreadControl)mockControl.proxy();

        final Mock mockHandler = new Mock( RequestHandler.class );
        final RequestHandler handler = (RequestHandler)mockHandler.proxy();

        final Mock mockPool = new Mock( ThreadPool.class );
        mockPool.expectAndReturn( "execute", C.args( C.isA( Runnable.class ) ), control );
        final ThreadPool threadPool = (ThreadPool)mockPool.proxy();

        final ThreadPerRequestHandler requestHandler =
            new ThreadPerRequestHandler( handler, threadPool );
        requestHandler.handleConnection( new Socket() );

        mockHandler.verify();
        mockPool.verify();
        mockControl.verify();
    }

    public void testShutdownWhileThreadStillGoingButInteruptible()
        throws Exception
    {
        final DelayingRequestHandler handler =
            new DelayingRequestHandler( 2000, true );

        final ThreadPerRequestHandler requestHandler =
            new ThreadPerRequestHandler( handler, new MockThreadPool() );
        requestHandler.handleConnection( new Socket() );
        Thread.sleep( 50 );

        requestHandler.shutdown( 50 );
        assertEquals( "isShutdown", true, handler.isShutdown() );
        assertEquals( "isExited", true, handler.isExited() );
        assertEquals( "isExitDueToInterrupt", true, handler.isExitDueToInterrupt() );
    }

    public void testShutdownWhileThreadStillGoingAndNotInteruptible()
        throws Exception
    {
        final DelayingRequestHandler handler =
            new DelayingRequestHandler( 2000, false );

        final ThreadPerRequestHandler requestHandler =
            new ThreadPerRequestHandler( handler, new MockThreadPool() );
        requestHandler.handleConnection( new Socket() );
        Thread.sleep( 50 );

        requestHandler.shutdown( 50 );
        assertEquals( "isShutdown", true, handler.isShutdown() );
        assertEquals( "isExited", false, handler.isExited() );
        assertEquals( "isExitDueToInterrupt", false, handler.isExitDueToInterrupt() );
    }

    public void testShutdownWhileThreadStillGoingAndWaitIndefinetly()
        throws Exception
    {
        final DelayingRequestHandler handler =
            new DelayingRequestHandler( 200, false );

        final ThreadPerRequestHandler requestHandler =
            new ThreadPerRequestHandler( handler, new MockThreadPool() );
        requestHandler.handleConnection( new Socket() );
        Thread.sleep( 50 );

        requestHandler.shutdown( 0 );
        assertEquals( "isShutdown", true, handler.isShutdown() );
        assertEquals( "isExited", true, handler.isExited() );
        assertEquals( "isExitDueToInterrupt", false, handler.isExitDueToInterrupt() );
    }
}
