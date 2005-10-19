/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl;

import org.codehaus.spice.jervlet.Listener;

/**
 * Default implementation of a Listener. A listener represents
 * an object listening for new connections on a port, an address
 * using some type of protocol.
 *
 * @author Johan Sjoberg
 */
public class DefaultListener implements Listener
{
    /** Host */
    final String m_host;

    /** Port */
    final int m_port;

    /** Listener type */
    final int m_type;

    /**
     * Create a new instance
     *
     * @param host the host
     * @param port the port
     * @param type listener type
     */
    public DefaultListener( String host, int port, int type )
    {
        m_host = host;
        m_port = port;
        m_type = type;
    }
    /**
     * Fetch the port for this listener.
     *
     * @return The port number
     */
    public int getPort()
    {
        return m_port;
    }

    /**
     * Fetch the host for this listener. The host value is optional.
     * No host value means all hosts.
     *
     * @return The host address to listen to or null for all hosts
     */
    public String getHost()
    {
        return m_host;
    }

    /**
     * Fetch the connection type.
     *
     * @return The type id if this connection.
     */
    public int getType()
    {
        return m_type;
    }
}
