package org.jcomponent.netserve.selector.impl;

import java.nio.channels.SelectionKey;

import org.jcomponent.netserve.selector.SelectorEventHandler;

class DelayingSelectorEventHandler
   implements SelectorEventHandler
{
   private final long m_delay;

   DelayingSelectorEventHandler( long delay )
   {
      m_delay = delay;
   }

   public void handleSelectorEvent( SelectionKey key, Object userData )
   {
      while ( true )
      {
         try
         {
            Thread.sleep( m_delay );
            System.out.println( "Exiting delay loop" );
            return;
         }
         catch ( InterruptedException e )
         {
         }
      }
   }
}
