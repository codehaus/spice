package org.jcomponent.netserve.selector;

import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.io.IOException;

class HelloSelectorEventHandler
   implements SelectorEventHandler
{
   static final byte[] MESSAGE = "HELLO!".getBytes();

   public void handleSelectorEvent( SelectionKey key )
   {
      final ServerSocketChannel channel = (ServerSocketChannel)key.channel();
      try
      {
         final SocketChannel socket = channel.accept();
         socket.socket().getOutputStream().write( MESSAGE );
         socket.socket().close();
      }
      catch ( IOException e )
      {
         e.printStackTrace();
      }
   }
}
