/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.netserve.connection;

/**
 * This manages creation and destruction of ConnectionHandler objects.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-04-23 01:35:30 $
 */
public interface ConnectionHandlerManager
{
    /**
     * Aquire a ConnectionHandler. The ConnectionHandler
     * can be used until the caller returns the handler
     * using the {@link #releaseHandler} method.
     *
     * @return the ConnectionHandler
     * @throws Exception if unable to aquire the ConnectionHandler
     */
    ConnectionHandler aquireHandler()
        throws Exception;

    /**
     * Release the ConnectionHandler to the manager. The
     * ConnectionHandler may be disposed or may be placed
     * back into a pool etc. The only requirement is that
     * the caller not use the handler after releasing it.
     *
     * @param handler the ConnectionHandler
     */
    void releaseHandler( ConnectionHandler handler );

}

