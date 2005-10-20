/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl.pico;

import org.codehaus.spice.jervlet.ContextHandler;
import org.codehaus.spice.jervlet.Instantiator;
import org.codehaus.spice.jervlet.impl.DefaultContext;
import org.picocontainer.Startable;

import java.net.URL;

/**
 * Pico component wrapping a Context.
 *
 * @author Peter Royal
 * @author Johan Sjoberg
 */
public class PicoContext extends DefaultContext implements Startable
{
    /** Context handler */
    private final ContextHandler m_contextHandler;

    /**
     * Construct a new PicoContext
     *
     * @param contextHandler Context Handler to deploy/start/stop/undeploy
     *        the context with
     * @param path The web path
     * @param virtualHosts List of virtual hosts to deploy the
     *        contest to (optional), set to null for all hosts
     * @param resource Application resource, meaning a pointer
     *        to the web archive
     * @param extractWar If the resource points at a .war file,
     *        should it be exploded
     * @param instantiator The instantiator reponsible for creating
     *        new servlet or filter classes
     */
    public PicoContext( ContextHandler contextHandler,
                        String path,
                        String[] virtualHosts,
                        URL resource,
                        boolean extractWar,
                        Instantiator instantiator )
    {
        super(path, virtualHosts, resource, extractWar, instantiator);
        m_contextHandler = contextHandler;
    }

    /**
     * Construct a new PicoContext
     *
     * @param contextHandler Context Handler to deploy/start/stop/undeploy
     *        the context with
     * @param path The web path
     * @param resource Application resource, meaning a pointer
     *        to the web archive
     * @param instantiator The instantiator reponsible for creating
     *        new servlet or filter classes
     */
    public PicoContext( ContextHandler contextHandler,
                        String path,
                        URL resource,
                        Instantiator instantiator )
    {
        super(path, null, resource, false, instantiator);
        m_contextHandler = contextHandler;
    }

    /**
     * Deploy and start the context
     */
    public void start()
    {
        m_contextHandler.addContext( this );
        m_contextHandler.startContext( this );
    }

    /**
     * Undeploy and stop the context
     */
    public void stop()
    {
        m_contextHandler.stopContext( this );
        m_contextHandler.removeContext( this );
    }
}