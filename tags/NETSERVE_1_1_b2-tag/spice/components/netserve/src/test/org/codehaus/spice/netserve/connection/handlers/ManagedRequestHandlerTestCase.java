/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netserve.connection.handlers;

import com.mockobjects.dynamic.C;
import com.mockobjects.dynamic.Mock;
import java.net.Socket;
import junit.framework.TestCase;
import org.codehaus.spice.netserve.connection.RequestHandler;

/**
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-03-21 23:42:59 $
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
