/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets.impl;

import java.nio.channels.SelectionKey;

/**
 * The entry describing an acceptor for NIO AcceptorManager.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.4 $ $Date: 2003-10-23 03:39:16 $
 */
class NIOAcceptorEntry
{
    /**
     * The configuration for acceptor.
     */
    private final AcceptorConfig m_config;

    /**
     * The selection key the acceptor was registered with.
     */
    private final SelectionKey m_key;

    /**
     * Create entry for config and key.
     *
     * @param config the config
     * @param key the key
     */
    NIOAcceptorEntry( final AcceptorConfig config,
                      final SelectionKey key )
    {
        if( null == config )
        {
            throw new NullPointerException( "config" );
        }
        if( null == key )
        {
            throw new NullPointerException( "key" );
        }
        m_config = config;
        m_key = key;
        m_key.attach( this );
    }

    /**
     * Return the config object for acceptor.
     *
     * @return the config object for acceptor.
     */
    AcceptorConfig getConfig()
    {
        return m_config;
    }

    /**
     * Return the SelectorKey for acceptor.
     *
     * @return the SelectorKey for acceptor.
     */
    SelectionKey getKey()
    {
        return m_key;
    }
}
