/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.netserve.connection.impl;

import org.realityforge.netserve.connection.ConnectionHandlerManager;
import org.realityforge.netserve.connection.ConnectionHandler;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-23 09:37:17 $
 */
public class ExceptingHandlerManager implements ConnectionHandlerManager
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
