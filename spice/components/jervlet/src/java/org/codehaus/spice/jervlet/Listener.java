/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet;

/**
 * The Listener interface repesents some type of litener that passes
 * requests to a servlet container. Listeners are managed by a
 * <code>ListenerHandler</code>.
 * 
 * @author Johan Sjoberg
 * @author Peter Royal
 */
public interface Listener
{
    /** Role, used by some component frameworks */
    String ROLE = Listener.class.getName();

    /** HTTP listener */
    static final int HTTP = 1;

    /** SSL and TSL listener */
    static final int TSL = 2;

    /** AJP 1.3 listener */
    static final int AJP13 = 3;

    /**
     *  Fetch the port for this listener.
     *
     * @return The port number
     */
    int getPort();

    /**
     * Fetch the host for this listener. The host value is optional.
     * No host value means all hosts.
     *
     * @return The host address to listen to or null for all hosts
     */
    String getHost();

    /**
     *  Fetch the connection type.
     *
     * @return The type id if this connection.
     */
    int getType();
}