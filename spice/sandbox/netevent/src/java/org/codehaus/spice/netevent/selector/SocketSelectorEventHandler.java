/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netevent.selector;

import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.events.AcceptPossibleEvent;
import org.codehaus.spice.netevent.events.ConnectPossibleEvent;
import org.codehaus.spice.netevent.events.ReadPossibleEvent;
import org.codehaus.spice.netevent.events.WritePossibleEvent;
import org.codehaus.spice.netevent.transport.ChannelTransport;
import org.realityforge.sca.selector.SelectorEventHandler;

/**
 * The handler receives read and write events for channels and generates
 * corresponding events to pass onto an EventSink.
 * 
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2004-01-16 00:22:03 $
 */
public class SocketSelectorEventHandler
    implements SelectorEventHandler
{
    private final EventSink _sink;

    /**
     * Create handler that sends events to specified sink.
     * 
     * @param sink the sink.
     */
    public SocketSelectorEventHandler( final EventSink sink )
    {
        if( null == sink )
        {
            throw new NullPointerException( "sink" );
        }
        _sink = sink;
    }

    /**
     * @see SelectorEventHandler#handleSelectorEvent(SelectionKey, Object)
     */
    public void handleSelectorEvent( final SelectionKey key,
                                     final Object userData )
    {
        if( key.isAcceptable() )
        {
            final ServerSocketChannel channel =
                (ServerSocketChannel)key.channel();
            final AcceptPossibleEvent event =
                new AcceptPossibleEvent( channel, userData );
            _sink.addEvent( event );
        }
        if( key.isWritable() )
        {
            final ChannelTransport transport = (ChannelTransport)userData;
            final WritePossibleEvent event =
                new WritePossibleEvent( transport );
            _sink.addEvent( event );
        }
        if( key.isReadable() )
        {
            final ChannelTransport transport = (ChannelTransport)userData;
            final ReadPossibleEvent event =
                new ReadPossibleEvent( transport );
            _sink.addEvent( event );
        }
        if( key.isConnectable() )
        {
            final ConnectPossibleEvent event =
                new ConnectPossibleEvent( key.channel(), userData );
            _sink.addEvent( event );
        }
    }
}
