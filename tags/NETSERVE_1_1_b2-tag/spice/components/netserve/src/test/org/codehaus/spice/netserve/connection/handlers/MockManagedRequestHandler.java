/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netserve.connection.handlers;

import java.net.Socket;
import org.codehaus.spice.netserve.connection.RequestHandler;

/**
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-03-21 23:42:59 $
 */
class MockManagedRequestHandler
    extends ManagedRequestHandler
{
    private final RequestManager m_requestManager;

    public MockManagedRequestHandler( RequestManager requestManager )
    {
        m_requestManager = requestManager;
    }

    protected RequestHandler aquireHandler( Socket socket )
    {
        return m_requestManager.aquireHandler( socket );
    }

    protected void releaseHandler( RequestHandler handler )
    {
        m_requestManager.releaseHandler( handler );
    }
}

