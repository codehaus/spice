/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.handlers;

import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.handlers.AbstractDirectedHandler;
import org.realityforge.packet.Packet;
import org.realityforge.packet.events.NackEvent;
import org.realityforge.packet.events.PacketWriteRequestEvent;
import org.realityforge.packet.events.TransportDisconnectRequestEvent;
import org.realityforge.packet.session.Session;

/**
 * Simple handler that handles ack.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-13 06:49:19 $
 */
public class NackEventHandler
    extends AbstractDirectedHandler
{
    /**
     * Create handler.
     * 
     * @param sink the destination
     */
    public NackEventHandler( final EventSink sink )
    {
        super( sink );
    }

    /**
     * @see EventHandler#handleEvent(Object)
     */
    public void handleEvent( final Object event )
    {
        final NackEvent ce = (NackEvent)event;
        final short sequence = ce.getSequence();
        final Session session = ce.getSession();
        final Packet packet =
            session.getMessageQueue().getPacket( sequence );
        if( null == packet )
        {
            final TransportDisconnectRequestEvent error =
                new TransportDisconnectRequestEvent( session.getTransport(),
                                                     Protocol.ERROR_BAD_NACK );
            getSink().addEvent( error );
        }
        else
        {
            final PacketWriteRequestEvent response =
                new PacketWriteRequestEvent( session, packet );
            getSink().addEvent( response );
        }
    }
}
