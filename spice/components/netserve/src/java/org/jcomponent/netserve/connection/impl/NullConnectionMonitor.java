/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.impl;

/**
 * Noop implementation of ConnectionMonitor.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
class NullConnectionMonitor
    implements ConnectionMonitor
{
    public void acceptorClosed( final String name )
    {
    }

    public void acceptorCreated( final ConnectionAcceptor acceptor )
    {
    }

    public void acceptorDisconnected( final ConnectionAcceptor acceptor,
                                      boolean tearDown )
    {
    }

    public void acceptorStopping( final String name )
    {
    }

    public void connectionEnding( final String name, final String hostAddress )
    {
    }

    public void connectionStarting( final String name, final String hostAddress )
    {
    }

    public void runnerAlreadyDisposed( final ConnectionRunner runner )
    {
    }

    public void runnerCreating( final String name )
    {
    }

    public void runnerDisposing( final ConnectionRunner runner )
    {
    }

    public void serverSocketClosing( final String name )
    {
    }

    public void serverSocketListening( final String name )
    {
    }

    public void unexpectedError( final String message,
                                 final Throwable t )
    {
    }
}
