/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.message;

import org.d_haven.event.EventHandler;

/**
 * EventHandler that passes messages on to a Destination
 *
 * @author <a href="mailto:peter.royal@pobox.com">peter royal</a>
 */
class DestinationEventHandler implements EventHandler
{
    private final Destination m_destination;
    private final DestinationMonitor m_monitor;

    public DestinationEventHandler( final Destination destination, final DestinationMonitor monitor )
    {
        m_destination = destination;
        m_monitor = monitor;
    }

    public void handleEvent( final Object element )
    {
        try
        {
            m_destination.deliver( element );
        }
        catch( Exception e )
        {
            m_monitor.deliveryFailed( element, m_destination, e );
        }
    }

    public void handleEvents( final Object[] elements )
    {
        for( int i = 0; i < elements.length; i++ )
        {
            handleEvent( elements[i] );
        }
    }
}