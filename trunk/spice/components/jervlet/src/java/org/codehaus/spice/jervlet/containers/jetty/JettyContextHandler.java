/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.containers.jetty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.codehaus.spice.jervlet.Context;
import org.codehaus.spice.jervlet.ContextException;
import org.codehaus.spice.jervlet.ContextHandler;
import org.codehaus.spice.jervlet.ContextMonitor;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.WebApplicationContext;

/**
 * Contex hander for a Jetty container. The context handler is really
 * the heart of the Jetty implementation. It is responsible for
 * adding, removing, starting and stopping web contexts
 * (web applications). A JettyContextHandler is always linked to a
 * JettyServer.
 *
 * @author Johan Sjoberg
 */
public class JettyContextHandler implements ContextHandler
{
    /** Jetty server */
    private Server m_server;
    /** Context monitor to send monitor events to */
    private ContextMonitor m_contextMonitor;
    /** Map of current contexts */
    private HashMap m_contextMap = new HashMap();

    /**
     * Create a new <code>ContextHandler</code> for Jetty servers.
     *
     * @param server the Jetty server
     * @param contextMonitor a context monitor
     * @throws IllegalArgumentException if an argument was null
     */
    public JettyContextHandler( Server server, ContextMonitor contextMonitor )
    {
        if( null == server )
        {
            throw new IllegalArgumentException(
              "Server given to constructor was null." );
        }
        if( null == contextMonitor )
        {
            throw new IllegalArgumentException(
              "ContextMonitor given to constructor was null." );
        }
        m_server = server;
        m_contextMonitor = contextMonitor;
    }

    /**
     * Add a context to the container.
     * <br/><br/>
     * This is done by creating a new <code>WebApplicationContext</code>
     * and giving it a special handler that uses the
     * <code>Instantiator</code> from the given <code>Context</code>
     * to create new Servlet or Filter class instances.
     *
     * @param context The context to add
     * @throws IllegalArgumentException if the given context was bad
     */
    public void addContext( Context context )
    {
        verifyContext( context );

        WebApplicationContext webApplicationContext =
          new WebApplicationContext( context.getResource().getPath() );
        webApplicationContext.addHandler( 0,
          new JettyWebApplicationHandler( context.getInstantiator() ) );
        webApplicationContext.setContextPath( context.getPath() );
        webApplicationContext.setExtractWAR( context.extractWebArchive() );

        if( null != context.getVirtualHosts() )
        {
            webApplicationContext.setVirtualHosts( context.getVirtualHosts() );
        }
        synchronized( this ) //TODO: Actually, we want to synch m_server and m_contextMap
        {
            if( !isLegalContext( context ) )
            {
                m_contextMap.put( context, webApplicationContext );
                m_server.addContext( webApplicationContext );
            }
        }
        m_contextMonitor.addContextNotification( this.getClass(), context );
    }

    /**
     * Remove a context from the container.
     *
     * @param context The context to remove.
     */
    public void removeContext( Context context )
    {
        if( isLegalContext( context ) )
        {
            final WebApplicationContext webApplicationContext;
            synchronized( this )  //TODO: Actually, we want to sync m_server and m_contextMap
            {
                try
                {
                    stopContext( context );
                }
                catch( ContextException ce )
                {
                    final String message =
                      "Context didn't stop cleanely. Message was [" + ce.getMessage() + "].";
                    m_contextMonitor.removeContextWarning( this.getClass(), context, message );
                }
                webApplicationContext =
                  (WebApplicationContext)m_contextMap.get( context );
                m_server.removeContext( webApplicationContext );
                m_contextMap.remove( context );
            }
            webApplicationContext.destroy();
            m_contextMonitor.removeContextNotification( this.getClass(), context );
        }
    }

    /**
     * List the <code>Context</code>s that this
     * <code>ContextHandler</code> currently manages.
     *
     * @return A new list of contexts holding all JervletContexts
     */
    public List getContexts()
    {
        return new ArrayList( m_contextMap.values() );
    }

    /**
     * Start a context.
     *
     * @param context The context to start.
     */
    public void startContext( Context context ) throws ContextException
    {
        if( isLegalContext( context ) )
        {
            WebApplicationContext webApplicationContext =
              getWebApplicationContext( context );
            if( !webApplicationContext.isStarted() )
            {
                try
                {
                    webApplicationContext.start();
                    //TODO: This notification will not be called if Jetty starts the contexts.
                    m_contextMonitor.startContextNotification( this.getClass(), context );
                }
                catch( Exception e )
                {
                    m_contextMonitor.startContextException( this.getClass(), context, e );
                }
            }
        }
    }

    /**
     * Stop a context
     *
     * @param context The context to stop.
     */
    public void stopContext( Context context ) throws ContextException
    {
        if( isLegalContext( context ) )
        {
            WebApplicationContext webApplicationContext =
              getWebApplicationContext( context );
            if( webApplicationContext.isStarted() )
            {
                try
                {
                    webApplicationContext.stop();
                    //TODO: This notification will not be called if Jetty stops the contexts.
                    m_contextMonitor.stopContextNotification( this.getClass(), context );
                }
                catch( InterruptedException ie )
                {
                    m_contextMonitor.stopContextException( this.getClass(), context, ie );
                }
            }
        }
    }

    /**
     * Check if <code>Context</context> owned by this handler
     * is started on not.
     *
     * @param context The context to check
     * @return true if the given context is owned by this context
     *         handler and started, else false
     */
    public boolean isStarted( Context context )
    {
        if( isLegalContext( context ) )
        {
            WebApplicationContext webApplicationContext =
              getWebApplicationContext( context );
            return webApplicationContext.isStarted();
        }
        return false;
    }

    /**
     * Check if a context belongs to this <code>ContextHandler</code>
     *
     * @return true if the context is legal for this ContextHandler
     */
    private boolean isLegalContext( Context context )
    {
        if( null == context )
        {
            return false;
        }
        return m_contextMap.containsKey( context );
    }

    /**
     * Fetch a Jetty <code>WebApplicationContext</code> representing
     * a Jervlet <code>Context</code>.
     *
     * @param context The Context
     * @return The WebApplicationContext for the given Context or null.
     */
    private WebApplicationContext getWebApplicationContext( Context context )
    {
        if( isLegalContext( context ) )
        {
            return (WebApplicationContext)m_contextMap.get( context );
        }
        return null;
    }

    /**
     * Destroy this ContextHandler. Before destruction all
     * deployed Contexts are undeployed.
     */
    protected void destroy()
    {
        synchronized( this )
        {
            List contexts = getContexts();
            for( int i = 0; i < contexts.size(); i++ )
            {
                removeContext( (Context)contexts.get( i ) );
            }
            m_server = null;
            m_contextMonitor = null;
            m_contextMap = null;
        }
    }

    /**
     * Verify a context
     * <br/><br/>
     * If the context's instantiator, path or resource is
     * <code>null</code>, an <code>IllegalArgumentException</code>
     * is thrown with proper message.
     *
     * @param context The context to verify
     * @throws IllegalArgumentException if something is wrong with the context.
     */
    private void verifyContext( Context context )
    {
        if( null == context.getInstantiator() )
        {
            final String message = "The context's Instantiator was null.";
            throw new IllegalArgumentException( message );
        }
        if( null == context.getPath() )
        {
            final String message = "The context's Path was null.";
            throw new IllegalArgumentException( message );
        }
        if( null == context.getResource() )
        {
            final String message = "The context's Resource was null.";
            throw new IllegalArgumentException( message );
        }
    }
}
