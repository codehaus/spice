package org.jcomponent.netserve.sockets;

import java.nio.channels.SelectionKey;

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
   public void handleSelectorEvent( final SelectionKey key )
   {
   }
}
