/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.sca.connector;

/**
 * A debug monitor that prints all messages to standard out.
 *
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003-12-05 05:39:34 $
 */
class DebugMonitor
    extends NullMonitor
    implements ConnectorMonitor
{
    /** A constant containing instance of null monitor. */
    public static final DebugMonitor MONITOR = new DebugMonitor();

    /**
     * @see ConnectorMonitor#attemptingConnection
     */
    public void attemptingConnection()
    {
        System.out.println( "attemptingConnection" );
        super.attemptingConnection();
    }

    /**
     * @see ConnectorMonitor#connectionEstablished
     */
    public void connectionEstablished()
    {
        System.out.println( "connectionEstablished" );
        super.connectionEstablished();
    }

    /**
     * @see ConnectorMonitor#errorConnecting
     */
    public void errorConnecting( final Throwable t )
    {
        System.out.println( "errorConnecting(" + t + ")" );
        super.errorConnecting( t );
    }

    /**
     * @see ConnectorMonitor#errorDisconnecting
     */
    public void errorDisconnecting( final Throwable t )
    {
        System.out.println( "errorDisconnecting(" + t + ")" );
        super.errorDisconnecting( t );
    }

    /**
     * @see ConnectorMonitor#attemptingValidation
     */
    public void attemptingValidation()
    {
        System.out.println( "attemptingValidation" );
        super.attemptingValidation();
    }

    /**
     * @see ConnectorMonitor#errorValidatingConnection
     */
    public void errorValidatingConnection( final Throwable t )
    {
        System.out.println( "errorValidatingConnection(" + t + ")" );
        super.errorValidatingConnection( t );
    }

    /**
     * @see ConnectorMonitor#skippingConnectionAttempt
     */
    public void skippingConnectionAttempt()
    {
        System.out.println( "skippingConnectionAttempt" );
        super.skippingConnectionAttempt();
    }
}
