/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netevent.selector;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.events.BufferOverflowEvent;
import org.codehaus.spice.netevent.events.BufferUnderflowEvent;
import org.codehaus.spice.netevent.events.CloseEvent;
import org.codehaus.spice.netevent.events.ReadErrorEvent;
import org.codehaus.spice.netevent.events.ReadEvent;
import org.codehaus.spice.netevent.events.WriteErrorEvent;
import org.codehaus.spice.netevent.events.WriteEvent;
import org.codehaus.spice.netevent.transport.CircularBuffer;
import org.codehaus.spice.netevent.transport.TcpTransport;
import org.realityforge.sca.selector.SelectorEventHandler;

/**
 * The handler receives read and write events for channels and generates
 * corresponding events to pass onto an EventSink.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-07 06:26:17 $
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
        final TcpTransport transport = (TcpTransport)userData;

        if( key.isWritable() )
        {
            handleWriteEvent( transport );
        }
        if( key.isValid() && key.isReadable() )
        {
            handleReadEvent( transport );
        }

        transport.getKey().interestOps( transport.getSelectOps() );
    }

    /**
     * Perform a write for specified transport.
     * 
     * @param transport the transport
     */
    void handleWriteEvent( final TcpTransport transport )
    {
        final CircularBuffer buffer = transport.getTransmitBuffer();
        if( 0 >= buffer.getAvailable() )
        {
            final BufferUnderflowEvent event =
                new BufferUnderflowEvent( transport );
            _sink.addEvent( event );
        }
        else
        {
            final ByteBuffer[] byteBuffers = buffer.asReadBuffers();
            try
            {
                final SocketChannel channel = transport.getChannel();
                final int count = (int)channel.write( byteBuffers );
                if( -1 != count )
                {
                    buffer.readBytes( count );
                    final WriteEvent event = new WriteEvent( transport, count );
                    _sink.addEvent( event );
                }
                else
                {
                    final CloseEvent event = new CloseEvent( transport );
                    _sink.addEvent( event );
                }
            }
            catch( final IOException ioe )
            {
                final WriteErrorEvent event =
                    new WriteErrorEvent( transport, ioe );
                _sink.addEvent( event );
            }
        }
    }

    /**
     * Perform a read for specified transport.
     * 
     * @param transport the transport
     */
    void handleReadEvent( final TcpTransport transport )
    {
        final CircularBuffer buffer = transport.getReceiveBuffer();
        if( 0 >= buffer.getSpace() )
        {
            final BufferOverflowEvent event =
                new BufferOverflowEvent( transport );
            _sink.addEvent( event );
        }
        else
        {
            final ByteBuffer[] byteBuffers = buffer.asWriteBuffers();
            try
            {
                final SocketChannel channel = transport.getChannel();
                final int count = (int)channel.read( byteBuffers );
                if( -1 != count )
                {
                    buffer.writeBytes( count );
                    final ReadEvent event = new ReadEvent( transport, count );
                    _sink.addEvent( event );
                }
                else
                {
                    final CloseEvent event = new CloseEvent( transport );
                    _sink.addEvent( event );
                }
            }
            catch( final IOException ioe )
            {
                final ReadErrorEvent event =
                    new ReadErrorEvent( transport, ioe );
                _sink.addEvent( event );
            }
        }
    }
}
