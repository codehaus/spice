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
 * A Null SelectorMonitor.
 */
public class NullSelectorMonitor
    implements SelectorMonitor
{
    /**
     * Constant containing instance of NullSelectorMonitor.
     */
    public static final NullSelectorMonitor MONITOR = new NullSelectorMonitor();

    /**
     * @see SelectorMonitor#selectorStartup
     */
    public void selectorStartup()
    {
    }

    /**
     * @see SelectorMonitor#enteringSelectorLoop
     */
    public void enteringSelectorLoop()
    {
    }

    /**
     * @see SelectorMonitor#enteringSelect
     */
    public void enteringSelect()
    {
    }

    /**
     * @see SelectorMonitor#selectCompleted
     */
    public void selectCompleted( final int count )
    {
    }

    /**
     * @see SelectorMonitor#handlingSelectEvent
     */
    public void handlingSelectEvent( final SelectionKey key )
    {
    }

    /**
     * @see SelectorMonitor#exitingSelectorLoop
     */
    public void exitingSelectorLoop()
    {
    }

    /**
     * @see SelectorMonitor#selectorShutdown
     */
    public void selectorShutdown()
    {
    }

    /**
     * @see SelectorMonitor#errorClosingSelector
     */
    public void errorClosingSelector( final IOException ioe )
    {
    }

    /**
     * @see SelectorMonitor#invalidAttachment
     */
    public void invalidAttachment( final SelectionKey key )
    {
    }
}
