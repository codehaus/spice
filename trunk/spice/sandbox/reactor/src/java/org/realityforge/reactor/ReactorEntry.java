package org.realityforge.reactor;

import java.nio.channels.SocketChannel;

class ReactorEntry
{
   private final String _name;
   private final SocketChannel _channel;
   private final SocketDataHandler _handler;
   private final Object _passback;

   public ReactorEntry( final String name,
                        final SocketChannel channel,
                        final SocketDataHandler handler,
                        final Object passback )
   {
      if ( null == name )
      {
         throw new NullPointerException( "name" );
      }
      if ( null == channel )
      {
         throw new NullPointerException( "channel" );
      }
      if ( null == handler )
      {
         throw new NullPointerException( "handler" );
      }
      _name = name;
      _channel = channel;
      _handler = handler;
      _passback = passback;
   }

   public String getName()
   {
      return _name;
   }

   public SocketChannel getChannel()
   {
      return _channel;
   }

   public SocketDataHandler getHandler()
   {
      return _handler;
   }

   public Object getPassback()
   {
      return _passback;
   }
}
