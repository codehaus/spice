package org.jcomponent.netserve.connection.impl;

import org.jcomponent.netserve.connection.impl.AcceptorMonitor;
import org.jcomponent.netserve.connection.impl.DefaultAcceptorManager;

/**
 * A Pico compliant implementation of AcceptorManager.
 */
public class PicoAcceptorManager
   extends DefaultAcceptorManager
{
   /**
    * Create instance with default timeout and monitor.
    */
   public PicoAcceptorManager()
   {
   }

   /**
    * Create instance with default monitor.
    *
    * @param shutdownTimeout the timeout
    */
   public PicoAcceptorManager( final int shutdownTimeout )
   {
      setShutdownTimeout( shutdownTimeout );
   }

   /**
    * Create instance with default timeout.
    *
    * @param monitor the monitor
    */
   public PicoAcceptorManager( final AcceptorMonitor monitor )
   {
      setMonitor( monitor );
   }

   /**
    * Create instance specifying both timeout and monitor.
    *
    * @param shutdownTimeout the timeout
    * @param monitor the monitor
    */
   public PicoAcceptorManager( final int shutdownTimeout,
                               final AcceptorMonitor monitor )
   {
      setShutdownTimeout( shutdownTimeout );
      setMonitor( monitor );
   }
}
