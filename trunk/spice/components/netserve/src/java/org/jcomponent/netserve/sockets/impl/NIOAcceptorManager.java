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
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.jcomponent.netserve.sockets.SocketAcceptorManager;
import org.jcomponent.netserve.sockets.SocketConnectionHandler;

/**
 * Abstract implementation of {@link SocketAcceptorManager} that NIO
 * to monitor several server sockets.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-09 06:46:42 $
 * @dna.component
 * @dna.service type="SocketAcceptorManager"
 */
public class NIOAcceptorManager
    implements SocketAcceptorManager, Runnable
{
    /**
     * The monitor that receives notifications of Connection events
     */
    private AcceptorMonitor m_monitor = NullAcceptorMonitor.MONITOR;

    /**
     * The map of name->NIOAcceptorEntry.
     */
    private final Map m_acceptors = new Hashtable();

    /**
     * The map of socket->NIOAcceptorEntry.
     */
    private final Map m_socket2Entry = new Hashtable();

    /**
     * Selector used to monitor for accepts.
     */
    private Selector m_selector;

    /**
     * Flag indicating whether manager is running.
     */
    private boolean m_running;

    /**
     * Initialize the selector to monitor accept attempts.
     *
     * @throws IOException if unable to initialize selector
     */
    public void startupSelector()
        throws IOException
    {
        synchronized( this )
        {
            m_selector = Selector.open();
            m_running = true;
        }
        startThread();
    }

    /**
     * Start the thread to accept connections.
     */
    protected void startThread()
    {
        final Thread thread = new Thread( this, "NIOAcceptorManager" );
        thread.start();
    }

    /**
     * Shutdown the selector and any associated acceptors.
     */
    public void shutdownSelector()
    {
        synchronized( this )
        {
            m_running = false;
            shutdownAcceptors();
            if( null != m_selector )
            {
                try
                {
                    m_selector.close();
                }
                catch( IOException ioe )
                {
                    //TODO: notify monitor
                }
            }
        }
        while( null != m_selector )
        {
            synchronized( this )
            {
                try
                {
                    wait( 100 );
                }
                catch( InterruptedException e )
                {
                    //Ignore
                }
            }
        }
    }

    /**
     * Dispose the ConnectionManager which involves shutting down all
     * the connected acceptors.
     */
    protected synchronized void shutdownAcceptors()
    {
        final String[] names;
        synchronized( m_acceptors )
        {
            names = (String[])m_acceptors.keySet().toArray( new String[ 0 ] );
        }
        for( int i = 0; i < names.length; i++ )
        {
            disconnect( names[ i ] );
        }
    }

    /**
     * Start accepting connections from a socket and passing connections
     * to specified handler.
     *
     * @param name the name of connection. This serves as a key used to
     *        shutdown acceptor.
     * @param socket the ServerSocket from which connections are accepted
     * @throws Exception if unable to initiate connection management. This could
     *         be due to the key already being used for another acceptor,
     *        the serversocket being closed, the handler being null etc.
     */
    public void connect( final String name,
                         final ServerSocket socket,
                         final SocketConnectionHandler handler )
        throws Exception
    {
        if( null == name )
        {
            throw new NullPointerException( "name" );
        }
        if( null == socket )
        {
            throw new NullPointerException( "socket" );
        }
        if( null == handler )
        {
            throw new NullPointerException( "handler" );
        }

        synchronized( m_acceptors )
        {
            if( isConnected( name ) )
            {
                final String message =
                    "Connection already exists with name " + name;
                throw new IllegalArgumentException( message );
            }

            final ServerSocketChannel channel = socket.getChannel();
            channel.configureBlocking( false );
            final SelectionKey key =
                channel.register( m_selector, SelectionKey.OP_ACCEPT );

            final AcceptorConfig acceptor =
                new AcceptorConfig( name, socket, handler );
            final NIOAcceptorEntry entry =
                new NIOAcceptorEntry( acceptor, key );
            m_acceptors.put( name, entry );
            m_socket2Entry.put( socket, entry );
        }
    }

    /**
     * Return true if acceptor with specified name exists.
     *
     * @param name the name
     * @return true if acceptor with specified name exists.
     */
    public boolean isConnected( final String name )
    {
        return m_acceptors.containsKey( name );
    }

    /**
     * This shuts down the acceptor and the associated ServerSocket.
     *
     * @param name the name of connection
     * @throws IllegalArgumentException if no connection with specified name
     */
    public void disconnect( final String name )
    {
        final NIOAcceptorEntry entry =
            (NIOAcceptorEntry)m_acceptors.remove( name );
        if( null == entry )
        {
            final String message = "No connection with name " + name;
            throw new IllegalArgumentException( message );
        }
        m_socket2Entry.remove( entry.getConfig().getServerSocket() );
        entry.getKey().cancel();
    }

    /**
     * Return true if the selector is manager is running.
     *
     * @return true if the selector is manager is running.
     */
    public synchronized boolean isRunning()
    {
        return m_running;
    }

    /**
     * This is the main connection accepting loop.
     */
    public void run()
    {
        // Here's where everything happens. The select method will
        // return when any operations registered above have occurred, the
        // thread has been interrupted, etc.
        while( isRunning() )
        {
            // Someone is ready for I/O, get the ready keys
            final Set keys = m_selector.selectedKeys();
            final Iterator iterator = keys.iterator();

            // Walk through the ready keys collection and process date requests.
            while( iterator.hasNext() )
            {
                final SelectionKey key = (SelectionKey)iterator.next();
                iterator.remove();
                // The key indexes into the selector so you
                // can retrieve the socket that's ready for I/O

                final ServerSocketChannel channel = (ServerSocketChannel)key.channel();
                handleChannel( channel );
            }
        }
        m_selector = null;
        synchronized( this )
        {
            notifyAll();
        }
    }

    /**
     * Handle a connection attempt on specific channel.
     *
     * @param channel the channel
     */
    private void handleChannel( final ServerSocketChannel channel )
    {
        final NIOAcceptorEntry entry =
            (NIOAcceptorEntry)m_socket2Entry.get( channel.socket() );
        if( null == entry )
        {
            //The acceptor must have been disconnected
            //so we just ignore this and assume it wont happen
            //again
            return;
        }

        final SocketConnectionHandler handler = entry.getConfig().getHandler();
        try
        {
            final Socket socket = channel.accept().socket();
            handler.handleConnection( socket );
        }
        catch( final IOException ioe )
        {
            final String name = entry.getConfig().getName();
            m_monitor.errorAcceptingConnection( name, ioe );
        }
    }
}
