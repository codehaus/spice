/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.event.impl.collections;

/**
 * A bounded FIFO Buffer implementation.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-16 02:03:12 $
 */
public final class BoundedFifoBuffer
    extends AbstractFifoBuffer
{
    /**
     * Create a buffer with specified size.
     * 
     * @param size the size of the buffer
     */
    public BoundedFifoBuffer( final int size )
    {
        super( size );
    }

    /**
     * @see AbstractFifoBuffer#doAddAll(Object[])
     */
    protected boolean doAddAll( final Object[] objects )
    {
        if( size() + objects.length > m_buffer.length )
        {
            return false;
        }
        else
        {
            for( int i = 0; i < objects.length; i++ )
            {
                add( objects[ i ] );
            }
            return true;
        }
    }

    /**
     * @see AbstractFifoBuffer#doAdd(Object)
     */
    protected boolean doAdd( final Object object )
    {
        if( size() + 1 > m_buffer.length )
        {
            return false;
        }
        else
        {
            m_buffer[ m_tail ] = object;
            m_tail++;
            if( m_tail >= m_buffer.length )
            {
                m_tail = 0;
                m_isWrappedBuffer = true;
            }
            return true;
        }
    }
}
