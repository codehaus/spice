/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.sca.connector;

/**
 * A null monitor that consumes all messages.
 *
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003-12-05 05:39:33 $
 */
public class NullMonitor
    implements ConnectorMonitor
{
    /** A constant containing instance of null monitor. */
    public static final NullMonitor MONITOR = new NullMonitor();

    /**
     * @see ConnectorMonitor#attemptingConnection()
     */
    public void attemptingConnection()
    {
    }

    /**
     * @see ConnectorMonitor#connectionEstablished()
     */
    public void connectionEstablished()
    {
    }

    /**
     * @see ConnectorMonitor#errorConnecting(Throwable)
     */
    public void errorConnecting( final Throwable t )
    {
    }

    /**
     * @see ConnectorMonitor#errorDisconnecting(Throwable)
     */
    public void errorDisconnecting( final Throwable t )
    {
    }

    /**
     * @see ConnectorMonitor#attemptingValidation()
     */
    public void attemptingValidation()
    {
    }

    /**
     * @see ConnectorMonitor#errorValidatingConnection(Throwable)
     */
    public void errorValidatingConnection( final Throwable t )
    {
    }

    /**
     * @see ConnectorMonitor#skippingConnectionAttempt()
     */
    public void skippingConnectionAttempt()
    {
    }

    /**
     * @see ConnectorMonitor#attemptingDisconnection()
     */
    public void attemptingDisconnection()
    {
    }
}
