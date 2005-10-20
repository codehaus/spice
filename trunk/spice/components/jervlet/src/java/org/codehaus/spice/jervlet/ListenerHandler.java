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
 * A <code>ListenerHandler</code> handles <code>Listener</code>s.
 *
 * @author Johan Sjoberg
 * @author Peter Royal
 */
public interface ListenerHandler
{
    /** Role, used in some component frameworks */
    String ROLE = ListenerHandler.class.getName();

    /**
     * Add a <code>Listener</code> the container.
     */
    void addListener( Listener listener ) throws ListenerException;

    /**
     * Remove a <code>Listener</code> from the container.
     */
    void removeListener( Listener listener ) throws ListenerException;

    /**
     * Start a <code>Listener</code>.
     */
    void startListener( Listener listener ) throws ListenerException;

    /**
     * Stop a <code>Listener</code>.
     */
    void stopListener( Listener listener ) throws ListenerException;

    /**
     * Fetch a list of all current <code>Listener</code>s. If there are
     * no listeners the returned list can be empty, but not null.
     *
     * @return All current listeners.
     */
    List getListeners();

    /**
     * Check if a <code>Listener</code> is started.
     *
     * @param listener The listener
     * @return True if the listener is started, else false.
     */
    boolean isStarted( Listener listener );
}