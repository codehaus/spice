package org.jcomponent.netserve.connection.impl;

import org.jcontainer.dna.Logger;
import org.jcomponent.netserve.connection.impl.AbstractLoggingAcceptorMonitor;

/**
 * AcceptorMonitor that writes out messages to DNA Logger.
 */
public class DNAAcceptorMonitor
   extends AbstractLoggingAcceptorMonitor
{
   /**
    * The associated DNA Logger.
    */
   private final Logger m_logger;

   /**
    * Create AcceptorMonitor that writes to DNA Logger.
    *
    * @param logger the logger
    */
   public DNAAcceptorMonitor( final Logger logger )
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
