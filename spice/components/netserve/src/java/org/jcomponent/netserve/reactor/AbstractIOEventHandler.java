package org.jcomponent.netserve.reactor;

import java.nio.channels.SelectableChannel;

import org.jcomponent.netserve.reactor.IOEventHandler;

/**
 * Abstract noop implementation of IOEventHandler.
 */
public abstract class AbstractIOEventHandler
   implements IOEventHandler
{
   /**
    * @see IOEventHandler#accept
    */
   public void accept( final SelectableChannel channel,
                       final Object userData )
   {
   }

   /**
    * @see IOEventHandler#connect
    */
   public void connect( final SelectableChannel channel,
                        final Object userData )
   {
   }

   /**
    * @see IOEventHandler#receive
    */
   public void receive( final SelectableChannel channel,
                        final Object userData )
   {
   }

   /**
    * @see IOEventHandler#transmitted
    */
   public void transmitted( final SelectableChannel channel,
                            final Object userData )
   {
   }
}
