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
