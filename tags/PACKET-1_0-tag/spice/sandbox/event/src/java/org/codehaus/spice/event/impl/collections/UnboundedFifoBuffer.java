/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.event.impl.collections;

/**
 * A unbounded FIFO Buffer implementation.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-16 02:03:12 $
 */
public final class UnboundedFifoBuffer
    extends AbstractFifoBuffer
{
    /**
     * Create a buffer with specified initial size.
     * 
     * @param size the initial size of the buffer
     */
    public UnboundedFifoBuffer( final int size )
    {
        super( size );
    }

    /**
     * @see AbstractFifoBuffer#doAddAll(Object[])
     */
    protected boolean doAddAll( final Object[] objects )
    {
        for( int i = 0; i < objects.length; i++ )
        {
            add( objects[ i ] );
        }
        return true;
    }

    /**
     * @see AbstractFifoBuffer#doAdd(Object)
     */
    protected boolean doAdd( final Object object )
    {
        if( size() + 1 >= m_buffer.length )
        {
            final int newSize = ((m_buffer.length - 1) * 2) + 1;
            final Object[] tmp = new Object[ newSize ];

            int j = 0;
            for( int i = m_head; i != m_tail; )
            {
                tmp[ j ] = m_buffer[ i ];
                m_buffer[ i ] = null;

                j++;
                i++;
                if( i == m_buffer.length )
                {
                    i = 0;
                }
            }

            m_buffer = tmp;
            m_head = 0;
            m_tail = j;
            m_isWrappedBuffer = false;
        }

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
