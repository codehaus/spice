/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.impl;

import org.jcontainer.dna.AbstractLogEnabled;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

/**
 * Implementation of ConnectionMonitor which logs event with the DNA logger.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-10-08 07:40:45 $
 */
class DNAConnectionMonitor
    extends AbstractLogEnabled
    implements ConnectionMonitor
{
    public void acceptorCreated( final String name,
                                 final ServerSocket serverSocket )
    {
        if( getLogger().isInfoEnabled() )
        {
            final String message =
                "Creating Acceptor " + name + " on " +
                serverSocket.getInetAddress().getHostAddress() + ":" +
                serverSocket.getLocalPort() + ".";
            getLogger().info( message );
        }
    }

    public void acceptorClosing( final String name,
                                 final ServerSocket serverSocket )
    {
        if( getLogger().isInfoEnabled() )
        {
            final String message = "Closing Acceptor " + name + ".";
            getLogger().info( message );
        }
    }

    public void serverSocketListening( final String name,
                                       final ServerSocket serverSocket )
    {
        if( getLogger().isDebugEnabled() )
        {
            final String message =
                "About to call accept() on ServerSocket '" + name + "'.";
            getLogger().debug( message );
        }
    }

    public void errorAcceptingConnection( final String name,
                                          final IOException ioe )
    {
        if( getLogger().isWarnEnabled() )
        {
            getLogger().warn( "Error Accepting connection on " + name, ioe );
        }
    }

    public void errorClosingServerSocket( final String name,
                                          final IOException ioe )
    {
        if( getLogger().isWarnEnabled() )
        {
            getLogger().warn( "Error Closing Server Socket " + name, ioe );
        }
    }

    public void acceptorDisconnecting( final String name,
                                       final boolean tearDown )
    {
        if( getLogger().isInfoEnabled() )
        {
            final String message =
                "Disconnecting Acceptor " + name + ". tearDown=" + tearDown;
            getLogger().info( message );
        }
    }

    public void connectionEnding( final String name, final Socket socket )
    {
        if( getLogger().isDebugEnabled() )
        {
            final String message =
                "Ending connection '" + name + "' on " +
                socket.getInetAddress().getHostAddress() + ".";
            getLogger().debug( message );
        }
    }

    public void connectionStarting( final String name, final Socket socket )
    {
        if( getLogger().isDebugEnabled() )
        {
            final String message =
                "Starting connection '" + name + "' on " +
                socket.getInetAddress().getHostAddress() + ".";
            getLogger().debug( message );
        }
    }

    public void errorHandlerAlreadyDisposed( final String name,
                                             final Socket socket )
    {
        if( getLogger().isWarnEnabled() )
        {
            final String message = "Attempting to dispose runner " +
                name + " that has already been disposed.";
            getLogger().warn( message );
        }
    }

    public void disposingHandler( final String name,
                                 final Socket socket )
    {
        if( getLogger().isDebugEnabled() )
        {
            final String message = "Disposing handler " + name + ".";
            getLogger().debug( message );
        }
    }

    public void errorClosingSocket( final String name,
                                    final Socket socket,
                                    final Exception e )
    {
        if( getLogger().isWarnEnabled() )
        {
            getLogger().warn( "Error closing socket " + socket +
                              " for " + name, e );
        }
    }

    public void errorAquiringHandler( final String name,
                                      final Exception e )
    {
        if( getLogger().isWarnEnabled() )
        {
            getLogger().warn( "Error Aquiring SocketHandler for " + name, e );
        }
    }

    public void errorHandlingConnection( final String name,
                                         final Socket socket,
                                         final Exception e )
    {
        if( getLogger().isWarnEnabled() )
        {
            getLogger().warn( "Error Handling connection for " + name +
                              " on " + socket, e );
        }
    }
}
