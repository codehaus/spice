/*
 * Copyright (C) 2005 The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.containers.jetty;

import org.codehaus.spice.jervlet.Container;
import org.codehaus.spice.jervlet.ContextHandler;
import org.codehaus.spice.jervlet.ListenerHandler;
import org.codehaus.spice.jervlet.ContextMonitor;
import org.codehaus.spice.jervlet.ListenerMonitor;

/**
 * Interface common for Jetty servlet containers
 *
 * @author Peter Royal
 * @author Johan Sjoberg
 */
public interface JettyContainer extends Container, ContextHandler, ListenerHandler
{
    /**
     * Initialize the Jetty container
     *
     * @throws Exception on all errors, most probably a IOException,
     *         rethrown from the creation of Jetty's Server.
     */
    void initialize() throws Exception;

    /**
     * Start the Jetty container
     *
     * @throws Exception on all errors
     */
    void start() throws Exception;

    /**
     * Stop the container
     *
     * @throws Exception on all errors
     */
    void stop() throws Exception;

    /**
     * Set configuration for the Jetty <code>Server</code>. The
     * underlaying Jetty server will be created with this
     * configuration.
     *
     * @param configuration the configuration to pass to Jetty
     */
    void setJettyConfiguration( Object configuration );

    /**
     * Set a context monitor
     *
     * @param contextMonitor a context monitor.
     */
    void setContextMonitor( ContextMonitor contextMonitor );

    /**
     * Set the context monitor
     *
     * @param listenerMonitor a context monitor
     */
    void setListenerMonitor( ListenerMonitor listenerMonitor );
}