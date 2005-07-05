/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl;

import org.codehaus.spice.jervlet.Context;
import org.codehaus.spice.jervlet.Instantiator;

import java.net.URL;

/**
 * Default implementation of a Context. A context represents a web
 * application. It has a path, e.g. '/myapp', an optional list of
 * virtual hosts, e.g. '[www.mydomain.com][www.myotherdomain.net]'
 * and a pointer to the resource. A resource is the physical URL to
 * a WAR file, or directory containing an exploaded war. In the case
 * of Jervlet (here) we also have an instantiator used to instantiate
 * the servlet and filter classes.
 * 
 * @author Johan Sjoberg
 */
public class DefaultContext implements Context
{
    /** The path of the context */
    private String m_path;

    /** Optional virtual hosts */
    private String[] m_virtualHosts;

    /** Web application resource pointer */
    private URL m_resource;

    /** Extract WARs or not */
    private boolean m_extractWebArchive;

    /** Servlet instantiator */
    private Instantiator m_instantiator;

    /**
     * Create a new Context
     *
     * @param path The path of the context
     * @param virtualHosts Optional virtual hosts, set to null for no virtual hosts.
     * @param resource The web application resource pointer
     * @param instantiator A servlet instantiator
     * @throws IllegalArgumentException if some of the arguments,
     *         except virtualHosts, are null.
     */
    public DefaultContext( final String path,
                           final String[] virtualHosts,
                           final URL resource,
                           final boolean extractWebArchive,
                           final Instantiator instantiator )
    {
        if( null == path || null == resource || null == instantiator )
        {
            throw new IllegalArgumentException( "A required argument was null." );
        }
        m_path = path;
        m_resource = resource;
        m_instantiator = instantiator;
        m_virtualHosts = virtualHosts;
        m_extractWebArchive = extractWebArchive;
    }

    /**
     * Fetch the web path
     */
    public String getPath()
    {
        return m_path;
    }

    /**
     * Fetch possible virtual hosts.
     */
    public String[] getVirtualHosts()
    {
        return m_virtualHosts;
    }

    /**
     * Fetch the web app resource pointer.
     */
    public URL getResource()
    {
        return m_resource;
    }

    /**
     * If the resource is a Web Archive, should it be extracted?
     *
     * @return true if the Container should extract the resource, else false
     */
    public boolean extractWebArchive()
    {
        return m_extractWebArchive;
    }

    /**
     * Fetch the class instantiator.
     */
    public Instantiator getInstantiator()
    {
        return m_instantiator;
    }
}