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
import org.codehaus.spice.netevent.transport.ChannelTransport;

/**
 * Handler for Connecting a socket (which means creating a transport and
 * registering it for events).
 * 
 * @author Peter Donald
 * @version $Revision: 1.10 $ $Date: 2004-02-11 02:56:00 $
 */
public class ClientConnectEventHandler
    extends AbstractIOEventHandler
{
    /**
     * The destination for events relating to next layer.
     */
    private EventSink _target;

    /**
     * Create handler with specified destination sink.
     * 
     * @param sink the destination
     * @param bufferManager the bufferManager
     */
    public ClientConnectEventHandler( final EventSink sink,
                                      final EventSink target,
                                      final BufferManager bufferManager )
    {
        super( sink, bufferManager );
        if( null == target )
        {
            throw new NullPointerException( "target" );
        }
        _target = target;
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
        transport.setUserData( ce.getUserData() );
        transport.setKey( ce.getKey() );
        ce.getKey().attach( transport );
        try
        {
            channel.finishConnect();
            final ConnectEvent response = new ConnectEvent( transport );
            _target.addEvent( response );
            transport.reregister();
        }
        catch( final IOException ioe )
        {
            transport.close();
            final ConnectErrorEvent error =
                new ConnectErrorEvent( transport, ioe );
            _target.addEvent( error );
        }
    }
}
