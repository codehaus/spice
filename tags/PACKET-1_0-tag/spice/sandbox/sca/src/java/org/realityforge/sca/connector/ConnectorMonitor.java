/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.sca.connector;

/**
 * The ConnectorMonitor gets notified of events in Connector.
 *
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003-12-05 05:39:33 $
 */
public interface ConnectorMonitor
{
    /**
     * Notify that connection attempt about to start.
     */
    void attemptingConnection();

    /**
     * Notify monitor that connection has been established.
     */
    void connectionEstablished();

    /**
     * Notify monitor that there was an error connecting.
     *
     * @param t the error
     */
    void errorConnecting( Throwable t );

    /**
     * Notify monitor that there was an error disconnecting.
     *
     * @param t the error
     */
    void errorDisconnecting( Throwable t );

    /**
     * Notify monito that attempting to validate connection.
     */
    void attemptingValidation();

    /**
     * Notify monitor that there was an error validating connection. After this
     * method is called the connection will be disconnected.
     */
    void errorValidatingConnection( Throwable t );

    /**
     * Notify that the policy has indicated that a connection attempt should not
     * be made at this point in time.
     */
    void skippingConnectionAttempt();

    /**
     * Notify monitor that Connection is being disconnected.
     */
    void attemptingDisconnection();
}
