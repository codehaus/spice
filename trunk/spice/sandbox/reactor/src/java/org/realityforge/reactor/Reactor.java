package org.realityforge.reactor;

import java.nio.channels.SocketChannel;

public interface Reactor
{
   void addConnection( String name,
                       SocketChannel channel,
                       SocketDataHandler handler,
                       Object passback )
      throws Exception;

   void removeConnection( String name )
      throws Exception;
}
