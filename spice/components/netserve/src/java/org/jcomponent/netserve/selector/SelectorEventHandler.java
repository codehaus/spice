/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.selector;

import java.nio.channels.SelectionKey;

/**
 * Interface implemente to receive events from
 * a Selector.
 */
public interface SelectorEventHandler
{
    /**
     * Method that receives events from selector.
     * The user MUST NOT modify or access the
     * keys attachment.
     *
     * @param key the SelectionKey
     * @param userData the data user specified when registering listener
     */
    void handleSelectorEvent( SelectionKey key, Object userData );
}
