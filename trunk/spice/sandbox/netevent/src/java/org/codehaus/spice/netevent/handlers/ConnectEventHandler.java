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
import org.codehaus.spice.netevent.buffers.BufferManager;
import org.codehaus.spice.netevent.events.AcceptEvent;
import org.codehaus.spice.netevent.events.ConnectErrorEvent;
import org.codehaus.spice.netevent.events.ConnectEvent;
import org.codehaus.spice.netevent.selector.SocketEventSource;
import org.codehaus.spice.netevent.transport.ChannelTransport;

/**
 * Handler for Connecting a socket (which means creating a transport and
 * registering it for events).
 * 
 * @author Peter Donald
 * @version $Revision: 1.7 $ $Date: 2004-01-12 04:58:14 $
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
     * @param bufferManager the bufferManager
     * @param source the source
     */
    public ConnectEventHandler( final EventSink sink,
                                final BufferManager bufferManager,
                                final SocketEventSource source )
    {
        super( sink, bufferManager );
        if( null == source )
        {
            throw new NullPointerException( "source" );
        }
        _source = source;
    }

    /**
     * @see EventHandler#handleEvent(Object)
     */
    public void handleEvent( final Object event )
    {
        final AcceptEvent ce = (AcceptEvent)event;
        final Channel channel = ce.getChannel();
        if( !channel.isOpen() )
        {
            return;
        }

        final ChannelTransport transport =
            new ChannelTransport( channel,
                                  new UnboundedFifoBuffer( 4 ),
                                  getBufferManager(),
                                  getSink() );
        try
        {
            transport.register( _source );
            final ConnectEvent response = new ConnectEvent( transport );
            getSink().addEvent( response );
        }
        catch( final IOException ioe )
        {
            final ConnectErrorEvent error =
                new ConnectErrorEvent( transport, ioe );
            getSink().addEvent( error );
        }
    }
}