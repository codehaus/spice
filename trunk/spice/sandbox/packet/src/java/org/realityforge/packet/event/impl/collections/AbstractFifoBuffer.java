package org.realityforge.packet.event.impl.collections;

/**
 * A unbounded FIFO Buffer implementation. This class is loosely based on the
 * Jakarta-Commons Collections package Buffer implementations.
 * 
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2003-12-05 02:28:33 $
 */
public abstract class AbstractFifoBuffer
    implements Buffer
{
    /** The underlying object array. */
    Object[] m_buffer;

    /** The pointer to first element. */
    int m_head;

    /** The pointer to last element. */
    int m_tail;

    /**
     * Create a buffer with specified initial size.
     * 
     * @param size the initial size of the buffer
     */
    public AbstractFifoBuffer( final int size )
    {
        if( size <= 0 )
        {
            throw new IllegalArgumentException( "size <= 0" );
        }
        m_buffer = new Object[ size + 1 ];
        m_head = 0;
        m_tail = 0;
    }

    /**
     * @see Buffer#size()
     */
    public int size()
    {
        if( m_tail < m_head )
        {
            return m_buffer.length - m_head + m_tail;
        }
        else
        {
            return m_tail - m_head;
        }
    }

    /**
     * @see Buffer#addAll(Object[])
     */
    public boolean addAll( final Object[] objects )
    {
        if( null == objects )
        {
            throw new NullPointerException( "objects" );
        }

        return doAddAll( objects );
    }

    /**
     * @see Buffer#add(Object)
     */
    public boolean add( final Object object )
    {
        if( null == object )
        {
            throw new NullPointerException( "object" );
        }
        return doAdd( object );
    }

    /**
     * @see Buffer#peek()
     */
    public Object peek()
    {
        if( 0 == size() )
        {
            return null;
        }
        else
        {
            return m_buffer[ m_head ];
        }
    }

    /**
     * @see Buffer#pop()
     */
    public Object pop()
    {
        final Object element = peek();
        if( null != element )
        {
            m_buffer[ m_head ] = null;

            m_head++;
            if( m_head >= m_buffer.length )
            {
                m_head = 0;
            }
        }

        return element;
    }

    /**
     * Perform addition of objects to buffer.
     * 
     * @param objects the objects
     * @return true if addition successful
     */
    protected abstract boolean doAddAll( Object[] objects );

    /**
     * Perform addition of object to buffer.
     * 
     * @param object the object
     * @return true if addition successful
     */
    protected abstract boolean doAdd( Object object );
}
