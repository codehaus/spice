package org.jcomponent.netserve.sockets;

import java.nio.channels.SelectionKey;

/**
 * Interface implemente to receive events from
 * a Selector.
 */
public interface SelectorEventHandler
{
   /**
    * Method that receives events from selector.
    *
    * @param key the SelectionKey
    */
   void handleSelectorEvent( SelectionKey key );
}
