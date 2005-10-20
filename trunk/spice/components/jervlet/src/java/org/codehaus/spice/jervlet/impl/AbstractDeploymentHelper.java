/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl;

import java.net.URL;
import java.util.List;

import org.codehaus.spice.jervlet.Context;
import org.codehaus.spice.jervlet.ContextException;
import org.codehaus.spice.jervlet.ContextHandler;
import org.codehaus.spice.jervlet.Instantiator;

/**
 * Basic helper class to be extended by "deployment" compoents.
 * It includes common functionality to set and get resources
 * plus methods to help deployment and undeployment of applications.
 * 
 * @author Johan Sjoberg
 */
public abstract class AbstractDeploymentHelper
{
    /** Selvlet class instantiator */
    private Instantiator m_instantiator;

    /** Context handler */
    private ContextHandler m_contextHandler;

    /**
     * Set the <code>Instantiator</code> to be used when creating Servlet
     * instances. Note that the instantiator can only be set once.
     *
     * @param instantiator The local servlet class instantiator
     */
    public void setInstantiator( Instantiator instantiator )
    {
        if( null == m_instantiator )
        {
            m_instantiator = instantiator;
        }
    }

    /**
     * Set the <code>ContextHandler</code> for this component. Note that
     * the context handler can only be set once.
     *
     * @param contextHandler The local context handler
     */
    public void setContextHandler( ContextHandler contextHandler )
    {
        if( null == m_contextHandler )
        {
            m_contextHandler = contextHandler;
        }
    }

    /**
     * Fetch the <code>ContextHandler</code> used here for handling
     * contexts.
     *
     * @return The local context handler.
     */
    public ContextHandler getContextHandler()
    {
        return m_contextHandler;
    }

    /**
     * Deploy a context.
     *
     * @param path The web path of the context.
     * @param resource A pointer to the resource for this web application.
     */
    public void deployContext( final String path, final URL resource )
        throws ContextException
    {
        deployContext( path, null, resource );
    }

    /**
     * Deploy a context.
     *
     * @param path The web path of the context.
     * @param virtualHosts A list of virtual hosts to deploy this context
     *                     to or null if virtual hosts are not used.
     * @param resource A pointer to the resource for this web application.
     */
    public void deployContext( final String path,
                               final String[] virtualHosts,
                               final URL resource ) throws ContextException
    {
        DefaultContext context = new DefaultContext(
          path, virtualHosts, resource, true, m_instantiator );
        m_contextHandler.addContext( context );
        m_contextHandler.startContext( context );
    }

    /**
     * Deploy a context.
     *
     * @param path The web path of the context
     * @param virtualHosts A list of virtual hosts to deploy this context
     *                     to or null if virtual hosts are not used
     * @param resource A pointer to the resource for this web application
     * @param extractWebArchive Should WARs be extracted by the container?
     */
    public void deployContext( final String path, 
                               final String[] virtualHosts,
                               final URL resource,
                               final boolean extractWebArchive )
        throws ContextException
    {
        DefaultContext context = new DefaultContext(
          path, virtualHosts, resource, extractWebArchive, m_instantiator );
        m_contextHandler.addContext( context );
        m_contextHandler.startContext( context );
    }

    /**
     * Undeploy a context.
     *
     * @param path The path of the context to undeploy
     */
    public void undeployContext( final String path ) throws ContextException
    {
        Context context = getContext( path );
        if( null != context )
        {
            m_contextHandler.stopContext( context );
            m_contextHandler.removeContext( context );
        }
    }

    /**
     * Fetch the <code>Context</code> for a web path.
     *
     * @param path The path of the context to look for.
     * @return The context for the given path or null if none was found.
     */
    private Context getContext( final String path )
    {
        if( null == path )
        {
            return null;
        }
        List contexts = m_contextHandler.getContexts();
        for( int i = 0; i < contexts.size(); i++ )
        {
            Context context = (Context)contexts.get( i );
            if( path.equals( context.getPath() ) )
            {
                return context;
            }
        }
        return null;
    }
}
