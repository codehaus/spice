/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.message;

/**
 * A Messenger allows clients to send messages to specific named addresses.
 *
 * If a message is sent to an unknown address, it will merely end up in the discarded message box. The sender will not
 * be informed.
 *
 * @author <a href="mailto:peter.royal@pobox.com">peter royal</a>
 */
public interface Dispatcher
{
    /**
     * Send a message to an address. Delivery is guaranteed if the destintion exists. If the destination does not exist,
     * no notification will occur.
     *
     * @param address Address to send message to. Required.
     * @param message Message to send. Required.
     *
     * @throws InvalidMessageException if a message is invalid
     */
    void send( String address, Object message );
}