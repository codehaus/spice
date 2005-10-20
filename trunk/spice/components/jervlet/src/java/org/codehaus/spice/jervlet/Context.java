/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet;

import java.net.URL;

/**
 * The <code>Context</code> interface represents a web context. A web
 * context holds information about a web application. This includes
 * e.g. the path, possible virtual hosts and a pointer to the resource.
 * In Jervlet, contexts also hold a reference to an
 * <code>Instantiator</code>. The instantiator is used to create new
 * instances of servlets or servlet filters.
 *
 * @author Johan Sjoberg
 * @author Peter Royal
 */
public interface Context
{
    /** Role, used in some component frameworks */
    String ROLE = Context.class.getName();

    /**
     * Fetch the path of the web application, e.g. "/myapp".
     *
     * @return The path of the web appliaction
     */
    String getPath();

    /**
     * Fetch possible virtual hosts defined for this context. As
     * virtual hosts are optional for web contexts the returned
     * array can be empty or null.
     *
     * @return An array of virtual hosts for this context, or null.
     */
    String[] getVirtualHosts();

    /**
     * Fetch the resource for this context. A resource is a pointer
     * to the web archive. A web archive is usually a .war file or a
     * directory including the exploded WAR.
     *
     * @return The resource URL to the web application
     */
    URL getResource();

    /**
     * If the resource is a WAR file, should it be extracted?
     *
     * @return true if the container should extract the resource, else false
     */
    boolean extractWebArchive();

    /**
     * Fetch the <code>Instantiator</code> that should be used to instantiate
     * the servlet and filter classes in this context.
     *
     * @return A reference to this context's instantiator implementation.
     */
    Instantiator getInstantiator();
}