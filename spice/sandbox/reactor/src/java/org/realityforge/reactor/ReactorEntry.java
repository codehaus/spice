package org.realityforge.reactor;

import java.nio.channels.SocketChannel;
import java.nio.channels.SelectionKey;

/**
 * A class containing data relevent for a particular
 * connection.
 */
class ReactorEntry
{
   /**
    * Arbitary name associated with conenction.
    */
   private final String _name;

   /**
    * The key used to register connection in selector.
    */
   private final SelectionKey _key;

   /**
    * The channel associated with connection.
    */
   private final SocketChannel _channel;

   /**
    * The handler that gets called when data
    * is either transmitted or received.
    */
   private final SocketDataHandler _handler;

   /**
    * A object passed back to the handler specified
    * by client when registering entry.
    */
   private final Object _passback;

   /**
    * Create an instance of ReactorEntry.
    *
    * @param name the name
    * @param key the key
    * @param channel the channel
    * @param handler the handler
    * @param passback the passback object
    */
   ReactorEntry( final String name,
                 final SelectionKey key,
                 final SocketChannel channel,
                 final SocketDataHandler handler,
                 final Object passback )
   {
      if ( null == name )
      {
         throw new NullPointerException( "name" );
      }
      if ( null == key )
      {
         throw new NullPointerException( "key" );
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
      _key = key;
      _channel = channel;
      _handler = handler;
      _passback = passback;
   }

   /**
    * Return the name associated with connection.
    *
    * @return the name associated with connection.
    */
   String getName()
   {
      return _name;
   }

   /**
    * Return the key used to register connection in selector.
    *
    * @return the key used to register connection in selector.
    */
   SelectionKey getKey()
   {
      return _key;
   }

   /**
    * Return the channel that represents connection.
    *
    * @return the channel that represents connection.
    */
   SocketChannel getChannel()
   {
      return _channel;
   }

   /**
    * Return the associated handler.
    *
    * @return the associated handler.
    */
   SocketDataHandler getHandler()
   {
      return _handler;
   }

   /**
    * Return the user specified passback object.
    *
    * @return the user specified passback object.
    */
   Object getPassback()
   {
      return _passback;
   }
}
