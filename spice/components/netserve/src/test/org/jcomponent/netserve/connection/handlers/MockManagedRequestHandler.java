/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.handlers;

import java.net.Socket;
import org.jcomponent.netserve.connection.RequestHandler;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-29 03:00:14 $
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

