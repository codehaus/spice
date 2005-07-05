/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet;

/**
 * The <code>Container</code> interface represents a servlet container.
 * In Jervlet the container has only two functions, to create and
 * destroy <code>ContextHandler</code>s. The context handlers are used
 * to manage contexts.
 * <br/><br/>
 * The idea behind this interface is to shield web applications from other
 * components in the system, using the same container (read web server). A
 * context can only be managed through the <code>ContextManager</code>
 * that created it.
 * <br/><br/>
 * A container implementation representing a HTTP server/servlet container
 * would normally implement the <code>Container</code> and
 * <code>ListenerHandler</code> interfaces. In a system where all
 * components can access all contexts noting says that the container
 * implementation can't directly implement <code>ContextHandler</code> too,
 * for an unshielded context manager.
 * 
 * @author Johan Sjoberg
 * @author Peter Royal
 */
public interface Container
{
    String ROLE = Container.class.getName();

    /**                                    
     * Create a new context handler.
     * 
     * @return A new ContextHandler.
     */
    ContextHandler createContextHandler();
    
    /**
     * Destroy a context handler. If the given context
     * handler still has contexts, they will be stopped
     * and removed before destruction.
     * 
     * @param contextHandler The ContextHandler to destroy
     */
    void destroyContextHandler( ContextHandler contextHandler );
    //TODO: Verify that this method is really needed, perhaps just as a convenience...
}