/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.protocol;

import java.nio.ByteBuffer;
import org.jmock.Constraint;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003-12-09 01:54:07 $
 */
class MessageConstraint
    implements Constraint
{
    private final byte[] m_data;

    MessageConstraint( final byte[] data )
    {
        m_data = data;
    }

    public boolean eval( Object o )
    {
        if( o instanceof ByteBuffer )
        {
            final ByteBuffer byteBuffer = (ByteBuffer)o;
            //byteBuffer.limit()
        }
        return false;
    }
}
