/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.impl;

import java.util.ArrayList;
import java.util.List;

import org.jcomponent.netserve.connection.ConnectionHandler;
import org.jcomponent.netserve.connection.ConnectionHandlerManager;

/**
 * test connectionahndler factory.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-08-31 09:33:55 $
 */
class TestConnectionHandlerManager
    implements ConnectionHandlerManager
{
    private final List m_list = new ArrayList();

    void addHandler( final ConnectionHandler handler )
    {
        m_list.add( handler );
    }

    public ConnectionHandler aquireHandler()
        throws Exception
    {
        return (ConnectionHandler)m_list.remove( 0 );
    }

    public void releaseHandler( ConnectionHandler handler )
    {
    }
}
