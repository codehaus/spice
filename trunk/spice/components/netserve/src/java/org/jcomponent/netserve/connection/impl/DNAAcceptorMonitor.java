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
    * @see org.jcomponent.netserve.connection.impl.AbstractLoggingAcceptorMonitor#isDebugEnabled
    */
   protected boolean isDebugEnabled()
   {
      return m_logger.isDebugEnabled();
   }

   /**
    * @see org.jcomponent.netserve.connection.impl.AbstractLoggingAcceptorMonitor#debug
    */
   protected void debug( final String message )
   {
      m_logger.debug( message );
   }

   /**
    * @see org.jcomponent.netserve.connection.impl.AbstractLoggingAcceptorMonitor#info
    */
   protected void info( final String message )
   {
      m_logger.info( message );
   }

   /**
    * @see org.jcomponent.netserve.connection.impl.AbstractLoggingAcceptorMonitor#warn
    */
   protected void warn( final String message,
                        final Exception e )
   {
      m_logger.warn( message, e );
   }
}
