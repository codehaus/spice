/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.spice.message;

/**
 * @author <a href="mailto:peter.royal@pobox.com">peter royal</a>
 */
public class NoSuchDestinationException extends MessageException
{
    private final Destination m_destination;

    public NoSuchDestinationException( final Destination destination )
    {
        super( "Unknown destination address '" + destination.getAddress() + "'" );
        m_destination = destination;
    }

    public Destination getDestination()
    {
        return m_destination;
    }
}