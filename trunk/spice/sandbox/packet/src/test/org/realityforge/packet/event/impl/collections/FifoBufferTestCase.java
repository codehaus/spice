package org.realityforge.packet.event.impl.collections;

import junit.framework.TestCase;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-05 03:31:33 $
 */
public class FifoBufferTestCase
    extends TestCase
{
    public void testNegativeSizedBuffer()
        throws Exception
    {
        try
        {
            new BoundedFifoBuffer( -1 );
        }
        catch( final IllegalArgumentException iae )
        {
            assertEquals( "iae.getMessage()",
                          "size <= 0", iae.getMessage() );
            return;
        }
        fail( "Exected to fail due to negative buffer size " );
    }

    public void testCreate()
        throws Exception
    {
        final AbstractFifoBuffer buffer = new BoundedFifoBuffer( 3 );
        assertEquals( "buffer.size()", 0, buffer.size() );
        assertEquals( "buffer.m_buffer.length", 3, buffer.m_buffer.length );
        assertEquals( "buffer.m_head", 0, buffer.m_head );
        assertEquals( "buffer.m_tail", 0, buffer.m_tail );
        assertEquals( "buffer.m_isWrappedBuffer",
                      false, buffer.m_isWrappedBuffer );
    }

    public void testSimpleAdd()
        throws Exception
    {
        final AbstractFifoBuffer buffer = new BoundedFifoBuffer( 3 );
        final Object object = new Object();
        assertEquals( "buffer.add( object )", true, buffer.add( object ) );
        assertEquals( "buffer.size()", 1, buffer.size() );
        assertEquals( "buffer.m_head", 0, buffer.m_head );
        assertEquals( "buffer.m_tail", 1, buffer.m_tail );
        assertEquals( "buffer.m_isWrappedBuffer",
                      false, buffer.m_isWrappedBuffer );
        assertEquals( "buffer.m_buffer[ 0 ]", object, buffer.m_buffer[ 0 ] );
        assertEquals( "buffer.add( object )", true, buffer.add( object ) );
        assertEquals( "buffer.size()", 2, buffer.size() );
        assertEquals( "buffer.m_head", 0, buffer.m_head );
        assertEquals( "buffer.m_tail", 2, buffer.m_tail );
        assertEquals( "buffer.m_isWrappedBuffer",
                      false, buffer.m_isWrappedBuffer );
        assertEquals( "buffer.m_buffer[ 1 ]", object, buffer.m_buffer[ 1 ] );
    }

    public void testAddWithWrap()
        throws Exception
    {
        final AbstractFifoBuffer buffer = new BoundedFifoBuffer( 3 );
        final Object object1 = new Object();
        final Object object2 = new Object();
        final Object object3 = new Object();
        final Object object4 = new Object();

        assertEquals( "buffer.add( object1 )",
                      true,
                      buffer.add( object1 ) );
        assertEquals( "buffer.add( object2 )",
                      true,
                      buffer.add( object2 ) );
        assertEquals( "buffer.add( object3 )",
                      true,
                      buffer.add( object3 ) );
        assertEquals( "buffer.size()", 3, buffer.size() );
        assertEquals( "buffer.m_head", 0, buffer.m_head );
        assertEquals( "buffer.m_tail", 0, buffer.m_tail );
        assertEquals( "buffer.m_isWrappedBuffer",
                      true, buffer.m_isWrappedBuffer );

        assertEquals( "buffer.peek()", object1, buffer.peek() );
        assertEquals( "buffer.size() After Peek", 3, buffer.size() );
        assertEquals( "buffer.m_head After Peek", 0, buffer.m_head );
        assertEquals( "buffer.m_tail After Peek", 0, buffer.m_tail );
        assertEquals( "buffer.m_isWrappedBuffer After Peek",
                      true, buffer.m_isWrappedBuffer );

        assertEquals( "buffer.pop()", object1, buffer.pop() );
        assertEquals( "buffer.size() After Pop", 2, buffer.size() );
        assertEquals( "buffer.m_head After Pop", 1, buffer.m_head );
        assertEquals( "buffer.m_tail After Pop", 0, buffer.m_tail );
        assertEquals( "buffer.m_isWrappedBuffer After Pop",
                      true, buffer.m_isWrappedBuffer );

        assertEquals( "buffer.add( object4 )",
                      true,
                      buffer.add( object4 ) );
        assertEquals( "buffer.size() After Add", 3, buffer.size() );
        assertEquals( "buffer.m_head After Add", 1, buffer.m_head );
        assertEquals( "buffer.m_tail After Add", 1, buffer.m_tail );
        assertEquals( "buffer.m_isWrappedBuffer After Add",
                      true, buffer.m_isWrappedBuffer );
        assertEquals( "buffer.m_buffer[ 0 ]", object4, buffer.m_buffer[ 0 ] );
        assertEquals( "buffer.m_buffer[ 1 ]", object2, buffer.m_buffer[ 1 ] );
        assertEquals( "buffer.m_buffer[ 2 ]", object3, buffer.m_buffer[ 2 ] );
    }

    public void testSimpleAddAll()
        throws Exception
    {
        final AbstractFifoBuffer buffer = new BoundedFifoBuffer( 3 );
        final Object object = new Object();
        final Object[] objects = new Object[]{object};
        assertEquals( "buffer.addAll( objects )", true, buffer.addAll( objects ) );
        assertEquals( "buffer.size()", 1, buffer.size() );
        assertEquals( "buffer.m_head", 0, buffer.m_head );
        assertEquals( "buffer.m_tail", 1, buffer.m_tail );
        assertEquals( "buffer.m_isWrappedBuffer",
                      false, buffer.m_isWrappedBuffer );
        assertEquals( "buffer.m_buffer[ 0 ]", object, buffer.m_buffer[ 0 ] );
    }

    public void testFillingAddAll()
        throws Exception
    {
        final AbstractFifoBuffer buffer = new BoundedFifoBuffer( 3 );
        final Object object1 = new Object();
        final Object object2 = new Object();
        final Object object3 = new Object();
        final Object[] objects = new Object[]{object1, object2, object3};
        assertEquals( "buffer.addAll( objects )", true,
                      buffer.addAll( objects ) );
        assertEquals( "buffer.size()", 3, buffer.size() );
        assertEquals( "buffer.m_head", 0, buffer.m_head );
        assertEquals( "buffer.m_tail", 0, buffer.m_tail );
        assertEquals( "buffer.m_isWrappedBuffer",
                      true, buffer.m_isWrappedBuffer );
        assertEquals( "buffer.m_buffer[ 0 ]", object1, buffer.m_buffer[ 0 ] );
        assertEquals( "buffer.m_buffer[ 1 ]", object2, buffer.m_buffer[ 1 ] );
        assertEquals( "buffer.m_buffer[ 2 ]", object3, buffer.m_buffer[ 2 ] );
    }

    public void testAddAllWithWrap()
        throws Exception
    {
        final AbstractFifoBuffer buffer = new BoundedFifoBuffer( 3 );
        final Object object1 = new Object();
        final Object object2 = new Object();
        final Object object3 = new Object();
        final Object object4 = new Object();

        assertEquals( "buffer.add( object1 )",
                      true,
                      buffer.add( object1 ) );
        assertEquals( "buffer.add( object2 )",
                      true,
                      buffer.add( object2 ) );
        assertEquals( "buffer.add( object3 )",
                      true,
                      buffer.add( object3 ) );
        assertEquals( "buffer.size()", 3, buffer.size() );
        assertEquals( "buffer.m_head", 0, buffer.m_head );
        assertEquals( "buffer.m_tail", 0, buffer.m_tail );
        assertEquals( "buffer.m_isWrappedBuffer",
                      true, buffer.m_isWrappedBuffer );

        assertEquals( "buffer.peek()", object1, buffer.peek() );
        assertEquals( "buffer.size() After Peek", 3, buffer.size() );
        assertEquals( "buffer.m_head After Peek", 0, buffer.m_head );
        assertEquals( "buffer.m_tail After Peek", 0, buffer.m_tail );
        assertEquals( "buffer.m_isWrappedBuffer After Peek",
                      true, buffer.m_isWrappedBuffer );

        assertEquals( "buffer.pop()", object1, buffer.pop() );
        assertEquals( "buffer.size() After Pop", 2, buffer.size() );
        assertEquals( "buffer.m_head After Pop", 1, buffer.m_head );
        assertEquals( "buffer.m_tail After Pop", 0, buffer.m_tail );
        assertEquals( "buffer.m_isWrappedBuffer After Pop",
                      true, buffer.m_isWrappedBuffer );

        assertEquals( "buffer.addAll( new Object[]{object4} )",
                      true,
                      buffer.addAll( new Object[]{object4} ) );
        assertEquals( "buffer.size() After Add", 3, buffer.size() );
        assertEquals( "buffer.m_head After Add", 1, buffer.m_head );
        assertEquals( "buffer.m_tail After Add", 1, buffer.m_tail );
        assertEquals( "buffer.m_isWrappedBuffer After Add",
                      true, buffer.m_isWrappedBuffer );

        assertEquals( "buffer.m_buffer[ 0 ]", object4, buffer.m_buffer[ 0 ] );
        assertEquals( "buffer.m_buffer[ 1 ]", object2, buffer.m_buffer[ 1 ] );
        assertEquals( "buffer.m_buffer[ 2 ]", object3, buffer.m_buffer[ 2 ] );
    }
}
