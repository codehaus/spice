package org.realityforge.reactor;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Hashtable;
import java.util.Map;

public class DefaultReactor
   implements Reactor
{
   /**
    * The map of name->ReactorEntry.
    */
   private final Map _entrys = new Hashtable();

   /**
    * Selector used to monitor for accepts.
    */
   private Selector _selector;

   /**
    * @see Reactor#isConnected
    */
   public boolean isConnected( final String name )
   {
      return _entrys.containsKey( name );
   }

   /**
    * @see Reactor#connect
    */
   public void connect( final String name,
                        final SocketChannel channel,
                        final SocketDataHandler handler,
                        final Object passback )
      throws Exception
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

      synchronized ( _entrys )
      {
         if ( isConnected( name ) )
         {
            final String message =
               "Connection already exists with name " + name;
            throw new IllegalArgumentException( message );
         }

         channel.configureBlocking( false );
         final SelectionKey key =
            channel.register( _selector,
                              SelectionKey.OP_READ |
                              SelectionKey.OP_WRITE );

         final ReactorEntry entry =
            new ReactorEntry( name, key, channel, handler, passback );
         _entrys.put( name, entry );
         //_monitor.connectionRegistered( name, channel );
      }
   }

   /**
    * @see Reactor#disconnect
    */
   public void disconnect( String name )
      throws Exception
   {
      final ReactorEntry entry =
         (ReactorEntry) _entrys.remove( name );
      if ( null == entry )
      {
         final String message = "No connection with name " + name;
         throw new IllegalArgumentException( message );
      }
      entry.getKey().cancel();
      //_monitor.connectionDeregistered( name, channel );
   }
}
