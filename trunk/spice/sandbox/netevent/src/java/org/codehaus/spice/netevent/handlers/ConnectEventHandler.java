/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netevent.handlers;

import java.io.IOException;
import java.nio.channels.Channel;
import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.event.impl.collections.UnboundedFifoBuffer;
import org.codehaus.spice.netevent.events.ConnectErrorEvent;
import org.codehaus.spice.netevent.events.ConnectEvent;
import org.codehaus.spice.netevent.selector.SocketEventSource;
import org.codehaus.spice.netevent.transport.ChannelTransport;

/**
 * Handler for Connecting a socket (which means creating a transport and
 * registering it for events).
 * 
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-01-08 03:41:14 $
 */
public class ConnectEventHandler
    extends AbstractIOEventHandler
{
    /** Handler to pass events on to. */
    private final SocketEventSource _source;

    /**
     * Create handler with specified destination sink.
     * 
     * @param sink the destination
     */
    public ConnectEventHandler( final EventSink sink,
                                final SocketEventSource source )
    {
        super( sink );
        _source = source;
    }

    /**
     * @see EventHandler#handleEvent(Object)
     */
    public void handleEvent( final Object event )
    {
        final ConnectEvent ce = (ConnectEvent)event;
        final Channel channel = ce.getChannel();
        final ChannelTransport transport =
            new ChannelTransport( channel,
                                  new UnboundedFifoBuffer( 4 ),
                                  new UnboundedFifoBuffer( 4 ) );
        try
        {
            transport.register( _source );
        }
        catch( final IOException ioe )
        {
            final ConnectErrorEvent error =
                new ConnectErrorEvent( transport, ioe );
            getSink().addEvent( error );
        }
    }
}
