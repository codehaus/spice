/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.handlers;

import org.jcomponent.netserve.connection.ConnectionHandlerManager;
import org.jcomponent.threadpool.ThreadPool;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-24 08:05:21 $
 */
public class ThreadPerRequestHandler
    extends DefaultRequestHandler
{
    /**
     * the thread pool that used to handle requests.
     */
    private final ThreadPool m_threadPool;

    public ThreadPerRequestHandler( final ConnectionHandlerManager handlerManager,
                                    final ThreadPool threadPool )
    {
        super( handlerManager );
        if( null == threadPool )
        {
            throw new NullPointerException( "threadPool" );
        }
        m_threadPool = threadPool;
    }
}
