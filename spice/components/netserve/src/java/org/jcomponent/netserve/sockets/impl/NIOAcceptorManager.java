/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.util.Hashtable;
import java.util.Map;

import org.jcomponent.netserve.sockets.SocketAcceptorManager;
import org.jcomponent.netserve.sockets.SocketConnectionHandler;

/**
 * Abstract implementation of {@link SocketAcceptorManager} that NIO
 * to monitor several server sockets.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.16 $ $Date: 2003-10-23 03:29:02 $
 * @dna.component
 * @dna.service type="SocketAcceptorManager"
 */
public class NIOAcceptorManager
   extends AbstractNIOReactor
   implements SocketAcceptorManager
{
   /**
    * The map of name->NIOAcceptorEntry.
    */
   private final Map m_acceptors = new Hashtable();

   /**
    * The map of socket->NIOAcceptorEntry.
    */
   private final Map m_socket2Entry = new Hashtable();

   /**
    * Shutdown the selector and any associated acceptors.
    */
   public void shutdown()
   {
      setRunning( false );
      shutdownAcceptors();
      shutdownSelector();
   }

   /**
    * Dispose the ConnectionManager which involves shutting down all
    * the connected acceptors.
    */
   protected synchronized void shutdownAcceptors()
   {
      final String[] names;
      synchronized ( m_acceptors )
      {
         names = (String[]) m_acceptors.keySet().toArray( new String[ 0 ] );
      }
      for ( int i = 0; i < names.length; i++ )
      {
         disconnect( names[ i ] );
      }
   }

   /**
    * @see SocketAcceptorManager#connect
    */
   public void connect( final String name,
                        final ServerSocket socket,
                        final SocketConnectionHandler handler )
      throws Exception
   {
      if ( null == name )
      {
         throw new NullPointerException( "name" );
      }
      if ( null == socket )
      {
         throw new NullPointerException( "socket" );
      }
      if ( null == handler )
      {
         throw new NullPointerException( "handler" );
      }
      final ServerSocketChannel channel = socket.getChannel();
      if ( null == channel )
      {
         final String message =
            "Socket needs to be created using " +
            "ServerSocketChannel.open for NIO " +
            "acceptor manager";
         throw new IllegalArgumentException( message );
      }

      synchronized ( m_acceptors )
      {
         if ( isConnected( name ) )
         {
            final String message =
               "Connection already exists with name " + name;
            throw new IllegalArgumentException( message );
         }

         channel.configureBlocking( false );
         final SelectionKey key =
            registerChannel( channel, SelectionKey.OP_ACCEPT );

         final AcceptorConfig acceptor =
            new AcceptorConfig( name, socket, handler );
         final NIOAcceptorEntry entry =
            new NIOAcceptorEntry( acceptor, key );
         m_acceptors.put( name, entry );
         m_socket2Entry.put( socket, entry );
         getMonitor().acceptorCreated( name, socket );
      }
   }

   /**
    * @see SocketAcceptorManager#isConnected
    */
   public boolean isConnected( final String name )
   {
      return m_acceptors.containsKey( name );
   }

   /**
    * @see SocketAcceptorManager#disconnect
    */
   public void disconnect( final String name )
   {
      final NIOAcceptorEntry entry =
         (NIOAcceptorEntry) m_acceptors.remove( name );
      if ( null == entry )
      {
         final String message = "No connection with name " + name;
         throw new IllegalArgumentException( message );
      }
      m_socket2Entry.remove( entry.getConfig().getServerSocket() );
      entry.getKey().cancel();
      getMonitor().acceptorClosing( name );
   }

   /**
    * @see AbstractNIOReactor#handleChannel
    */
   protected void handleChannel( final SelectionKey key )
   {
      handleChannel( (ServerSocketChannel) key.channel() );
   }

   /**
    * Handle a connection attempt on specific channel.
    *
    * @param channel the channel
    */
   void handleChannel( final ServerSocketChannel channel )
   {
      final ServerSocket serverSocket = channel.socket();
      final NIOAcceptorEntry entry =
         (NIOAcceptorEntry) m_socket2Entry.get( serverSocket );
      if ( null == entry )
      {
         //The acceptor must have been disconnected
         //so we just ignore this and assume it wont happen
         //again
         return;
      }

      final String name = entry.getConfig().getName();
      final SocketConnectionHandler handler = entry.getConfig().getHandler();
      try
      {
         final Socket socket = channel.accept().socket();
         getMonitor().handlingConnection( name, serverSocket, socket );
         handler.handleConnection( socket );
      }
      catch ( final IOException ioe )
      {
         getMonitor().errorAcceptingConnection( name, ioe );
      }
   }
}
