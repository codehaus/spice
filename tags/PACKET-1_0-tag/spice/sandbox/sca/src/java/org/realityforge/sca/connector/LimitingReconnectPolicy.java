/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.sca.connector;

/**
 * A Policy that will always attempt to limit the number of connection attempts
 * in a period of time.
 *
 * <p>After N connection attempts it will force a delay of T between successive
 * connection attempts. This is an attempt not to overload the resource being
 * connected to.</p>
 *
 * @author Peter Donald
 * @version $Revision: 1.4 $ $Date: 2003-12-05 05:39:33 $
 */
public class LimitingReconnectPolicy
    implements ReconnectionPolicy
{
    /** The number of attempts allowed before delay will kick in. */
    private final int m_attempts;

    /** The delay between successive connection attempts. */
    private final long m_delay;

    /**
     * Create a policy instance.
     *
     * @param attempts the number of attempts before the delay is enabled.
     * @param delay the delay
     */
    public LimitingReconnectPolicy( final int attempts,
                                    final long delay )
    {
        m_attempts = attempts;
        m_delay = delay;
    }

    /**
     * @see ReconnectionPolicy#attemptConnection(long, int)
     */
    public boolean attemptConnection( final long lastConnectionAttempt,
                                      final int connectionAttempts )
    {
        if( connectionAttempts >= m_attempts )
        {
            final long now = System.currentTimeMillis();
            final long nextAttempt = lastConnectionAttempt + m_delay;
            if( now < nextAttempt )
            {
                return false;
            }
        }
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
