/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.transport;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import junit.framework.TestCase;

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2003-12-05 06:57:12 $
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
        final TcpTransport transport =
            new TcpTransport( m_channel, 20, 30 );
        assertEquals( "channel", m_channel, transport.getChannel() );
        assertEquals( "key", null, transport.getKey() );
        assertEquals( "readBuffer.size",
                      20,
                      transport.getReadBuffer().getCapacity() );
        assertEquals( "writeBuffer.size",
                      30,
                      transport.getWriteBuffer().getCapacity() );
    }

    public void testNullChannelPassedToCtor()
        throws Exception
    {
        try
        {
            new TcpTransport( null, 20, 20 );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "channel", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null Channel passed into Ctor" );
    }

    public void testSetKey()
        throws Exception
    {
        final TcpTransport transport =
            new TcpTransport( m_channel, 20, 30 );
        final MockSelectorKey key = new MockSelectorKey();
        transport.setKey( key );
        assertEquals( "key", key, transport.getKey() );
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
            final TcpTransport transport =
                new TcpTransport( m_channel, 20, 30 );
            final MockSelectorKey key = new MockSelectorKey();
            transport.setKey( key );
            assertEquals( "key", key, transport.getKey() );
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
        final TcpTransport transport =
            new TcpTransport( m_channel, 20, 30 );
        m_channel.close();
        final MockSelectorKey key = new MockSelectorKey();
        transport.setKey( key );
        assertEquals( "key", key, transport.getKey() );
        assertEquals( "channel.isOpen()", false, m_channel.isOpen() );
        transport.close();
        assertEquals( "key", null, transport.getKey() );
        assertEquals( "channel.isOpen()", false, m_channel.isOpen() );
    }

    public void testGetSelectOpsOnWriteable()
        throws Exception
    {
        final TcpTransport transport =
            new TcpTransport( m_channel, 20, 30 );
        final CircularBuffer writeBuffer = transport.getWriteBuffer();
        final CircularBuffer readBuffer = transport.getReadBuffer();
        writeBuffer.writeBytes( writeBuffer.getCapacity() );
        readBuffer.writeBytes( readBuffer.getCapacity() );
        assertEquals( "SelectOps",
                      SelectionKey.OP_WRITE,
                      transport.getSelectOps() );
    }

    public void testGetSelectOpsOnReadable()
        throws Exception
    {
        final TcpTransport transport =
            new TcpTransport( m_channel, 20, 30 );
        assertEquals( "SelectOps",
                      SelectionKey.OP_READ,
                      transport.getSelectOps() );
    }

    public void testGetSelectOpsWithBothOps()
        throws Exception
    {
        final TcpTransport transport =
            new TcpTransport( m_channel, 20, 30 );
        final CircularBuffer readBuffer = transport.getReadBuffer();
        final CircularBuffer writeBuffer = transport.getWriteBuffer();
        readBuffer.writeBytes( 5 );
        writeBuffer.writeBytes( 5 );
        final int operations =
            SelectionKey.OP_READ | SelectionKey.OP_WRITE;
        assertEquals( "SelectOps", operations, transport.getSelectOps() );
    }

    public void testGetSelectOpsWithNoOps()
        throws Exception
    {
        final TcpTransport transport =
            new TcpTransport( m_channel, 20, 30 );
        final CircularBuffer readBuffer = transport.getReadBuffer();
        readBuffer.writeBytes( readBuffer.getCapacity() );
        System.out.println( "getSpace() = " + readBuffer.getSpace() );
        System.out.println( "getCapacity() = " + readBuffer.getCapacity() );
        System.out.println( "getAvailable() = " + readBuffer.getAvailable() );
        System.out.println(
            "isWrappedBuffer() = " + readBuffer.isWrappedBuffer() );
        assertEquals( "SelectOps", 0, transport.getSelectOps() );
    }
}
