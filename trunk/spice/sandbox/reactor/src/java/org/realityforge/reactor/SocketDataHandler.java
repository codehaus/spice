package org.realityforge.reactor;

import java.nio.channels.SocketChannel;

public interface SocketDataHandler
{
   void dataReceived( String name, SocketChannel channel, Object passBack );

   void dataTransmitted( String name, SocketChannel channel, Object passBack );
}
