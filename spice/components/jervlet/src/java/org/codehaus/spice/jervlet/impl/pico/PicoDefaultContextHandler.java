/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl.pico;

import java.util.List;

import org.codehaus.spice.jervlet.ContextHandler;
import org.codehaus.spice.jervlet.Container;
import org.codehaus.spice.jervlet.Context;
import org.picocontainer.Startable;

/**
 * @author <a href="mailto:peter.royal@pobox.com">peter royal</a>
 */
public class PicoDefaultContextHandler implements ContextHandler, Startable
{
    private final Container m_container;
    private ContextHandler m_handler;

    public PicoDefaultContextHandler( final Container container )
    {
        m_container = container;
    }

    public void start()
    {
        m_handler = m_container.createContextHandler();
    }

    public void stop()
    {
        m_container.destroyContextHandler( m_handler );
    }

    public void addContext( Context context )
    {
        m_handler.addContext( context );
    }

    public List getContexts()
    {
        return m_handler.getContexts();
    }

    public boolean isStarted( Context context )
    {
        return m_handler.isStarted( context );
    }

    public void removeContext( Context context )
    {
        m_handler.removeContext( context );
    }

    public void startContext( Context context )
    {
        m_handler.startContext( context );
    }

    public void stopContext( Context context )
    {
        m_handler.stopContext( context );
    }
}