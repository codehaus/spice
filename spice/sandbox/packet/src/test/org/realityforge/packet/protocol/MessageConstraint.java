/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.protocol;

import com.mockobjects.constraint.Constraint;
import java.nio.ByteBuffer;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-11-12 11:09:09 $
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