/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.impl;

/**
 * Implementation of ConnectionMonitor which logs events to console.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
class ConsoleConnectionMonitor
    implements ConnectionMonitor
{
    private boolean m_logEnabled;
    
    public ConsoleConnectionMonitor()
    {
        this( true );
    }

    public ConsoleConnectionMonitor( final boolean logEnabled )
    {
        m_logEnabled = logEnabled;
    }

    public void acceptorClosed( final String name )
    {
        if( m_logEnabled )
        {
            final String message = "Closing Acceptor " + name + ".";
            System.out.println( message );
        }
    }

    public void acceptorCreated( final ConnectionAcceptor acceptor )
    {
        if( m_logEnabled )
        {
            final String message = "Creating Acceptor " + acceptor + ".";
            System.out.println( message );
        }
    }

    public void acceptorDisconnected( final ConnectionAcceptor acceptor,
                                      boolean tearDown )
    {
        if( m_logEnabled )
        {
            final String message =
                "Disconnecting Acceptor " + acceptor + ". tearDown=" + tearDown;
            System.out.println( message );
        }
    }

    public void acceptorStopping( final String name )
    {
        if( m_logEnabled )
        {
            final String message = "Stopping Acceptor " + name + ".";
            System.out.println( message );
        }
    }

    public void connectionEnding( final String name, final String hostAddress )
    {
        if( m_logEnabled )
        {
            final String message =
                "Ending connection '" + name + "' on " + hostAddress + ".";
            System.out.println( message );
        }
    }

    public void connectionStarting( final String name, final String hostAddress )
    {
        if( m_logEnabled )
        {
            final String message =
                "Starting connection '" + name + "' on " + hostAddress + ".";
            System.out.println( message );
        }
    }

    public void runnerAlreadyDisposed( final ConnectionRunner runner )
    {
        if( m_logEnabled )
        {
            final String message = "Attempting to dispose runner " +
                runner + " that has already been disposed.";
            System.out.println( message );
        }
    }

    public void runnerCreating( final String name )
    {
        if( m_logEnabled )
        {
            final String message = "Creating ConnectionRunner " + name + ".";
            System.out.println( message );
        }
    }

    public void runnerDisposing( final ConnectionRunner runner )
    {
        if( m_logEnabled )
        {
            final String message = "Disposing runner " + runner + ".";
            System.out.println( message );
        }
    }

    public void serverSocketClosing( final String name )
    {
        if( m_logEnabled )
        {
            final String message = "Closing ServerSocket " + name + ".";
            System.out.println( message );
        }
    }

    public void serverSocketListening( final String name )
    {
        if( m_logEnabled )
        {
            final String message =
                "About to call accept() on ServerSocket '" + name + "'.";
            System.out.println( message );
        }
    }

    public void unexpectedError( final String message,
                                 final Throwable t )
    {
        if( m_logEnabled )
        {
            System.out.println( "Unexpected Error (" + message + ")");
            t.printStackTrace();
        }
    }
}
