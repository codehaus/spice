package org.realityforge.packet.event.impl.collections;

/**
 * A bounded FIFO Buffer implementation.
 * 
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2003-12-05 03:31:33 $
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
