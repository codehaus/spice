/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.selector.impl;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * Monitor used to monitor events in the AcceptorManager.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-10-24 03:34:02 $
 */
public interface SelectorMonitor
{
    /**
     * Selector being started up.
     */
    void selectorStartup();

    /**
     * Entering the main loop that
     * accepts connections.
     */
    void enteringSelectorLoop();

    /**
     * Entering select call.
     */
    void enteringSelect();

    /**
     * Aselect has completed.
     *
     * @param count the number of accepts that are ready
     */
    void selectCompleted( int count );

    /**
     * About to handle a selection event.
     *
     * @param key the selection key
     */
    void handlingSelectEvent( SelectionKey key );

    /**
     * Exiting the main loop that
     * accepts connections.
     */
    void exitingSelectorLoop();

    /**
     * Selector being shutdown.
     */
    void selectorShutdown();

    /**
     * There was an error closing selector.
     *
     * @param ioe the exception
     */
    void errorClosingSelector( IOException ioe );

    /**
     * The specified key has an invalid attachment.
     *
     * @param key the key
     */
    void invalidAttachment( SelectionKey key );
}
