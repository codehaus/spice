/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet;

import java.util.List;

/**
 * A <code>ContexHandler</code> manages contexts in the system.
 * Supported functions are addition, removal, listing, starting
 * and stopping of <code>Contexts</code>.
 *
 * @author Johan Sjoberg
 * @author Peter Royal
 */
public interface ContextHandler
{
    /** Role, used in some component frameworks */
    String ROLE = ContextHandler.class.getName();

    /**
     * Add a context to the container.
     * 
     * @param context The context to add
     */
    void addContext( Context context );

    /**
     * Remove a context from the container.
     *
     * @param context The context to remove
     */
    void removeContext( Context context );

    /**
     * Start a context.
     *
     * @param context The context to start
     * @throws ContextException if the contex couldn't be started
     */
    void startContext( Context context ) throws ContextException;

    /**
     * Stop a context.
     *
     * @param context The context to stop
     * @throws ContextException if the contex couldn't be stopped
     */
    void stopContext( Context context ) throws ContextException;

    /**
     * List the <code>Context</code>s this <code>ContextHandler</code>
     * manages.
     *
     * @return A new list of Contexts
     */
    List getContexts();

    /**
     * Check if a <code>Context</code> is started on not. The given
     * context must be managed by the asked context handler.
     *
     * @param context The context to check
     * @return true if the given context is started, else false
     */
    boolean isStarted( Context context );
}