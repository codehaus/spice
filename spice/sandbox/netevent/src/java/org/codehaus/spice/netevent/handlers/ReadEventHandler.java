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
import java.nio.channels.ReadableByteChannel;
import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.buffers.BufferManager;
import org.codehaus.spice.netevent.events.CloseChannelRequestEvent;
import org.codehaus.spice.netevent.events.ReadErrorEvent;
import org.codehaus.spice.netevent.events.ReadEvent;
import org.codehaus.spice.netevent.events.ReadPossibleEvent;
import org.codehaus.spice.netevent.transport.ChannelTransport;

/**
 * Handler for reading data from channel.
 * 
 * @author Peter Donald
 * @version $Revision: 1.5 $ $Date: 2004-01-20 01:08:30 $
 */
public class ReadEventHandler
    extends AbstractIOEventHandler
{
    /**
     * Create handler with specified destination sink.
     * 
     * @param sink the destination
     * @param bufferManager the bufferManager
     */
    public ReadEventHandler( final EventSink sink,
                             final BufferManager bufferManager )
    {
        super( sink, bufferManager );
    }

    /**
     * @see EventHandler#handleEvent(Object)
     */
    public void handleEvent( final Object event )
    {
        final ReadPossibleEvent ce = (ReadPossibleEvent)event;
        final ChannelTransport transport = ce.getTransport();
        final ReadableByteChannel channel =
            (ReadableByteChannel)transport.getChannel();
        if( !channel.isOpen() )
        {
            return;
        }

        final ByteBuffer buffer = aquireBuffer( ce );
        try
        {
            final int ip = buffer.position();
            final int count = channel.read( buffer );
            System.out.println( "original buffer.position( ) = " + ip );
            System.out.println( "count = " + count );
            System.out.println( "buffer.position( ) = " + buffer.position() );
            System.out.println( "buffer.limit( ) = " + buffer.limit() );
            System.out.println( "buffer.get( 0 ) = " + buffer.get( 0 ) +
                                "/" + ((char)buffer.get( 0 )) );

            if( -1 == count )
            {
                final CloseChannelRequestEvent result =
                    new CloseChannelRequestEvent( transport );
                getSink().addEvent( result );
                getBufferManager().releaseBuffer( buffer );
            }
            else if( 0 == count )
            {
                getBufferManager().releaseBuffer( buffer );
            }
            else
            {
                buffer.flip();
                final ReadEvent result =
                    new ReadEvent( transport, buffer );
                getSink().addEvent( result );
            }
        }
        catch( final IOException ioe )
        {
            final ReadErrorEvent error =
                new ReadErrorEvent( transport,
                                    ioe );
            getSink().addEvent( error );
        }
    }
}
