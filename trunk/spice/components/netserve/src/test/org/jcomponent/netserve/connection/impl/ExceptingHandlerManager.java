/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.impl;

import org.jcomponent.netserve.connection.ConnectionHandlerManager;
import org.jcomponent.netserve.connection.ConnectionHandler;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-07-13 17:55:13 $
 */
class ExceptingHandlerManager implements ConnectionHandlerManager
{
    public ConnectionHandler aquireHandler()
        throws Exception
    {
        throw new Exception( "Humbug!" );
    }

    public void releaseHandler( ConnectionHandler handler )
    {
    }
}
