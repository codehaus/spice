package org.realityforge.reactor;

import java.nio.channels.SocketChannel;

public interface Reactor
{
   boolean isConnected( String name );

   void connect( String name,
                 SocketChannel channel,
                 SocketDataHandler handler,
                 Object passback )
      throws Exception;

   void disconnect( String name )
      throws Exception;
}
