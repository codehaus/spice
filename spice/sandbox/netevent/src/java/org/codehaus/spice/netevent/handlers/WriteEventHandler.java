/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netevent.handlers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.event.impl.collections.Buffer;
import org.codehaus.spice.netevent.events.WriteErrorEvent;
import org.codehaus.spice.netevent.events.WritePossibleEvent;
import org.codehaus.spice.netevent.transport.ChannelTransport;

/**
 * Handler for writing data to channel.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-08 03:41:14 $
 */
public class WriteEventHandler
    extends AbstractIOEventHandler
{
    /**
     * Create handler with specified destination sink.
     * 
     * @param sink the destination
     */
    public WriteEventHandler( final EventSink sink )
    {
        super( sink );
    }

    /**
     * @see EventHandler#handleEvent(Object)
     */
    public void handleEvent( final Object event )
    {
        final WritePossibleEvent we = (WritePossibleEvent)event;
        final ChannelTransport transport = we.getTransport();
        final WritableByteChannel channel =
            (WritableByteChannel)transport.getChannel();

        final Buffer transmitBuffer = transport.getTransmitBuffer();
        final ByteBuffer buffer = (ByteBuffer)transmitBuffer.peek();
        final int remaining = buffer.remaining();

        try
        {
            final int count = channel.write( buffer );
            if( remaining == count )
            {
                transmitBuffer.pop();
                releaseBuffer( buffer );
            }
        }
        catch( final IOException ioe )
        {
            final WriteErrorEvent error =
                new WriteErrorEvent( transport, ioe );
            getSink().addEvent( error );
        }
    }

}
