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
import org.codehaus.spice.event.impl.collections.Buffer;
import org.codehaus.spice.event.impl.collections.UnboundedFifoBuffer;

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-01-08 03:41:14 $
 */
public class TcpTransportTestCase
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
        final UnboundedFifoBuffer rx = new UnboundedFifoBuffer( 1 );
        final UnboundedFifoBuffer tx = new UnboundedFifoBuffer( 1 );
        final ChannelTransport transport =
            new ChannelTransport( m_channel, rx, tx );
        assertEquals( "channel", m_channel, transport.getChannel() );
        assertEquals( "key", null, transport.getKey() );
        assertEquals( "getReceiveBuffer", rx, transport.getReceiveBuffer() );
        assertEquals( "getTransmitBuffer", tx, transport.getTransmitBuffer() );
    }

    public void testNullChannelPassedToCtor()
        throws Exception
    {
        final UnboundedFifoBuffer rx = new UnboundedFifoBuffer( 1 );
        final UnboundedFifoBuffer tx = new UnboundedFifoBuffer( 1 );
        try
        {
            new ChannelTransport( null, rx, tx );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "channel", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null Channel passed into Ctor" );
    }

    public void testNullReceiveBufferPassedToCtor()
        throws Exception
    {
        final SocketChannel channel = SocketChannel.open();
        final UnboundedFifoBuffer tx = new UnboundedFifoBuffer( 1 );
        try
        {
            new ChannelTransport( channel, null, tx );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "receiveBuffer",
                          npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null receiveBuffer passed into Ctor" );
    }

    public void testNullTransmitBufferPassedToCtor()
        throws Exception
    {
        final SocketChannel channel = SocketChannel.open();
        final UnboundedFifoBuffer rx = new UnboundedFifoBuffer( 1 );
        try
        {
            new ChannelTransport( channel, rx, null );
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
                new ChannelTransport( m_channel,
                                      new UnboundedFifoBuffer( 1 ),
                                      new UnboundedFifoBuffer( 1 ) );
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
            new ChannelTransport( m_channel,
                                  new UnboundedFifoBuffer( 1 ),
                                  new UnboundedFifoBuffer( 1 ) );
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
            new ChannelTransport( m_channel, new UnboundedFifoBuffer( 1 ),
                                  new UnboundedFifoBuffer( 1 ) );
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
            new ChannelTransport( m_channel, new UnboundedFifoBuffer( 1 ),
                                  new UnboundedFifoBuffer( 1 ) );
        assertEquals( "SelectOps",
                      SelectionKey.OP_READ,
                      transport.getSelectOps() );
    }
}
