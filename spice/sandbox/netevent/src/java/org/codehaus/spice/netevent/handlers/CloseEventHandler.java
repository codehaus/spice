/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netevent.handlers;

import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.events.AbstractTransportEvent;
import org.codehaus.spice.netevent.events.ChannelClosedEvent;
import org.codehaus.spice.netevent.transport.ChannelTransport;

/**
 * Simple handler that closes underlying transport.
 * 
 * @author Peter Donald
 * @version $Revision: 1.4 $ $Date: 2004-05-17 06:21:38 $
 */
public class CloseEventHandler
    extends AbstractDirectedHandler
{
    /**
     * Create handler with specified target.
     * 
     * @param sink the destination sink
     */
    public CloseEventHandler( final EventSink sink )
    {
        super( sink );
    }

    /**
     * @see EventHandler#handleEvent(Object)
     */
    public void handleEvent( final Object event )
    {
        final AbstractTransportEvent ce = (AbstractTransportEvent) event;
        final ChannelTransport transport = ce.getTransport();
        if( !transport.isClosed() )
        {
            transport.close();
            final ChannelClosedEvent result = new ChannelClosedEvent( transport );
            getSink().addEvent( result );
        }
    }
}
