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
import org.codehaus.spice.netevent.events.ConnectEvent;
import org.realityforge.sca.selector.SelectorEventHandler;

/**
 * A SelectorEventHandler that generates ConnectEvents and sends them to an
 * EventSink.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-07 06:26:17 $
 */
public class ServerSocketSelectorEventHandler
    implements SelectorEventHandler
{
    /** The destination of ConnectEvents. */
    private final EventSink _sink;

    /**
     * Create handler with specified sink.
     * 
     * @param sink the event sink.
     */
    public ServerSocketSelectorEventHandler( final EventSink sink )
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
            final ConnectEvent event = new ConnectEvent( channel );
            _sink.addEvent( event );
        }
    }
}
