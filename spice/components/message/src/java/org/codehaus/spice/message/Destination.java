/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.message;

/**
 * A Message Destination receives messages for a named destination.
 *
 * Message objects will be specific to the destination implementation. Please consult destination documentation on what
 * constitutes valid messages.
 *
 * @author <a href="mailto:peter.royal@pobox.com">peter royal</a>
 */
public interface Destination
{
    /**
     * Get the address of this destination.
     *
     * @return Address of destination. Required.
     */
    String getAddress();

    /**
     * Is the specified message a valid message for this destination? This method is just to check message validity. All
     * valid messages must be delivered without throwing any exceptions.
     *
     * @param message Message object to validate. Required.
     *
     * @return True if the message is valid for this destination.
     */
    boolean isValidMessage( Object message );

    /**
     * Deliver a message. Messages should first be tested for validity with {@link #isValidMessage(Object)}.
     *
     * @param message Message to deliver.
     */
    void deliver( Object message );
}