/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.protocol;

import com.mockobjects.dynamic.C;
import com.mockobjects.dynamic.Mock;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import junit.framework.TestCase;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-11-26 02:09:41 $
 */
public class ProtocolWriterTestCase
    extends TestCase
{
    public void testForceWriteMessage()
        throws Exception
    {
        final byte[] data = new byte[]{1, 2, 3};
        final ByteBuffer message = ByteBuffer.wrap( data );

        final Mock channelMock = new Mock( WritableByteChannel.class );
        channelMock.matchAndReturn( "write", C.args( C.eq( message ) ), 3 );

        final WritableByteChannel channel =
            (WritableByteChannel)channelMock.proxy();

        final ProtocolWriter writer = new ProtocolWriter();
        writer.forceWriteMessage( message, channel );

        channelMock.verify();
    }

    public void testForceWriteMessageThatOverflows()
        throws Exception
    {
        final byte[] data = new byte[]{1, 2, 3};
        final ByteBuffer message = ByteBuffer.wrap( data );

        final Mock channelMock = new Mock( WritableByteChannel.class );
        channelMock.matchAndReturn( "write", C.args( C.eq( message ) ), 2 );

        final WritableByteChannel channel =
            (WritableByteChannel)channelMock.proxy();

        final ProtocolWriter writer = new ProtocolWriter();
        try
        {
            writer.forceWriteMessage( message, channel );
            fail( "Expected to get a BufferOverflow Exception" );
        }
        catch( final BufferOverflowException boe )
        {
            return;
        }

        channelMock.verify();
    }
}
