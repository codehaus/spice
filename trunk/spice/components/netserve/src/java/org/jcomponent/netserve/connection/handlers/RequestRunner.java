package org.jcomponent.netserve.connection.handlers;

import java.net.Socket;

/**
 * Helper class to run actual handler in separate thread.
 */
class RequestRunner
   implements Runnable
{
   /**
    * the parent handler.
    */
   private final ThreadPerRequestHandler _parent;

   /**
    * The connection being handled.
    */
   private final Socket _socket;

   /**
    * Create runner so can run handle request in separate thread.
    *
    * @param parent the parent handler
    * @param socket the socket
    */
   RequestRunner( final ThreadPerRequestHandler parent,
                  final Socket socket )
   {
      _parent = parent;
      _socket = socket;
   }

   /**
    * Actually perform request by invoking performRequest
    * on parent handler.
    */
   public void run()
   {
      _parent.performRequest( _socket );
   }
}
