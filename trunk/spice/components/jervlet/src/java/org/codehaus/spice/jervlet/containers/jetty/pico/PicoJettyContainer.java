/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.containers.jetty.pico;

import java.io.File;
import java.util.List;

import org.picocontainer.Startable;

import org.codehaus.spice.jervlet.Container;
import org.codehaus.spice.jervlet.ContextHandler;
import org.codehaus.spice.jervlet.ContextMonitor;
import org.codehaus.spice.jervlet.Listener;
import org.codehaus.spice.jervlet.ListenerException;
import org.codehaus.spice.jervlet.ListenerHandler;
import org.codehaus.spice.jervlet.ListenerMonitor;
import org.codehaus.spice.jervlet.containers.jetty.DefaultJettyContainer;
import org.codehaus.spice.jervlet.containers.jetty.JettyContainer;
import org.codehaus.spice.jervlet.containers.jetty.ShieldingJettyContainer;

/**
 * Pico style component wrapping a Jetty Server
 * <br/><br/>
 * This Jetty container implementation handles the creation and
 * lifecycle of the underlaying Jetty Server. This component also
 * serves as a <code>ListenerHandler</code> and <code>Container</code>
 * using the wrapped <code>JettyContainer</code>. See ListenerHandler's
 * and Container's documentation about how to deploy webapps and start
 * listeners.
 *
 * @author Johan Sjoberg
 */
public class PicoJettyContainer implements Container, ListenerHandler, Startable
{
    /** Wrapped Jetty container */
    private JettyContainer m_container;

    /**
     * Create a new Pico style JettyContainer.
     *
     * @param configuration optional configuration data for the Jetty server
     * @param contextMonitor optional context monitor.
     *        null will result in a NOOP implementation
     * @param listenerMonitor optional listener monitor.
     *        null will result in a NOOP implementation.
     */
    public PicoJettyContainer( JettyContainerConfiguration configuration,
                               ContextMonitor contextMonitor,
                               ListenerMonitor listenerMonitor )

    {
        if( null != configuration && configuration.shieldJetty() )
        {
            final ShieldingJettyContainer shieldingJettyContainer =
              new ShieldingJettyContainer();
            shieldingJettyContainer.addJettyProperties(
              configuration.getProperties() );
            m_container = shieldingJettyContainer;
        }
        else
        {
            m_container = new DefaultJettyContainer();
        }
        Object jettyConfiguration = getJettyConfiguration( configuration );
        if( null != jettyConfiguration )
        {
            m_container.setJettyConfiguration( jettyConfiguration );
        }
        if( null != contextMonitor )
        {
            m_container.setContextMonitor( contextMonitor );
        }
        if( null != listenerMonitor )
        {
            m_container.setListenerMonitor( listenerMonitor );
        }
    }

    /**
     * Start the Jetty container
     * <br/><br/>
     * Note, the container must be started before any
     * <code>Listener</code>s or <code>Context</code>s
     * can be handled.
     *
     * @throws RuntimeException on all errors
     */
    public void start()
    {
        try
        {
            m_container.initialize();
            m_container.start();
        }
        catch( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Stop the Jetty container
     *
     * @throws RuntimeException on all errors
     */
    public void stop()
    {
        try
        {
            m_container.stop();
        }
        catch( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Fetch the configuration object to instantiate Jetty with.
     * <br/></br>
     * There are four possible return values;
     * (i) a <code>String</code> representing a Jetty configuration file,
     * (ii) a <code>String</code> that IS the Jetty XML configuration,
     * (iii) a <code>URL</code> pointing at a Jetty configuration and
     * (iv) null.
     * No checks about the configuration's correctness are done here.
     * Note that Jetty's XML configuration always starts with a
     * <b>&lt;Configure&gt;</b> element.
     *
     * @param configuration the configuration
     * @return <code>String</code>, <code>URL</code> or <code>null</code>
     */
    private Object getJettyConfiguration( final JettyContainerConfiguration configuration )
    {
        if( null == configuration || null == configuration.getConfiguration() )
        {
            return null;
        }
        if( configuration.getConfiguration() instanceof String )
        {
            String configurationString = (String)configuration.getConfiguration();
            configurationString.trim();
            if( configurationString.startsWith( "<" ) )
            {
                if( configurationString.indexOf( "?>" ) > 0 )
                {
                    configurationString = configurationString.substring(
                      configurationString.indexOf( "?>" ) + 2 );
                }
                return configurationString;
            }
            else
            {
                configurationString = configurationString.replace( '\\', File.separatorChar );
                configurationString = configurationString.replace( '/', File.separatorChar );
                return configurationString;
            }
        }
        return configuration.getConfiguration();
    }

    /**
     * Create a new context handler.
     *
     * @return A new ContextHandler.
     */
    public ContextHandler createContextHandler()
    {
        return m_container.createContextHandler();
    }

    /**
     * Destroy a context handler. If the given context
     * handler still has contexts, they will be stopped
     * and removed before destruction.
     *
     * @param contextHandler The ContextHandler to destroy
     */
    public void destroyContextHandler( ContextHandler contextHandler )
    {
        m_container.destroyContextHandler( contextHandler );
    }

    /**
     * Add a <code>Listener</code> the container.
     */
    public void addListener( Listener listener ) throws ListenerException
    {
        m_container.addListener( listener );
    }

    /**
     * Remove a <code>Listener</code> from the container.
     */
    public void removeListener( Listener listener ) throws ListenerException
    {
        m_container.removeListener( listener );
    }

    /**
     * Start a <code>Listener</code>.
     */
    public void startListener( Listener listener ) throws ListenerException
    {
        m_container.startListener( listener );
    }

    /**
     * Stop a <code>Listener</code>.
     */
    public void stopListener( Listener listener ) throws ListenerException
    {
        m_container.stopListener( listener );
    }

    /**
     * Fetch a list of all current <code>Listener</code>s. If there are
     * no listener the returned list can be empty.
     *
     * @return All new list all current listeners.
     */
    public List getListeners()
    {
        return m_container.getListeners();
    }

    /**
     * Check if a <code>Listener</code> is started or not.
     *
     * @param listener The listener
     * @return True if the listener is started, else false.
     */
    public boolean isStarted( Listener listener )
    {
        return m_container.isStarted( listener );
    }
}
