/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet;

import java.nio.ByteBuffer;
import junit.framework.TestCase;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003-12-05 06:57:12 $
 */
public class PacketTestCase
    extends TestCase
{
    public void testPacket()
        throws Exception
    {
        final ByteBuffer data = ByteBuffer.allocate( 3 );
        final Packet packet = new Packet( (short)2, 3, data );
        assertEquals( "sequence", 2, packet.getSequence() );
        assertEquals( "flags", 3, packet.getFlags() );
        assertEquals( "data", data, packet.getData() );
    }

    public void testNullDataPassedToCtor()
        throws Exception
    {
        try
        {
            new Packet( (short)2, 2, null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "data", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null Data passed into Ctor" );
    }
}
