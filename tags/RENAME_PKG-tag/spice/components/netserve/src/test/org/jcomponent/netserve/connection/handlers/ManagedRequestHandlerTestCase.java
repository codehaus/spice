/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.handlers;

import com.mockobjects.dynamic.C;
import com.mockobjects.dynamic.Mock;
import java.net.Socket;
import junit.framework.TestCase;
import org.jcomponent.netserve.connection.RequestHandler;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-29 03:00:14 $
 */
public class ManagedRequestHandlerTestCase
    extends TestCase
{
    public void testManagedRequestHandler()
        throws Exception
    {
        final Mock mockHandler = new Mock( RequestHandler.class );
        final Socket socket = new Socket();
        mockHandler.expect( "handleConnection", C.args( C.eq( socket ) ) );
        final RequestHandler handler = (RequestHandler)mockHandler.proxy();

        final Mock mockManager = new Mock( RequestManager.class );
        mockManager.expectAndReturn( "aquireHandler",
                                     C.args( C.isA( Socket.class ) ),
                                     handler );
        mockManager.expect( "releaseHandler",
                            C.args( C.eq( handler ) ) );

        final RequestManager manager = (RequestManager)mockManager.proxy();

        final MockManagedRequestHandler managedHandler =
            new MockManagedRequestHandler( manager );
        managedHandler.handleConnection( socket );

        mockManager.verify();
        mockHandler.verify();
    }
}
