/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.spice.event;

import java.util.EventObject;

import org.drools.FactException;

/**
 * @author <a href="mailto:peter.royal@pobox.com">peter royal</a>
 */
public class PublicationException extends RuntimeException
{
    private final EventObject m_event;

    public PublicationException( final EventObject event, final FactException e )
    {
        super( "Unable to publish event '" + event + "' due to " + e.getMessage(), e );

        m_event = event;
    }

    public EventObject getEvent()
    {
        return m_event;
    }
}