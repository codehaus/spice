package org.jcomponent.netserve.selector;

import java.nio.channels.SelectionKey;
import java.io.IOException;

class DebugSelectorMonitor
   implements SelectorMonitor
{
   public void selectorStartup()
   {
      System.out.println( "selectorStartup()" );
   }

   public void enteringSelectorLoop()
   {
      System.out.println( "enteringSelectorLoop()" );
   }

   public void enteringSelect()
   {
      System.out.println( "enteringSelect()" );
   }

   public void selectCompleted( int count )
   {
      System.out.println( "selectCompleted(" + count + ")" );
   }

   public void handlingSelectEvent( SelectionKey key )
   {
      System.out.println( "handlingSelectEvent(" + key + ")" );
   }

   public void exitingSelectorLoop()
   {
      System.out.println( "exitingSelectorLoop()" );
   }

   public void selectorShutdown()
   {
      System.out.println( "selectorShutdown()" );
   }

   public void errorClosingSelector( IOException ioe )
   {
      System.out.println( "errorClosingSelector(" + ioe + ")" );
   }
}
