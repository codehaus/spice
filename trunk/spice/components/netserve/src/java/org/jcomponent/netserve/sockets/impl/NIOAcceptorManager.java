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
 * @version $Revision: 1.18 $ $Date: 2003-10-23 04:24:30 $
 * @dna.component
 * @dna.service type="SocketAcceptorManager"
 */
public class NIOAcceptorManager
   extends AbstractNIOReactor
   implements SocketAcceptorManager
{
   /**
    * The map of name->SelectorKey.
    */
   private final Map m_acceptors = new Hashtable();

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

         final AcceptorConfig config =
            new AcceptorConfig( name, socket, handler );
         key.attach( config );
         m_acceptors.put( name, key );
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
      final SelectionKey key =
         (SelectionKey) m_acceptors.remove( name );
      if ( null == key )
      {
         final String message = "No connection with name " + name;
         throw new IllegalArgumentException( message );
      }
      key.cancel();
      getMonitor().acceptorClosing( name );
   }

   /**
    * @see AbstractNIOReactor#handleChannel
    */
   protected void handleChannel( final SelectionKey key )
   {
      final ServerSocketChannel channel =
         (ServerSocketChannel) key.channel();
      final ServerSocket serverSocket = channel.socket();
      final AcceptorConfig config =
         (AcceptorConfig) key.attachment();
      if ( null == config )
      {
         //The acceptor must have been disconnected
         //so we just ignore this and assume it wont happen
         //again
         return;
      }

      final String name = config.getName();
      final SocketConnectionHandler handler = config.getHandler();
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
