package org.jcomponent.netserve.selector.impl;

import java.nio.channels.SelectionKey;
import org.jcomponent.netserve.selector.SelectorEventHandler;

/**
 * Basic implementation of
 * SelectorEventHandler that does nothing.
 */
public class NullSelectorEventHandler
    implements SelectorEventHandler
{
    /**
     * Constant containing instance of NullSelectorEventHandler.
     */
    public static final NullSelectorEventHandler HANDLER = new NullSelectorEventHandler();

    /**
     * @see SelectorEventHandler#handleSelectorEvent
     */
    public void handleSelectorEvent( final SelectionKey key,
                                     final Object userData )
    {
    }
}
