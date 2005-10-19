/*
 * Copyright (C) 2005 The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.containers.jetty;

import java.net.URL;
import java.util.List;

import org.codehaus.spice.jervlet.ContextMonitor;
import org.codehaus.spice.jervlet.ListenerMonitor;
import org.codehaus.spice.jervlet.ContextHandler;
import org.codehaus.spice.jervlet.Listener;
import org.codehaus.spice.jervlet.ListenerException;
import org.codehaus.spice.jervlet.Context;
import org.codehaus.spice.jervlet.ContextException;
import org.codehaus.spice.jervlet.impl.NoopContextMonitor;
import org.codehaus.spice.jervlet.impl.NoopListenerMonitor;
import org.mortbay.jetty.Server;

/**
 * Component representing a plain Jetty container. It wraps a Jetty
 * <code>Server</code> class through an internal
 * <code>JettyServer</code> member.
 *
 * @author Peter Royal
 * @author Johan Sjoberg
 */
public class DefaultJettyContainer implements JettyContainer
{
    /** Context monitor for the underlaying Jetty container */
    private ContextMonitor m_contextMonitor;

    /** Listener monitor for the underlaying Jetty container */
    private ListenerMonitor m_listenerMonitor;

    /** Some type of Jetty configuration (can be null) */
    private Object m_jettyConfiguration;

    /** Underlaying Jetty container */
    private JettyServer m_jettyServer;

    /** Default and unshielded context handler */
    private ContextHandler m_defaultContextHandler;

    /**
     * Create a new DefaultJettyContainer
     */
    public DefaultJettyContainer()
    {
        m_contextMonitor = new NoopContextMonitor();
        m_listenerMonitor = new NoopListenerMonitor();
    }

    /**
     * Give the component a context monitor. If none is given, a
     * <code>NoopContextMonitor</code> will be used.
     *
     * @param contextMonitor the context monitor
     */
    public void setContextMonitor( final ContextMonitor contextMonitor )
    {
        m_contextMonitor = contextMonitor;
        if( null != m_jettyServer )
        {
            m_jettyServer.setContextMonitor( contextMonitor );
        }
    }

    /**
     * Give the component a listener monitor. If none is given, a
     * <code>NoopListenerMonitor</code> will be used.
     *
     * @param listenerMonitor the listener monitor
     */
    public void setListenerMonitor( final ListenerMonitor listenerMonitor )
    {
        m_listenerMonitor = listenerMonitor;
        if( null != m_jettyServer )
        {
            m_jettyServer.setListenerMonitor( listenerMonitor );
        }
    }

    /**
     * Give the underlaying Jetty implementation a configuration.
     * This is only useful before initialization as an empty
     * jetty <code>Server</code> will be created if no configuration
     * was available.
     *
     * @param configuration The configuration, <code>String</code> or
     *        <code>URL</code>
     */
    public void setJettyConfiguration( final Object configuration )
    {
        m_jettyConfiguration = configuration;
    }

    /**
     * Initialize the component. This implementation is not
     * operational before <code>initialize</code> has been called.
     *
     * @throws Exception on all errors
     */
    public void initialize() throws Exception
    {
        if( null != m_jettyServer )
        {
            return;
        }
        final Server jettyServer;
        if( null != m_jettyConfiguration )
        {
            if( m_jettyConfiguration instanceof String )
            {
                jettyServer = new Server( (String)m_jettyConfiguration );
            }
            else if( m_jettyConfiguration instanceof URL )
            {
                jettyServer = new Server( (URL)m_jettyConfiguration );
            }
            else
            {
                throw new Exception(
                  "Unknown configuration type. Only String, URL or null are supported." );
            }
        }
        else
        {
            jettyServer = new Server();
        }
        m_jettyServer = new JettyServer( jettyServer,
                                         m_listenerMonitor,
                                         m_contextMonitor );
    }

    /**
     * Start the Jetty server.
     *
     * @throws Exception on all errors
     */
    public void start() throws Exception
    {
        if( !m_jettyServer.isServerStarted() )
        {
            m_jettyServer.start();
            m_defaultContextHandler = m_jettyServer.createContextHandler();
        }
    }

    /**
     * Stop the Jetty server.
     *
     * @throws Exception on all errors
     */
    public void stop() throws Exception
    {
        if( m_jettyServer.isServerStarted() )
        {
            m_jettyServer.stop();
        }
    }

    /**
     * Create a new context handler.
     *
     * @return A new <code>ContextHandler</code> instance
     */
    public ContextHandler createContextHandler()
    {
        return m_jettyServer.createContextHandler();
    }

    /**
     * Destroy a context handler. If the given context
     * handler still has contexts, they will be stopped
     * and removed before destruction.
     *
     * @param contextHandler <code>ContextHandler</code> to destroy
     */
    public void destroyContextHandler( final ContextHandler contextHandler )
    {
        m_jettyServer.destroyContextHandler( contextHandler );
    }

    /**
     * Add a <code>Listener</code> the container.
     *
     * @param listener the listener to add
     * @throws ListenerException rethrown from
     *         <code>JettyServler.addListener</code>
     */
    public void addListener( final Listener listener ) throws ListenerException
    {
        m_jettyServer.addListener( listener );
    }

    /**
     * Remove a <code>Listener</code> from the container.
     *
     * @param listener the listener to remove
     */
    public void removeListener( final Listener listener )
    {
        m_jettyServer.removeListener( listener );
    }

    /**
     * Start a <code>Listener</code>.
     *
     * @param listener the listener to start
     * @throws ListenerException if the listener couldn't be started
     */
    public void startListener( final Listener listener ) throws ListenerException
    {
        m_jettyServer.startListener( listener );
    }

    /**
     * Stop a <code>Listener</code>.
     *
     * @param listener the listener to stop
     * @throws ListenerException if the listener couldn't be stopped
     */
    public void stopListener( final Listener listener ) throws ListenerException
    {
        m_jettyServer.stopListener( listener );
    }

    /**
     * Fetch a list of all current <code>Listener</code>s. If there
     * are no listeners the returned list will be empty, but never
     * null.
     *
     * @return a <b>new</b> list containing all current listeners.
     */
    public List getListeners()
    {
        return m_jettyServer.getListeners();
    }

    /**
     * Check if a <code>Listener</code> is started or not.
     *
     * @param listener The listener
     * @return true if the listener is started, else false.
     */
    public boolean isStarted( final Listener listener )
    {
        return m_jettyServer.isStarted( listener );
    }

    /**
     * Add a context to the container.
     *
     * @param context The context to add
     */
    public void addContext( final Context context )
    {
        m_defaultContextHandler.addContext( context );
    }

    /**
     * Remove a context from the container.
     *
     * @param context The context to remove
     */
    public void removeContext( final Context context )
    {
        m_defaultContextHandler.removeContext( context );
    }

    /**
     * Start a context.
     *
     * @param context The context to start
     * @throws ContextException rethrown from
     *         <code>ContextHandler.startContext</code>
     */
    public void startContext( final Context context ) throws ContextException
    {
        m_defaultContextHandler.startContext( context );
    }

    /**
     * Stop a context.
     *
     * @param context The context to stop
     * @throws ContextException if the context couldn't be stopped
     */
    public void stopContext( final Context context ) throws ContextException
    {
        m_defaultContextHandler.startContext( context );
    }

    /**
     * List the <code>Context</code>s that this <code>ContextHandler</code>
     * can manage.
     *
     * @return a new list of contexts holding all JervletContexts
     */
    public List getContexts()
    {
        return m_defaultContextHandler.getContexts();
    }

    /**
     * Check if <code>Context</context> owned by this handler
     * is started on not.
     *
     * @param context The context to check
     * @return true if the given context is started, else false
     */
    public boolean isStarted( final Context context )
    {
        return m_defaultContextHandler.isStarted( context );
    }
}