package org.jcomponent.netserve.selector;

import java.nio.channels.SelectionKey;
import java.io.IOException;

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
}
