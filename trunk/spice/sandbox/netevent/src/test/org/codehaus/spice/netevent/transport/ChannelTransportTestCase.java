/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netevent.transport;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import junit.framework.TestCase;
import org.codehaus.spice.event.impl.NullEventSink;
import org.codehaus.spice.event.impl.collections.Buffer;
import org.codehaus.spice.event.impl.collections.UnboundedFifoBuffer;
import org.codehaus.spice.netevent.buffers.DefaultBufferManager;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2004-01-12 04:12:19 $
 */
public class ChannelTransportTestCase
    extends TestCase
{
    private SocketChannel m_channel;

    protected void setUp()
        throws Exception
    {
        m_channel = SocketChannel.open();
    }

    protected void tearDown()
        throws Exception
    {
        m_channel.close();
    }

    public void testCreate()
        throws Exception
    {
        final UnboundedFifoBuffer tx = new UnboundedFifoBuffer( 1 );
        final ChannelTransport transport = newTransport( tx );
        assertEquals( "channel", m_channel, transport.getChannel() );
        assertEquals( "key", null, transport.getKey() );
        assertEquals( "getTransmitBuffer", tx, transport.getTransmitBuffer() );
    }

    private ChannelTransport newTransport( final UnboundedFifoBuffer tx )
    {
        return new ChannelTransport( m_channel, tx,
                                     new DefaultBufferManager(),
                                     new NullEventSink() );
    }

    public void testNullChannelPassedToCtor()
        throws Exception
    {
        final UnboundedFifoBuffer tx = new UnboundedFifoBuffer( 1 );
        try
        {
            new ChannelTransport( null,
                                  tx,
                                  new DefaultBufferManager(),
                                  new NullEventSink() );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "channel", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null Channel passed into Ctor" );
    }

    public void testNullTransmitBufferPassedToCtor()
        throws Exception
    {
        try
        {
            newTransport( null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "transmitBuffer",
                          npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null transmitBuffer passed into Ctor" );
    }

    public void testCloseTransportWithOpenChannel()
        throws Exception
    {
        final ServerSocketChannel ssc = ServerSocketChannel.open();

        final InetSocketAddress socketAddress =
            new InetSocketAddress( InetAddress.getLocalHost(), 19972 );
        try
        {
            ssc.socket().bind( socketAddress );
            m_channel.configureBlocking( false );
            m_channel.connect( socketAddress );
            final ChannelTransport transport =
                newTransport( new UnboundedFifoBuffer( 1 ) );
            assertEquals( "channel.isOpen()", true, m_channel.isOpen() );
            transport.close();
            assertEquals( "key", null, transport.getKey() );
            assertEquals( "channel.isOpen()", false, m_channel.isOpen() );
        }
        finally
        {
            ssc.close();
        }
    }

    public void testCloseTransportWithNonOpenChannel()
        throws Exception
    {
        final ChannelTransport transport =
            newTransport( new UnboundedFifoBuffer( 1 ) );
        m_channel.close();
        assertEquals( "channel.isOpen()", false, m_channel.isOpen() );
        transport.close();
        assertEquals( "key", null, transport.getKey() );
        assertEquals( "channel.isOpen()", false, m_channel.isOpen() );
    }

    public void testGetSelectOpsOnWriteable()
        throws Exception
    {
        final ChannelTransport transport =
            newTransport( new UnboundedFifoBuffer( 1 ) );
        final Buffer writeBuffer = transport.getTransmitBuffer();
        writeBuffer.add( new Object() );
        assertEquals( "SelectOps",
                      SelectionKey.OP_WRITE | SelectionKey.OP_READ,
                      transport.getSelectOps() );
    }

    public void testGetSelectOpsOnReadable()
        throws Exception
    {
        final ChannelTransport transport =
            new ChannelTransport( m_channel,
                                  new UnboundedFifoBuffer( 1 ),
                                  new DefaultBufferManager(),
                                  new NullEventSink() );
        assertEquals( "SelectOps",
                      SelectionKey.OP_READ,
                      transport.getSelectOps() );
    }
}
