package org.jcomponent.netserve.selector.impl;

import org.jcomponent.netserve.selector.SelectorEventHandler;

/**
 * A simple class that contains data relating
 * to a specific Selector registration.
 */
class SelectorEntry
{
   /**
    * The associated handler that is passed
    * events about channel.
    */
   private final SelectorEventHandler _handler;

   /**
    * The user specified data that is passed to the handler.
    */
   private final Object _userData;

   /**
    * Create an Entry for Selector registration.
    *
    * @param handler the handler
    * @param userData the user specified data
    */
   SelectorEntry( final SelectorEventHandler handler,
                  final Object userData )
   {
      if ( null == handler )
      {
         throw new NullPointerException( "handler" );
      }
      _handler = handler;
      _userData = userData;
   }

   /**
    * Return the handler for channel.
    *
    * @return the handler for channel.
    */
   SelectorEventHandler getHandler()
   {
      return _handler;
   }

   /**
    * Return the userData passed to handler.
    *
    * @return the userData passed to handler.
    */
   Object getUserData()
   {
      return _userData;
   }
}
