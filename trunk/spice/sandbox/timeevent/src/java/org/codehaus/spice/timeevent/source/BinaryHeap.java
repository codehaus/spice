package org.codehaus.spice.timeevent.source;

import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * The BinaryHeap class is an implementation of a priority queue.
 */
public final class BinaryHeap
{
    /**
     * Comparator used to instantiate a min heap - assumes contents implement the Comparable interface.
     */
    public static final Comparator MIN_COMPARATOR = new MinComparator();

    /**
     * Comparator used to instantiate a max heap - assumes contents implement the Comparable interface.
     */
    public static final Comparator MAX_COMPARATOR = new MaxComparator();

    private int m_size;
    private Object[] m_elements;
    private Comparator m_comparator;

    /**
     * Instantiates a new binary heap with the given initial capacity and ordered using the given Comparator.
     *
     * @param capacity the size of the heap
     * @param comparator to order the contents of the heap
     */
    public BinaryHeap( final int capacity, final Comparator comparator )
    {
        //+1 as 0 is noop
        m_elements = new Object[ capacity + 1 ];
        m_comparator = comparator;
    }

    /**
     * Clear all elements from queue.
     */
    public void clear()
    {
        m_size = 0;
    }

    /**
     * Test if queue is empty.
     *
     * @return true if queue is empty else false.
     */
    public boolean isEmpty()
    {
        return ( 0 == m_size );
    }

    /**
     * Test if queue is full.
     *
     * @return true if queue is full else false.
     */
    public boolean isFull()
    {
        //+1 as element 0 is noop
        return ( m_elements.length == m_size + 1 );
    }

    /**
     * Returns the number of elements currently on the heap.
     *
     * @return the size of the heap.
     */
    public int size()
    {
        return m_size;
    }

    /**
     * Insert an element into queue.
     *
     * @param element the element to be inserted
     */
    public void insert( final Object element )
    {
        if( isFull() )
        {
            grow();
        }

        percolateUpHeap( element );
    }

    /**
     * Return element on top of heap but don't remove it.
     *
     * @return the element at top of heap
     * @throws NoSuchElementException if isEmpty() == true
     */
    public Object peek() throws NoSuchElementException
    {
        if( isEmpty() )
        {
            throw new NoSuchElementException();
        }
        else
        {
            return m_elements[ 1 ];
        }
    }

    /**
     * Return element on top of heap and remove it.
     *
     * @return the element at top of heap
     * @throws NoSuchElementException if isEmpty() == true
     */
    public Object pop() throws NoSuchElementException
    {
        final Object result = peek();
        m_elements[ 1 ] = m_elements[ m_size-- ];

        //set the unused element to 'null' so that the garbage collector
        //can free the object if not used anywhere else.(remove reference)
        m_elements[ m_size + 1 ] = null;

        if( m_size != 0 )
        {
            percolateDownHeap( 1 );
        }

        return result;
    }

    /**
     * Percolate element down heap from top.
     *
     * @param index the index of element
     */
    void percolateDownHeap( final int index )
    {
        final Object element = m_elements[ index ];

        int hole = index;
        int child = hole << 1;

        while( child <= m_size )
        {
            //if we have a right child and that child can not be percolated
            //up then move onto other child
            if( child != m_size &&
                m_comparator.compare( m_elements[ child + 1 ], m_elements[ child ] ) < 0 )
            {
                child++;
            }

            //if we found resting place of bubble then terminate search
            if( m_comparator.compare( m_elements[ child ], element ) >= 0 )
            {
                break;
            }

            m_elements[ hole ] = m_elements[ child ];
            hole = child;
            child = hole << 1;
        }

        m_elements[ hole ] = element;
    }

    /**
     * Percolate element up heap from bottom.
     *
     * @param element the element
     */
    void percolateUpHeap( final Object element )
    {
        int hole = ++m_size;
        int next = hole >> 1;

        m_elements[ hole ] = element;

        while( hole > 1 &&
               m_comparator.compare( element, m_elements[ next ] ) < 0 )
        {
            m_elements[ hole ] = m_elements[ next ];
            hole = next;
            next = hole >> 1;
        }

        m_elements[ hole ] = element;
    }

    /**
     * Grows the heap by a factor of 2.
     */
    void grow()
    {
        final Object[] elements =
            new Object[ m_elements.length * 2 ];
        System.arraycopy( m_elements, 0, elements, 0, m_elements.length );
        m_elements = elements;
    }

    private static final class MinComparator
        implements Comparator
    {
        public final int compare( final Object lhs, final Object rhs )
        {
            return ( (Comparable)lhs ).compareTo( rhs );
        }
    }

    private static final class MaxComparator
        implements Comparator
    {
        public final int compare( final Object lhs, final Object rhs )
        {
            return ( (Comparable)rhs ).compareTo( lhs );
        }
    }
}

