package org.jcomponent.netserve.connection.impl;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * An abstract monitor that writes out messages
 * for acceptor events. Need to subclass and implement
 * methods to write out log messages.
 */
public abstract class AbstractLoggingAcceptorMonitor
   implements AcceptorMonitor
{
   /**
    * @see AcceptorMonitor#acceptorCreated
    */
   public void acceptorCreated( final String name,
                                final ServerSocket serverSocket )
   {
      final String message =
         "Creating Acceptor " + name + " on " +
         serverSocket.getInetAddress().getHostAddress() + ":" +
         serverSocket.getLocalPort() + ".";
      info( message );
   }

   /**
    * @see AcceptorMonitor#acceptorClosing
    */
   public void acceptorClosing( final String name,
                                final ServerSocket serverSocket )
   {
      final String message = "Closing Acceptor " + name + ".";
      info( message );
   }

   /**
    * @see AcceptorMonitor#serverSocketListening
    */
   public void serverSocketListening( final String name,
                                      final ServerSocket serverSocket )
   {
      if ( isDebugEnabled() )
      {
         final String message =
            "About to call accept() on ServerSocket '" + name + "'.";
         debug( message );
      }
   }

   /**
    * @see org.jcomponent.netserve.connection.impl.AcceptorMonitor#errorAcceptingConnection
    */
   public void errorAcceptingConnection( final String name,
                                         final IOException ioe )
   {
      warn( "Error Accepting connection on " + name, ioe );
   }

   /**
    * @see org.jcomponent.netserve.connection.impl.AcceptorMonitor#errorClosingServerSocket
    */
   public void errorClosingServerSocket( final String name,
                                         final IOException ioe )
   {
      warn( "Error Closing Server Socket " + name, ioe );
   }

   /**
    * Return true if debug logging enabled.
    *
    * @return true if debug logging enabled.
    */
   protected abstract boolean isDebugEnabled();

   /**
    * Write out debug message.
    *
    * @param message the message
    */
   protected abstract void debug( String message );

   /**
    * Write out info message.
    *
    * @param message the message
    */
   protected abstract void info( String message );

   /**
    * Write out warn message.
    *
    * @param message the message
    * @param e the warnings cause
    */
   protected abstract void warn( String message, Exception e );
}
