package org.codehaus.spice.netserve.connection.impl;

import org.apache.avalon.framework.logger.Logger;

/**
 * AcceptorMonitor that writes out messages to Avalon Logger.
 */
public class AvalonAcceptorMonitor
   extends AbstractLoggingAcceptorMonitor
{
   /**
    * The associated Avalon Logger.
    */
   private final Logger m_logger;

   /**
    * Create AcceptorMonitor that writes to Avalon Logger.
    *
    * @param logger the logger
    */
   public AvalonAcceptorMonitor( final Logger logger )
   {
      if ( null == logger )
      {
         throw new NullPointerException( "logger" );
      }
      m_logger = logger;
   }

   /**
    * @see AbstractLoggingAcceptorMonitor#isDebugEnabled
    */
   protected boolean isDebugEnabled()
   {
      return getLogger().isDebugEnabled();
   }

   /**
    * @see AbstractLoggingAcceptorMonitor#debug
    */
   protected void debug( final String message )
   {
      getLogger().debug( message );
   }

   /**
    * @see AbstractLoggingAcceptorMonitor#info
    */
   protected void info( final String message )
   {
      getLogger().info( message );
   }

   /**
    * @see AbstractLoggingAcceptorMonitor#warn
    */
   protected void warn( final String message,
                        final Exception e )
   {
      getLogger().warn( message, e );
   }

   /**
    * Return the logger.
    *
    * @return the logger
    */
   protected Logger getLogger()
   {
      return m_logger;
   }
}
