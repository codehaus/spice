/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.impl;

import org.apache.avalon.framework.logger.AbstractLogEnabled;

/**
 * Implementation of ConnectionMonitor which logs event with the Avalon logger.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
class AvalonConnectionMonitor
    extends AbstractLogEnabled
    implements ConnectionMonitor
{
    public void acceptorClosed( final String name )
    {
        if( getLogger().isInfoEnabled() )
        {
            final String message = "Closing Acceptor " + name + ".";
            getLogger().info( message );
        }
    }

    public void acceptorCreated( final ConnectionAcceptor acceptor )
    {
        if( getLogger().isInfoEnabled() )
        {
            final String message = "Creating Acceptor " + acceptor + ".";
            getLogger().info( message );
        }
    }

    public void acceptorDisconnected( final ConnectionAcceptor acceptor,
                                      boolean tearDown )
    {
        if( getLogger().isInfoEnabled() )
        {
            final String message =
                "Disconnecting Acceptor " + acceptor + ". tearDown=" + tearDown;
            getLogger().info( message );
        }
    }

    public void acceptorStopping( final String name )
    {
        if( getLogger().isDebugEnabled() )
        {
            final String message = "Stopping Acceptor " + name + ".";
            getLogger().debug( message );
        }
    }

    public void connectionEnding( final String name, final String hostAddress )
    {
        if( getLogger().isDebugEnabled() )
        {
            final String message =
                "Ending connection '" + name + "' on " + hostAddress + ".";
            getLogger().debug( message );
        }
    }

    public void connectionStarting( final String name, final String hostAddress )
    {
        if( getLogger().isDebugEnabled() )
        {
            final String message =
                "Starting connection '" + name + "' on " + hostAddress + ".";
            getLogger().debug( message );
        }
    }

    public void runnerAlreadyDisposed( final ConnectionRunner runner )
    {
        if( getLogger().isWarnEnabled() )
        {
            final String message = "Attempting to dispose runner " +
                runner + " that has already been disposed.";
            getLogger().warn( message );
        }
    }

    public void runnerCreating( final String name )
    {
        if( getLogger().isDebugEnabled() )
        {
            final String message = "Creating ConnectionRunner " + name + ".";
            getLogger().debug( message );
        }
    }

    public void runnerDisposing( final ConnectionRunner runner )
    {
        if( getLogger().isDebugEnabled() )
        {
            final String message = "Disposing runner " + runner + ".";
            getLogger().debug( message );
        }
    }

    public void serverSocketClosing( final String name )
    {
        if( getLogger().isDebugEnabled() )
        {
            final String message = "Closing ServerSocket " + name + ".";
            getLogger().debug( message );
        }
    }

    public void serverSocketListening( final String name )
    {
        if( getLogger().isDebugEnabled() )
        {
            final String message =
                "About to call accept() on ServerSocket '" + name + "'.";
            getLogger().debug( message );
        }
    }

    public void unexpectedError( final String message,
                                 final Throwable t )
    {
        if( getLogger().isWarnEnabled() )
        {
            getLogger().warn( "Unexpected Error (" + message + ")", t );
        }
    }
}
