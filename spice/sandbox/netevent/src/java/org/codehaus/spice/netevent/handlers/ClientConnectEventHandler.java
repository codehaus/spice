/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netevent.handlers;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.event.impl.collections.UnboundedFifoBuffer;
import org.codehaus.spice.netevent.buffers.BufferManager;
import org.codehaus.spice.netevent.events.ConnectErrorEvent;
import org.codehaus.spice.netevent.events.ConnectEvent;
import org.codehaus.spice.netevent.events.ConnectPossibleEvent;
import org.codehaus.spice.netevent.selector.SocketEventSource;
import org.codehaus.spice.netevent.transport.ChannelTransport;
import org.realityforge.packet.session.Session;

/**
 * Handler for Connecting a socket (which means creating a transport and
 * registering it for events).
 * 
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-01-18 22:44:25 $
 */
public class ClientConnectEventHandler
    extends AbstractIOEventHandler
{
    /** Handler to pass events on to. */
    private final SocketEventSource _source;

    /** Handler to pass high-level events on to. */
    private final EventSink _target;

    /**
     * Create handler with specified destination sink.
     * 
     * @param sink the destination
     * @param bufferManager the bufferManager
     * @param source the source
     */
    public ClientConnectEventHandler( final EventSink sink,
                                      final EventSink target,
                                      final BufferManager bufferManager,
                                      final SocketEventSource source )
    {
        super( sink, bufferManager );
        if( null == target )
        {
            throw new NullPointerException( "target" );
        }
        if( null == source )
        {
            throw new NullPointerException( "source" );
        }
        _target = target;
        _source = source;
    }

    /**
     * @see EventHandler#handleEvent(Object)
     */
    public void handleEvent( final Object event )
    {
        final ConnectPossibleEvent ce = (ConnectPossibleEvent)event;
        final SocketChannel channel = (SocketChannel)ce.getChannel();
        if( !channel.isConnectionPending() )
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
            final Object userData = ce.getUserData();
            transport.setUserData( userData );
            if( userData instanceof Session )
            {
                final Session session = (Session)userData;
                session.setTransport( transport );
            }

            channel.finishConnect();
            transport.register( _source );
            final ConnectEvent response = new ConnectEvent( transport );
            _target.addEvent( response );
        }
        catch( final IOException ioe )
        {
            final ConnectErrorEvent error =
                new ConnectErrorEvent( transport, ioe );
            getSink().addEvent( error );
        }
    }
}
