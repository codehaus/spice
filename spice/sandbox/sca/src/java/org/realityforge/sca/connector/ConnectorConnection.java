/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.sca.connector;

/**
 * Add in interface representing Connection managed by Connector.
 *
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003-12-05 05:39:33 $
 */
public interface ConnectorConnection
{
    /**
     * Establish the connection.
     *
     * @throws Exception if unable to connect
     */
    void doConnect()
        throws Exception;

    /**
     * Disconnect the connection.
     *
     * @throws Exception if unable to connect
     */
    void doDisconnect()
        throws Exception;

    /**
     * Validate the connection. The validation should involve explicitly testing
     * that that the connection is valid.
     *
     * @throws Exception if unable to connection is not valid
     */
    void doValidateConnection()
        throws Exception;
}
