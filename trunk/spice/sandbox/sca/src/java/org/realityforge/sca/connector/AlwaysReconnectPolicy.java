/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.sca.connector;

/**
 * A Policy that will always attempt to reconnect regardless of how many
 * failures or errors.
 *
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003-12-05 05:39:33 $
 */
public class AlwaysReconnectPolicy
    implements ReconnectionPolicy
{
    /** Constant containing instance of policy. */
    public static final AlwaysReconnectPolicy POLICY = new AlwaysReconnectPolicy();

    /**
     * @see ReconnectionPolicy#attemptConnection(long, int)
     */
    public boolean attemptConnection( final long lastConnectionAttempt,
                                      final int connectionAttempts )
    {
        return true;
    }

    /**
     * @see ReconnectionPolicy#disconnectOnError(Throwable)
     */
    public boolean disconnectOnError( final Throwable t )
    {
        return true;
    }

    /**
     * @see ReconnectionPolicy#reconnectOnDisconnect()
     */
    public boolean reconnectOnDisconnect()
    {
        return true;
    }
}
