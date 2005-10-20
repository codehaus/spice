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
 * Pico component representing a ContextHandler
 *
 * @author Peter Royal
 * @author Johan Sjoberg
 */
public class PicoDefaultContextHandler implements ContextHandler, Startable
{
    /** Container used to get a context handler */
    private final Container m_container;

    /** Wrapped context handler */
    private ContextHandler m_handler;

    /**
     * Construct a new instance
     *
     * @param container The container to be used for creating
     *        and destroying context handlers
     */
    public PicoDefaultContextHandler( final Container container )
    {
        m_container = container;
    }

    /**
     * Start the context handler, meaning get a new
     * context handler from the container. No contexts
     * can be deployed before starting.
     */
    public void start()
    {
        m_handler = m_container.createContextHandler();
    }

    /**
     * Stop the context handler, meaning destroy the
     * wrapped context handler with the container. No
     * contexts can be deployed after stopping.
     */
    public void stop()
    {
        m_container.destroyContextHandler( m_handler );
    }

    /**
     * Add a context
     *
     * @param context The context to add
     */
    public void addContext( Context context )
    {
        m_handler.addContext( context );
    }

    /**
     * List all current contexts
     *
     * @return A list containing all current context. The
     *         list can be empty, but not null.
     */
    public List getContexts()
    {
        return m_handler.getContexts();
    }

    /**
     * Check if a context has been started
     * @param context The context to check
     * @return true if it has been started with this
     *         context handler, else false.
     */
    public boolean isStarted( Context context )
    {
        return m_handler.isStarted( context );
    }

    /**
     * Remove a context
     *
     * @param context The context to remove
     */
    public void removeContext( Context context )
    {
        m_handler.removeContext( context );
    }

    /**
     * Start a context
     *
     * @param context The context to start
     */
    public void startContext( Context context )
    {
        m_handler.startContext( context );
    }

    /**
     * Stop a context
     *
     * @param context The context to stop
     */
    public void stopContext( Context context )
    {
        m_handler.stopContext( context );
    }
}